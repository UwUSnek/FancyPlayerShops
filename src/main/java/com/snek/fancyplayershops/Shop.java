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
import org.joml.Math;
import org.joml.Vector3d;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import com.snek.fancyplayershops.implementations.ui.InteractionBlocker;
import com.snek.fancyplayershops.implementations.ui.ShopCanvas;
import com.snek.fancyplayershops.implementations.ui.ShopItemDisplay;
import com.snek.fancyplayershops.implementations.ui.details.DetailsUi;
import com.snek.fancyplayershops.implementations.ui.edit.EditUi;
import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.utils.Easings;
import com.snek.framework.utils.MinecraftUtils;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.Utils;
import com.snek.framework.utils.scheduler.Scheduler;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.decoration.DisplayEntity.ItemDisplayEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
/**
 * A placed player shop.
 */
public class Shop {

    // Animation data
    public  static final int CANVAS_ANIMATION_DELAY = 5;


    // Strings
    public  static final Text EMPTY_SHOP_NAME = new Txt("[Empty]").italic().lightGray().get();
    private static final Text SHOP_EMPTY_TEXT = new Txt("This shop is empty!").lightGray().get();
    private static final Text SHOP_STOCK_TEXT = new Txt("This shop has no items in stock!").lightGray().get();


    // Storage files
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




    // Basic data
    private transient ServerWorld world;
    private String worldId;
    private transient ShopItemDisplay itemDisplay = null; //! Searched when needed instead of on data loading. The chunk needs to be loaded.
    private UUID itemDisplayUUID;
    private BlockPos pos;
    private transient String shopIdentifierCache;
    private transient String shopIdentifierCache_noWorld;
    public Vector3d calcDisplayPos() { return new Vector3d(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5); }


    // Shop data
    private transient ItemStack item = Items.AIR.getDefaultStack();
    private UUID   ownerUUID;
    private String serializedItem;
    private double price    = 0;
    private int    stock    = 0;
    private int    maxStock = 1000;
    private float  defaultRotation = 0f;


    // Shop status
    private transient @Nullable InteractionBlocker interactionBlocker = null;
    public  transient @Nullable ShopCanvas               activeCanvas = null;
    public  transient @Nullable PlayerEntity                     user = null;
    private transient           boolean                   focusStatus = false;
    private transient           boolean               focusStatusNext = false;
    private transient           long                    lastClickTick = 0; //! Used to limit click rate and prevent accidental double clicks

    public void setFocusStatusNext(boolean v) {
        focusStatusNext = v;
    }


    // Accessors
    public @NotNull ServerWorld     getWorld          () { return world;           }
    public @NotNull BlockPos        getPos            () { return pos;             }
    public @NotNull ItemStack       getItem           () { return item;            }
    public @NotNull ShopItemDisplay getItemDisplay    () { return findItemDisplayEntityIfNeeded(); }
    public          double          getPrice          () { return price;           }
    public          int             getStock          () { return stock;           }
    public          int             getMaxStock       () { return maxStock;        }
    public          float           getDefaultRotation() { return defaultRotation; }
    public          boolean         isFocused         () { return focusStatus;     }




    /**
     * Saves the item in its serialized form.
     */
    private void calcSerializedItem() {
        var result = ItemStack.CODEC.encode(item, JsonOps.INSTANCE, JsonOps.INSTANCE.empty()).result();
        if(result.isEmpty()) {
            throw new RuntimeException("Could not serialize shop item");
        }
        serializedItem = result.get().toString();
    }


    /**
     * Saves the item in its Item form, reading data from its serialized version.
     */
    private void calcDeserializedItem() {
        var result = ItemStack.CODEC.decode(JsonOps.INSTANCE, JsonParser.parseString(serializedItem)).result();
        if(result.isEmpty()) {
            throw new RuntimeException("Could not deserialize shop item");
        }
        item = result.get().getFirst();
    }


