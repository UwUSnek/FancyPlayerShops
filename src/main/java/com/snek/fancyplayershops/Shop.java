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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import com.snek.fancyplayershops.implementations.ui.DetailsDisplay;
import com.snek.fancyplayershops.implementations.ui.ShopItemDisplay;
import com.snek.framework.ui.interfaces.Clickable;
import com.snek.framework.utils.MinecraftUtils;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.Utils;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.decoration.DisplayEntity.ItemDisplayEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.scoreboard.ScoreboardCriterion.RenderType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;








// TODO fix broken shops and blocks if they don't exist in the world when the map is loaded
public class Shop {
    private static final Path SHOP_STORAGE_DIR;
    public  static final Text EMPTY_SHOP_NAME = new Txt("[Empty]").italic().lightGray().get();
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




    // Basic data
    private transient ServerWorld world;
    private String worldId;
    private transient ShopItemDisplay itemDisplay = null; //! Searched when needed instead of on data loading. The chunk needs to be loaded.
    private UUID itemDisplayUUID;
    public  UUID ownerUUID;
    private BlockPos pos;
    private transient String shopIdentifierCache;
    private transient String shopIdentifierCache_noWorld;
    public Vector3d calcDisplayPos() { return new Vector3d(pos.getX() + 0.5, pos.getY() + 0.3, pos.getZ() + 0.5); }


    // Shop data
    private transient ItemStack item = Items.AIR.getDefaultStack();
    private String serializedItem;
    private double price = 0;
    private int stock = 0;


    // Shop status
    //FIXME make display private
    public transient @Nullable DetailsDisplay focusDisplay = null; //TODO this might need to be a list
    public  transient @Nullable PlayerEntity           user = null;
    public  transient           boolean         focusStatus = false;
    public  transient           boolean     focusStatusNext = false;


    // Accessors
    public @NotNull ServerWorld getWorld() { return world; }
    public @NotNull BlockPos    getPos  () { return pos;   }
    public @NotNull ItemStack   getItem () { return item;  }
    public          double      getPrice() { return price; }
    public          int         getStock() { return stock; }





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
        itemDisplay = new ShopItemDisplay(this);
        itemDisplayUUID = itemDisplay.getEntity().getUuid();
        itemDisplay.spawn(calcDisplayPos());

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
                retrievedShop.focusStatus     = false;
                retrievedShop.focusStatusNext = false;
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
     * Returns the Shop instance present at a certain block position.
     * Returns null if no shop is there.
    */
    public static Shop findShop(BlockPos pos, String worldId) {
        return shopsByCoords.get(calcShopIdentifier(pos, worldId));
    }
    public static Shop findShop(BlockPos pos, World world) {
        return findShop(pos, world.getRegistryKey().getValue().toString());
    }




    /**
     * Spawns or removes the focus displays and starts item animations depending on the set next menu status.
     */
    public void updateFocusState(){
        if(focusStatus != focusStatusNext) {
            if(focusStatusNext) {

                // Create and setup the Text Display entity
                if(focusDisplay != null) focusDisplay.getEntity().despawn();
                focusDisplay = new DetailsDisplay(this);
                focusDisplay.spawn(calcDisplayPos().add(0, 0.3d, 0));

                // Start item animation and turn off the CustomName
                findItemDisplayEntityIfNeeded().enterFocusState();
            }
            else {
                // Despawn the text display
                focusDisplay.despawn();

                // Start item animation and turn the CustomName back on
                findItemDisplayEntityIfNeeded().leaveFocusState();

                // Automatically close open menus and unset shop user
                user = null;
                //TODO close menus
            }
            focusStatus = focusStatusNext;
        }
    }




    /**
     * Finds the display entity connected to this shop and saves it to this.itemDisplay.
     * If this.itemDisplay is null, a new ShopItemDisplay is created.
     * @reutrn the item display.
     */
    private ShopItemDisplay findItemDisplayEntityIfNeeded(){
        if(itemDisplay == null) {
            ItemDisplayEntity rawItemDisplay = (ItemDisplayEntity)(world.getEntity(itemDisplayUUID));
            if(rawItemDisplay == null) {
                itemDisplay = new ShopItemDisplay(this);
            }
            else {
                itemDisplay = new ShopItemDisplay(this, rawItemDisplay);
            }
        }
        return itemDisplay;
    }




