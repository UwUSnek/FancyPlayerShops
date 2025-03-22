package com.snek.fancyplayershops;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.joml.Vector4i;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import com.snek.fancyplayershops.CustomDisplays.CustomItemDisplay;
import com.snek.fancyplayershops.CustomDisplays.CustomTextDisplay;
import com.snek.fancyplayershops.utils.Scheduler;
import com.snek.fancyplayershops.utils.Utils;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.decoration.DisplayEntity.BillboardMode;
import net.minecraft.entity.decoration.DisplayEntity.ItemDisplayEntity;
import net.minecraft.entity.decoration.DisplayEntity.TextDisplayEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;








// TODO fix broken shops and blocks if they don't exist in the world when the map is loaded
// TODO purge stray focus displays on entity loading (use custom entity data)
public class Shop {
    private static final File SHOP_STORAGE_DIR;
    static {
        final Path shopStorageDirPath = FabricLoader.getInstance().getConfigDir().resolve(FancyPlayerShops.MOD_ID + "/shops");
        try {
            Files.createDirectories(shopStorageDirPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SHOP_STORAGE_DIR = shopStorageDirPath.toFile();
    }

    // Stores the shops of players, identifying them by their owner's UUID and their coordinates and world in the format "x,y,z,worldId"
    private static final Map<String, Shop> shopsByCoords = new HashMap<>();
    private static final Map<String, Shop> shopsByOwner  = new HashMap<>();

    // Focus display data
    private static final HashSet<UUID> activeFocusDisplays = new HashSet<>(); //! Used to avoid purges. Stray displays won't be in here
    private static final int BG_TRANSITION_TIME_IN = 5; // Measured in ticks
    private static final int BG_TRANSITION_TIME_OUT = 10; // Measured in ticks
    private static final Vector4i BG_FOCUSED   = new Vector4i(255, 40, 40, 40);
    private static final Vector4i BG_UNFOCUSED = new Vector4i(64,  0, 0, 0); //! Default nametag color
    private static final int FG_ALPHA_FOCUSED = 255;
    private static final int FG_ALPHA_UNFOCUSED = 0;




    private transient ServerWorld world;
    private String worldId;
    private transient CustomItemDisplay itemDisplay = null; //! Searched when needed instead of on data loading. The chunk needs to be loaded.
    private UUID itemDisplayUUID;
    public UUID ownerUUID;
    private BlockPos pos;
    private transient String shopIdentifierCache;

    private transient ItemStack item = Items.BARRIER.getDefaultStack();
    private String serializedItem;
    private double price = 0;
    private int stock = 0;

    private transient Boolean focusedState = false;
    public transient Boolean focusedStateNext = false;
    private transient List<CustomTextDisplay> focusDisplays = new ArrayList<>();


    private void calcSerializedItem() {
        serializedItem = ItemStack.CODEC.encode(item, JsonOps.INSTANCE, JsonOps.INSTANCE.empty()).result().get().toString();
    }
    private void calcDeserializedItem() {
        item = ItemStack.CODEC.decode(JsonOps.INSTANCE, JsonParser.parseString(serializedItem)).result().get().getFirst();
    }


    private void calcSerializedWorldId() {
        worldId = world.getRegistryKey().getValue().toString();
    }
    /**
     * Tries to deserialize the world Identifier and find the ServerWorld it belongs to.
     * @param server The server instance.
     * @throws RuntimeException if the world Identifier is invalid or the ServerWorld cannot be found.
     */
    private void calcDeserializedWorldId(MinecraftServer server) throws RuntimeException {
        for (ServerWorld w : server.getWorlds()) {
            if(w.getRegistryKey().getValue().toString().equals(worldId)) {
                world = w;
                return;
            }
        }
        throw new RuntimeException("Invalid shop data: Specified world \"" + worldId + "\" was not found");
    }




    /**
     * Creates a new Shop and saves it.
     * @param world The world the shop had to be created in.
     * @param _pos The position of the new shop.
     * @param owner The player that owns the shop.
     */
    public Shop(ServerWorld _world, BlockPos _pos, PlayerEntity owner){
        world = _world;
        ownerUUID = owner.getUuid();
        pos = _pos;
        calcSerializedItem();
        calcSerializedWorldId();
        cacheShopIdentifier();


        // Create and setup the Item Display entity
        itemDisplay = new CustomItemDisplay(
            world,
            item,
            new Vec3d(pos.getX() + 0.5, pos.getY() + 0.3, pos.getZ() + 0.5),
            true,
            false,
            null
        );
        itemDisplay.getRawDisplay().setCustomName(Text.of("[Empty shop]"));
        itemDisplayUUID = itemDisplay.getRawDisplay().getUuid();
        itemDisplay.spawn(world);


        // Save the shop
        saveShop();
    }




    /**
     * Sets the shop identifier
     * @return The shop identifier.
     */
    private void cacheShopIdentifier() {
        shopIdentifierCache = calcShopIdentifier(pos, worldId);
    }
    private static String calcShopIdentifier(BlockPos _pos, String worldId) {
        return String.format("%d,%d,%d,%s", _pos.getX(), _pos.getY(), _pos.getZ(), worldId);
    }




    /**
     * Saves the shop data of a specific player in the config file.
     * @param owner The player.
     */
    private void saveShop() {
        // Create map entry if absent, then add the new shop to the player's shops
        shopsByOwner.put(ownerUUID.toString(), this);
        shopsByCoords.put(shopIdentifierCache, this);


        // Create this shop's config file if absent, then save the JSON in it
        final File shopStorageFile = new File(SHOP_STORAGE_DIR.getPath() + "/" + shopIdentifierCache + ".json");
        try (Writer writer = new FileWriter(shopStorageFile)) {
            new Gson().toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    /**
     * Loads all the player shops into the runtime map.
     * Must be called on server started event (After the worlds are loaded!).
     */
    public static void loadData(MinecraftServer server) {
        for (File shopStorageFile : new File(SHOP_STORAGE_DIR.toString()).listFiles()) {


            // Read file
            Shop retrievedShop = null;
            try (Reader reader = new FileReader(shopStorageFile)) {
                retrievedShop = new Gson().fromJson(reader, Shop.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Recalculate transient members and update shop maps
            retrievedShop.cacheShopIdentifier();
            retrievedShop.calcDeserializedItem();
            try {
                retrievedShop.calcDeserializedWorldId(server);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
            retrievedShop.focusDisplays = new ArrayList<>();
            shopsByOwner.put(retrievedShop.ownerUUID.toString(), retrievedShop);
            shopsByCoords.put(retrievedShop.shopIdentifierCache, retrievedShop);
        }
    }




    /**
     * Returns the Shop instance preset at a certain block position.
     * Returns null if no shop is there.
    */
    public static Shop findShop(BlockPos pos, String worldId) {
        return shopsByCoords.get(calcShopIdentifier(pos, worldId));
    }




    /**
     * Spawns or removes the focus displays depending on the set next focus state.
     */
    public void updateFocusDisplay(){
        if(focusedState != focusedStateNext) {
            if(focusedStateNext) {

                // Create and setup the Text Display entity and turn off the CustomName
                CustomTextDisplay focusDisplay = new CustomTextDisplay(
                    world,
                    Text.empty()
                        .append(item.getItem() != Items.BARRIER ? Utils.getItemName(item) : Text.literal("[Empty shop]").setStyle(Style.EMPTY.withItalic(true)))
                        .append(Text.literal("\nPrice: ")).append(Text.literal(Utils.formatPrice (price)).setStyle(Style.EMPTY.withColor(Formatting.GOLD).withBold(true)))
                        .append(Text.literal("\nStock: ")).append(Text.literal(Utils.formatAmount(stock)).setStyle(Style.EMPTY.withColor(Formatting.DARK_AQUA).withBold(true)))
                    ,
                    new Vec3d(pos.getX() + 0.5, pos.getY() + 0.6, pos.getZ() + 0.5),
                    BillboardMode.VERTICAL,
                    false,
                    null
                );
                activeFocusDisplays.add(focusDisplay.getRawDisplay().getUuid()); //! Must be added before spawning the entity into the world to stop it from intantly getting purged
                focusDisplays.add(focusDisplay);
                focusDisplay.setTextOpacity(FG_ALPHA_UNFOCUSED);
                focusDisplay.spawn(world);
                focusDisplay.animateBackground(BG_FOCUSED, BG_TRANSITION_TIME_IN, 1);
                focusDisplay.animateTextOpacity(FG_ALPHA_FOCUSED, BG_TRANSITION_TIME_IN, 1);

                findDisplayEntityIfNeeded();
                if(itemDisplay != null) itemDisplay.getRawDisplay().setCustomNameVisible(false);
            }
            else {

                // Remove text display entities, stop and reset item rotation and turn the CustomName back on
                for (CustomTextDisplay e : focusDisplays) {
                    e.animateBackground(BG_UNFOCUSED, BG_TRANSITION_TIME_OUT, 1);
                    e.animateTextOpacity(FG_ALPHA_UNFOCUSED, BG_TRANSITION_TIME_OUT, 1);
                }
                List<CustomTextDisplay> focusDisplaysTmp = focusDisplays;
                focusDisplays = new ArrayList<>();
                Scheduler.schedule(BG_TRANSITION_TIME_OUT, () -> {
                    for (CustomTextDisplay e : focusDisplaysTmp) {
                        e.despawn();
                        activeFocusDisplays.remove(e.getRawDisplay().getUuid());
                    }
                    findDisplayEntityIfNeeded();
                    if(itemDisplay != null) itemDisplay.getRawDisplay().setCustomNameVisible(true);
                });
            }
            focusedState = focusedStateNext;
        }
    }




    /**
     * Finds the display entity connected to this shop, only if this.itemDisplay is null.
     * @param world The world search the the entity in.
     */
    private void findDisplayEntityIfNeeded(){
        if(itemDisplay == null) {
            ItemDisplayEntity rawItemDisplay = (ItemDisplayEntity)(world.getEntity(itemDisplayUUID));
            if(rawItemDisplay != null) {
                itemDisplay = new CustomItemDisplay(rawItemDisplay, null);
            }
        }
    }




    /**
     * Checks for stray focus displays and purges them.
     * Any TextDisplayEntity not registered as active display and in the same block as a shop is considered a stray display.
     * Must be called on entity load event.
     * @param entity
     */
    public static void onEntityLoad(Entity entity) {
        if (entity instanceof TextDisplayEntity) {
            World world = entity.getWorld();
            if(world != null && !activeFocusDisplays.contains(entity.getUuid()) && findShop(entity.getBlockPos(), world.getRegistryKey().getValue().toString()) != null) {
                entity.remove(RemovalReason.KILLED);
            }
        }
    }




    /**
     * Checks if the held item is a shop item. If it is, it spawns a new shop.
     */
    public static ActionResult onItemUse(World world, PlayerEntity player, Hand hand, BlockHitResult hitResult){
        ItemStack stack = player.getStackInHand(hand);
        if (stack.getItem() == FancyPlayerShops.SHOP_ITEM_ID && stack.hasNbt() && stack.getNbt().contains(FancyPlayerShops.SHOP_ITEM_NBT_KEY)) {
            if(world instanceof ServerWorld) {
                BlockPos blockPos = hitResult.getBlockPos().add(hitResult.getSide().getVector());
                new Shop((ServerWorld)world, blockPos, player);
                player.sendMessage(Text.of("New shop created! Right click it to configure."));
            }
            else {
                player.sendMessage(Text.of("You cannot create a shop here!"));
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }
}