    /**
     * Retrieves the world ID from the ServerWorld value and saves it in worldId.
     */
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
     * Creates a new Shop and saves it in its own file.
     * @param world The world the shop has to be created in.
     * @param _pos The position of the new shop.
     * @param owner The player that places the shop.
     */
    public Shop(ServerWorld _world, BlockPos _pos, PlayerEntity owner){
        world = _world;
        ownerUUID = owner.getUuid();
        pos = _pos;

        // Get members from serialized data and calculate shop identifier
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
     * Sets the shop identifier.
     */
    private void cacheShopIdentifier() {
        shopIdentifierCache         = calcShopIdentifier(pos, worldId);
        shopIdentifierCache_noWorld = calcShopIdentifier(pos);
    }

    /**
     * Calculates a shop identifier from a position and the world ID.
     * @param _pos The position.
     * @param worldId The world ID.
     * @return The generated identifier.
     */
    private static String calcShopIdentifier(BlockPos _pos, String worldId) {
        return calcShopIdentifier(_pos) + "," + worldId;
    }

    /**
     * Calculates a shop identifier from the position. This identifier doesn't include the world ID.
     * @param _pos The position.
     * @return The generated identifier.
     */
    private static String calcShopIdentifier(BlockPos _pos) {
        return String.format("%d,%d,%d", _pos.getX(), _pos.getY(), _pos.getZ());
    }




    /**
     * Saves the shop data in its config file.
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
     * @param server The server instance.
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
                if(retrievedShop != null) {
                    retrievedShop.focusStatus     = false;
                    retrievedShop.focusStatusNext = false;
                    retrievedShop.cacheShopIdentifier();
                    try {
                        retrievedShop.calcDeserializedItem();
                        retrievedShop.calcDeserializedWorldId(server);
                        shopsByOwner.put(retrievedShop.ownerUUID.toString(), retrievedShop);
                        shopsByCoords.put(retrievedShop.shopIdentifierCache, retrievedShop);
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }




    /**
     * Returns the Shop instance present at a certain block position.
     * Returns null if no shop is there.
     * @param pos The block position.
     * @param worldId The ID of the world the shop is in.
     * @return The shop, or null.
    */
    public static Shop findShop(BlockPos pos, String worldId) {
        return shopsByCoords.get(calcShopIdentifier(pos, worldId));
    }

    /**
     * Returns the Shop instance present at a certain block position.
     * Returns null if no shop is there.
     * @param pos The block position.
     * @param world The world the shop is in.
     * @return The shop, or null.
    */
    public static Shop findShop(BlockPos pos, World world) {
        return findShop(pos, world.getRegistryKey().getValue().toString());
    }




    /**
     * Spawns or removes the focus displays and starts item animations depending on the set next focus state.
     */
    public void updateFocusState(){
        if(focusStatus != focusStatusNext) {
            focusStatus = focusStatusNext;
            if(focusStatus) {

                // Create details canvas
                if(activeCanvas != null) activeCanvas.despawnNow();
                activeCanvas = new DetailsUi(this);
                activeCanvas.menuAnimationInitial = null;
                activeCanvas.menuAnimationIn = null;
                activeCanvas.spawn(calcDisplayPos());

                // Create interaction blocker
                interactionBlocker = new InteractionBlocker(this);
                interactionBlocker.spawn(new Vector3d(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5));

                // Start item animation and turn off the CustomName
                findItemDisplayEntityIfNeeded().enterFocusState();
            }
            else {

                // Despawn active canvas
                activeCanvas.menuAnimationOut = null;
                activeCanvas.despawn();
                activeCanvas = null;

                // Despawn interaction blocker
                interactionBlocker.despawn();
                interactionBlocker = null;

                // Cancel chat input callbacks, then reset the user
                if(user != null) ChatInput.removeCallback(user);
                user = null;

                // Turn the item display's custom name back on
                findItemDisplayEntityIfNeeded().leaveFocusState();
            }
        }
    }




    /**
     * Finds the display entity connected to this shop and saves it to this.itemDisplay.
     * If no connected entity is found, a new ShopItemDisplay is created.
     * @reutrn the item display.
     */
    private @NotNull ShopItemDisplay findItemDisplayEntityIfNeeded(){
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
        if(lastClickTick < Scheduler.getTickNum()) lastClickTick = Scheduler.getTickNum();
        else return;


        // If the shop is not currently being used, flag the player as its user
        if(user == null) {
            if(clickType == ClickType.LEFT) {
                if(player.getUuid().equals(ownerUUID)) {
                    retrieveItem(player);
                }
                else {
                    buyItem(player, 1);
                }
            }
            else {
                user = player;
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
            activeCanvas.forwardClick(player, clickType);
        }


        // Send an error message to the player if someone else has already opened a menu in the same shop
        else {
            if(clickType == ClickType.RIGHT) {
                player.sendMessage(new Txt(
                    "Someone else is already using this shop! Left click to " +
                    (player.getUuid().equals(ownerUUID) ? "retrieve" : "buy") +
                    " one item."
                ).lightGray().get(), true);
            }
            else {
                if(player.getUuid().equals(ownerUUID)) {
                    retrieveItem(player);
                }
                else {
                    buyItem(player, 1);
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
            owner. sendMessage(SHOP_EMPTY_TEXT, true);
        }
        else if(stock < 1) {
            owner.sendMessage(new Txt("This shop has no items in stock!").lightGray().get(), true);
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
            player.sendMessage(SHOP_EMPTY_TEXT, true);
        }
        else if(stock < 1) {
            player.sendMessage(SHOP_STOCK_TEXT, true);
        }
        else if(stock < amount) {
            player.sendMessage(SHOP_STOCK_TEXT.copy().append(new Txt(" Items left: " + stock).lightGray().get()), true);
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
        //TODO no need for retrieval undo as the owner can simply put the item back in
    }




    /**
     * Switches the active canvas with a new one (after a delay to avoid overlapping them).
     * @param canvas The new canvas.
     */
    public void changeCanvas(ShopCanvas canvas) {
        if(activeCanvas != null) activeCanvas.despawn();
        activeCanvas = canvas;

        Scheduler.schedule(CANVAS_ANIMATION_DELAY, () -> {
            if(activeCanvas != null) activeCanvas.spawn(calcDisplayPos());
        });
    }




    /**
     * Opens the edit shop UI.
     * @param player The player.
     */
    public void openEditUi(PlayerEntity player) {
        changeCanvas(new EditUi(this));
        findItemDisplayEntityIfNeeded().enterEditState();
    }




    /**
     * Opens the buy item UI.
     * @param player The player.
     */
    public void openBuyUi(PlayerEntity player) {
        if(item.getItem() == Items.AIR) {
            player.sendMessage(SHOP_EMPTY_TEXT, true);
        }
        //TODO actually open the UI
    }




    private static final String objectiveName = "tmp_balance";

    //FIXME use economy mod API instead of scoreboards
    public void addMoney(PlayerEntity player, double amount) {
        ServerScoreboard s = player.getServer().getScoreboard();
        if(!s.containsObjective(objectiveName)) s.addObjective(objectiveName, ScoreboardCriterion.DUMMY, new Txt(objectiveName).get(), RenderType.INTEGER);
        ScoreboardPlayerScore score = s.getPlayerScore(player.getName().getString(), s.getObjective(objectiveName));
        score.incrementScore((int)amount);
    }




    /**
     * Tries to set a new price for the item and sends an error message to the user if it's invalid.
     *     Prices are automatically rounded to the nearest hundredth.
     *     Prices under 0.01 are rounded to 0.01
     *     Prices under 0.00001 are rounded to 0.
     *     Negative values are considered invalid and return false without changing the price.
     * @param newPrice The new price
     * @return Whether the new value could be set.
     */
    public boolean setPrice(float newPrice) {
        if(newPrice < 0) {
            if(user != null) user.sendMessage(new Txt("The price cannot be negative").red().get(), true);
            return false;
        }
        else if(newPrice < 0.00001) price = 0;
        else if(newPrice < 0.01000) price = 0.01;
        else price = Math.round(newPrice * 100f) / 100f;
        saveShop();
        return true;
    }




    /**
     * Tries to set a new stock limit for the item and sends an error message to the user if it's invalid.
     *     Amounts are rounded to the nearest integer.
     *     Negative values and 0 are considered invalid and return false without changing the stock limit.
     *     Values that are higher than the shop's storage capacity are also considered invalid. //TODO implement shop tiers
     * @param newStockLimit The new stock limit.
     * @return Whether the new value could be set.
     */
    public boolean setStockLimit(float newStockLimit) {
        if(newStockLimit < 0.9999) {
            if(user != null) user.sendMessage(new Txt("The stock limit must be at least 1").red().get(), true);
            return false;
        }
        else maxStock = Math.round(newStockLimit);
        saveShop();
        return true;
    }




    /**
     * Adds a specified rotation to the default rotation of the item display and saves the shop to its file.
     * @param _rotation The rotation to add.
     */
    public void addDefaultRotation(float _rotation) {

        // Add value to default rotation and save the shop
        defaultRotation += _rotation;
        saveShop();
    }
}