    /**
     * Handles a single click event on this shop block.
     * @param player The player that clicked the shop.
     * @param click The click type.
     */
    public void onClick(PlayerEntity player, ClickType clickType) {
        if(focusDisplay != null) {


            // If the shop is not currently being used, flag the player as its user
            if(user == null) {
                user = player;
                if(clickType == ClickType.LEFT) {
                    if(player.getUuid().equals(ownerUUID)) {
                        retrieveItem(player);
                    }
                    else {
                        buyItem(player, 1);
                    }
                }
                else {
                    if(player.getUuid().equals(ownerUUID)) {
                        openEditUi(player);
                    }
                    else {
                        openBuyUi(player);
                    }
                }
            }


            // If the player that clicked has already opened a menu, forward the click event to it
            else if(user == player) {
                //TODO forward click events to active menu
                // ((Clickable)focusDisplay).onClick(player, click);
                player.sendMessage(new Txt("DEBUG: CLICK EVENT FORWARDED").green().get());
            }


            // Send an error message to the player if someone else has already opened a menu in the same shop
            else {
                if(clickType == ClickType.RIGHT) {
                    player.sendMessage(new Txt(
                        "Someone else is already using this shop! Left click to " +
                        (player.getUuid().equals(ownerUUID) ? "retrieve" : "buy") +
                        " one item."
                    ).gray().get());
                }
                else {
                    //TODO retrieve/buy one
                }
            }
        }
    }




    /**
     * Retrieves one item from the shop at no cost and gives it to the owner.
     * Sends an error message to the player if the shop is unconfigured or doesn't contain any item.
     * @param owner The owner of the shop.
     */
    public void retrieveItem(PlayerEntity owner){
        if(item.getItem() == Items.AIR) {
            owner.sendMessage(new Txt("This shop is empty!").gray().get());
        }
        else if(stock < 1) {
            owner.sendMessage(new Txt("This shop has no items in stock!").gray().get());
        }
        else {
            --stock;
            ItemStack _item = item.copyWithCount(1);
            owner.giveItemStack(_item);
        }
    }




    /**
     * Makes a player buy a specified amount of items from the shop.
     * Sends an error message to the player if the shop is unconfigured or doesn't contain enough item.
     * @param player The player.
     * @param amount The amount of items to buy.
     */
    public void buyItem(PlayerEntity player, int amount){
        if(item.getItem() == Items.AIR) {
            player.sendMessage(new Txt("This shop is empty!").gray().get());
        }
        else if(stock < 1) {
            player.sendMessage(new Txt("This shop has no items in stock!").gray().get());
        }
        else if(stock < amount) {
            player.sendMessage(new Txt("This shop doesn't have enough items in stock! Items left: " + stock).gray().get());
        }
        else {
            stock -= amount;
            final double totPrice = price * amount;
            player.sendMessage(new Txt("Bought " + amount + "x " + MinecraftUtils.getItemName(item) + " for " + Utils.formatPrice(totPrice)).green().get());
            addMoney(player, -totPrice);
            ItemStack _item = item.copyWithCount(amount);
            player.giveItemStack(_item);
        }


        //TODO show undo button in chat after first left click of an shop to let players undo accidental purchases
        //TODO show undo button in chat after first left click of an shop to let players undo accidental purchases
        //TODO show undo button in chat after first left click of an shop to let players undo accidental purchases

        //TODO no need for retrieval undo as the owner can simply put the item back in
    }




    /**
     * Opens the edit shop UI.
     * @param player The player.
     */
    public void openEditUi(PlayerEntity player) {
        //TODO actually open the UI
    }




    /**
     * Opens the buy item UI.
     * @param player The player.
     */
    public void openBuyUi(PlayerEntity player) {
        if(item.getItem() == Items.AIR) {
            player.sendMessage(new Txt("This shop is empty!").gray().get());
        }
        //TODO actually open the UI
    }





    private final String objectiveName = "tmp_balance";

    //FIXME use economy mod API instead of scoreboards
    public void addMoney(PlayerEntity player, double amount) {
        ServerScoreboard s = player.getServer().getScoreboard();
        if(!s.containsObjective(objectiveName)) s.addObjective(objectiveName, ScoreboardCriterion.DUMMY, new Txt(objectiveName).get(), RenderType.INTEGER);
        ScoreboardPlayerScore score = s.getPlayerScore(player.getName().getString(), s.getObjective(objectiveName));
        score.incrementScore((int)amount);
    }
}