package com.snek.fancyplayershops;

import java.io.Console;
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

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.snek.fancyplayershops.CustomDisplays.CustomItemDisplay;
import com.snek.fancyplayershops.CustomDisplays.CustomTextDisplay;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.decoration.DisplayEntity.BillboardMode;
import net.minecraft.entity.decoration.DisplayEntity.TextDisplayEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;








// TODO fix broken shops and blocks if they don't exist in the world when the map is loaded
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

    // Stores the shops of players, identifying them by their owner's UUID and their coordinates in the format "x,y,z"
    private static final Map<String, Shop> shopsByCoords = new HashMap<>();
    private static final Map<String, Shop> shopsByOwner = new HashMap<>();




    // private transient CustomItemDisplay itemDisplay;
    private UUID itemDisplayUUID;
    public UUID ownerUUID;
    private BlockPos pos;
    private transient String shopIdentifierCache;

    private transient ItemStack item = Items.AIR.getDefaultStack();
    private String serializedItem;
    private double price = 0;
    private int stock = 0;

    private transient Boolean focusedState = false;
    public transient Boolean focusedStateNext = false;
    private transient List<TextDisplayEntity> focusDisplays = new ArrayList<>();


    private void calcSerializedItem() {
        serializedItem = ItemStack.CODEC.encode(item, JsonOps.INSTANCE, JsonOps.INSTANCE.empty()).result().get().toString();
    }
    private void calcDeserializedItem() {
        item = ItemStack.CODEC.decode(JsonOps.INSTANCE, JsonParser.parseString(serializedItem)).result().get().getFirst();
    }




    /**
     * Creates a new Shop and saves it.
     * @param world The world the shop had to be created in.
     * @param _pos The position of the new shop.
     * @param owner The player that owns the shop.
     */
    public Shop(World world, BlockPos _pos, PlayerEntity owner){
        ownerUUID = owner.getUuid();
        pos = _pos;
        cacheShopIdentifier();
        calcSerializedItem();


        // Create and setup the Item Display entity
        CustomItemDisplay itemDisplay = new CustomItemDisplay(
            world,
            new ItemStack(Items.BARRIER, 1),
            new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5),
            true,
            false
        );
        itemDisplay.getRawDisplay().setCustomName(Text.of("Empty shop"));
        itemDisplayUUID = itemDisplay.getRawDisplay().getUuid();


        // Save the shop
        saveShop();
    }




    /**
     * Sets the shop identifier
     * @return The shop identifier.
     */
    private void cacheShopIdentifier() {
        shopIdentifierCache = calcShopIdentifier(pos);
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
    public static void load() {
        for (File shopStorageFile : new File(SHOP_STORAGE_DIR.toString()).listFiles()) {
            try (Reader reader = new FileReader(shopStorageFile)) {
                Shop retrievedShop = new Gson().fromJson(reader, Shop.class);
                retrievedShop.cacheShopIdentifier();
                retrievedShop.calcDeserializedItem();
                shopsByOwner.put(retrievedShop.ownerUUID.toString(), retrievedShop);
                shopsByCoords.put(retrievedShop.shopIdentifierCache, retrievedShop);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




    /**
     * Returns the Shop instance preset at a certain block position.
     * Returns null if no shop is there.
    */
    public static Shop findShop(BlockPos pos) {
        return shopsByCoords.get(calcShopIdentifier(pos));
    }




    /**
     * Spawns or removes the focus displays depending on the set next focus state.
     */
    public void updateFocusDisplay(World world){
        if(focusedState != focusedStateNext) {
            if(focusedStateNext) {

                // Create and setup the Text Display entity and turn off the CustomName
                CustomTextDisplay textDisplay = new CustomTextDisplay(
                    world,
                    Text.empty()
                        .append(Utils.getItemName(item))
                        .append(Text.literal("\nPrice: ")).append(Text.literal("$" + String.valueOf(price)).setStyle(Style.EMPTY.withColor(Formatting.GOLD).withBold(true)))
                        .append(Text.literal("\nIn stock: ").append(Text.literal(String.valueOf(stock)).setStyle(Style.EMPTY.withColor(Formatting.DARK_AQUA))))
                    ,
                    new Vec3d(pos.getX(), pos.getY() + 0.4, pos.getZ()),
                    BillboardMode.CENTER,
                    false
                );
            }
            else {

                // Remove text display entities, stop and reset item rotation and turn the CustomName back on
                for (TextDisplayEntity e : focusDisplays) {
                    e.remove(RemovalReason.KILLED);
                }
                focusDisplays.clear();
            }
            focusedState = focusedStateNext;
        }
    }
}
