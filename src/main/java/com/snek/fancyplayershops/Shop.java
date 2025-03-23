package com.snek.fancyplayershops;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import com.snek.fancyplayershops.ShopComponentEntities.FocusDisplay;
import com.snek.fancyplayershops.ShopComponentEntities.ShopItemDisplay;
import com.snek.fancyplayershops.utils.Scheduler;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.decoration.DisplayEntity.ItemDisplayEntity;
import net.minecraft.entity.decoration.DisplayEntity.TextDisplayEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;








// TODO fix broken shops and blocks if they don't exist in the world when the map is loaded
// TODO purge stray focus displays on entity loading (use custom entity data)
public class Shop {
    private static final Path SHOP_STORAGE_DIR;
    static {
        SHOP_STORAGE_DIR = FabricLoader.getInstance().getConfigDir().resolve(FancyPlayerShops.MOD_ID + "/shops");
        try {
            Files.createDirectories(SHOP_STORAGE_DIR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Stores the shops of players, identifying them by their owner's UUID and their coordinates and world in the format "x,y,z,worldId"
    private static final Map<String, Shop> shopsByCoords = new HashMap<>();
    private static final Map<String, Shop> shopsByOwner  = new HashMap<>();




    private transient ServerWorld world;
    private String worldId;
    private transient ShopItemDisplay itemDisplay = null; //! Searched when needed instead of on data loading. The chunk needs to be loaded.
    private UUID itemDisplayUUID;
    public UUID ownerUUID;
    private BlockPos pos;
    private transient String shopIdentifierCache;
    private transient String shopIdentifierCache_noWorld;

    private transient ItemStack item = Items.BARRIER.getDefaultStack();
    private String serializedItem;
    private double price = 0;
    private int stock = 0;

    private transient Boolean focusedState = false;
    public transient Boolean focusedStateNext = false;
    private transient FocusDisplay focusDisplay = null;




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


        // Create and spawn the Item Display entity
        itemDisplay = new ShopItemDisplay(world, pos, item);
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
        shopIdentifierCache         = calcShopIdentifier(pos, worldId);
        shopIdentifierCache_noWorld = calcShopIdentifier(pos);
    }
    private static String calcShopIdentifier(BlockPos _pos, String worldId) {
        return calcShopIdentifier(_pos) + "," + worldId;
    }
    private static String calcShopIdentifier(BlockPos _pos) {
        return String.format("%d,%d,%d", _pos.getX(), _pos.getY(), _pos.getZ());
    }




    /**
     * Saves the shop data of a specific player in the config file.
     * @param owner The player.
     */
    private void saveShop() {
        // Create map entry if absent, then add the new shop to the player's shops
        shopsByOwner.put(ownerUUID.toString(), this);
        shopsByCoords.put(shopIdentifierCache, this);


        // Create directory for the world
        final Path shopStorageDir = SHOP_STORAGE_DIR.resolve(worldId);
        try {
            Files.createDirectories(shopStorageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Create this shop's config file if absent, then save the JSON in it
        final File shopStorageFile = new File(shopStorageDir + "/" + shopIdentifierCache_noWorld + ".json");
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

        // For each world directory
        if(SHOP_STORAGE_DIR != null) for (File shopStorageDir : SHOP_STORAGE_DIR.toFile().listFiles()) {

            // For each shop file
            File[] shopStorageFiles = shopStorageDir.listFiles();
            if(shopStorageFiles != null) for (File shopStorageFile : shopStorageFiles) {

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
                shopsByOwner.put(retrievedShop.ownerUUID.toString(), retrievedShop);
                shopsByCoords.put(retrievedShop.shopIdentifierCache, retrievedShop);
            }
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
     * Spawns or removes the focus display and starts item animations depending on the set next focus state.
     */
    public void updateFocusStatus(){
        if(focusedState != focusedStateNext) {
            if(focusedStateNext) {

                // Create and setup the Text Display entity
                if(focusDisplay != null) focusDisplay.getRawDisplay().remove(RemovalReason.KILLED);
                focusDisplay = new FocusDisplay(world, pos, item, price, stock);
                focusDisplay.spawn(world);

                // Start item animation and turn off the CustomName
                findDisplayEntityIfNeeded();
                if(itemDisplay != null) itemDisplay.enterFocusState();
            }
            else {

                // Despawn the text display
                focusDisplay.despawn();

                // Start item animation and turn the CustomName back on
                findDisplayEntityIfNeeded();
                if(itemDisplay != null) itemDisplay.leaveFocusState();
            }
            focusedState = focusedStateNext;
        }
    }




    /**
     * Finds the display entity connected to this shop, only if this.itemDisplay is null.
     */
    private void findDisplayEntityIfNeeded(){
        if(itemDisplay == null) {
            ItemDisplayEntity rawItemDisplay = (ItemDisplayEntity)(world.getEntity(itemDisplayUUID));
            if(rawItemDisplay != null) {
                itemDisplay = new ShopItemDisplay(rawItemDisplay, null);
            }
        }
    }
}
