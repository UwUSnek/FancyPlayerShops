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
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import com.snek.fancyplayershops.CustomDisplays.CustomItemDisplay;
import com.snek.fancyplayershops.CustomDisplays.CustomTextDisplay;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.decoration.DisplayEntity.BillboardMode;
import net.minecraft.entity.decoration.DisplayEntity.ItemDisplayEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;








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
    private static final Map<String, Shop> shopsByOwner = new HashMap<>();




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
    private void calcDeserializedWorldId(MinecraftServer server) {
        for (ServerWorld w : server.getWorlds()) {
            if(w.getRegistryKey().getValue().toString().equals(worldId)) {
                world = w;
                return;
            }
        }
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
            false
        );
        itemDisplay.getRawDisplay().setCustomName(Text.of("[Empty shop]"));
        itemDisplayUUID = itemDisplay.getRawDisplay().getUuid();


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
     */
    public static void load(MinecraftServer server) {
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
            retrievedShop.calcDeserializedWorldId(server);
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
                    BillboardMode.CENTER,
                    false
                );
                focusDisplay.setInterpolationDuration(10);
                focusDisplay.setBackground(200, 0, 0, 0);
                focusDisplay.startInterpolation();
                focusDisplays.add(focusDisplay);

                findDisplayEntityIfNeeded();
                if(itemDisplay != null) itemDisplay.getRawDisplay().setCustomNameVisible(false);
            }
            else {

                // Remove text display entities, stop and reset item rotation and turn the CustomName back on
                for (CustomTextDisplay e : focusDisplays) {
                    e.setInterpolationDuration(10);
                    e.setBackground(64, 0, 0, 0); //! 64 is the default value displays spawns with
                    e.startInterpolation();
                }
                List<CustomTextDisplay> focusDisplaysTmp = focusDisplays;
                focusDisplays = new ArrayList<>();
                Utils.runDelayedSync(world.getServer(), 500, () -> {
                    for (CustomTextDisplay e : focusDisplaysTmp) {
                        e.getRawDisplay().remove(RemovalReason.KILLED);
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
        if(itemDisplay != null) {
            ItemDisplayEntity rawItemDisplay = (ItemDisplayEntity)world.getEntity(itemDisplayUUID);
            if(rawItemDisplay != null) {
                itemDisplay = new CustomItemDisplay(rawItemDisplay);
            }
        }
    }
}
