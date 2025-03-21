package com.snek.fancyplayershops;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snek.fancyplayershops.utils.Scheduler;








public class FancyPlayerShops implements ModInitializer {
    public static final String MOD_ID = "fancyplayershops";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final Item SHOP_ITEM_ID = Items.REDSTONE;
    public static final ItemStack shopItem = new ItemStack(SHOP_ITEM_ID);
    public static final String SHOP_ITEM_NBT_KEY = MOD_ID + ".item.shop_item";
    static {
        shopItem.setCustomName(Text.of("Shop shelf"));
        NbtCompound nbt = shopItem.getOrCreateNbt();
        nbt.putBoolean(SHOP_ITEM_NBT_KEY, true);
    }




    @Override
    public void onInitialize() {
        ShopCommand.register();




        // Create shop item recipe
        // Identifier recipeIdentifier = new Identifier(MOD_ID, "shop_item");
        // DefaultedList<Ingredient> recipeIngredients = DefaultedList.copyOf(Ingredient.EMPTY,
        //     Ingredient.ofItems(Items.GLASS_PANE), Ingredient.ofItems(Items.GLASS_PANE), Ingredient.ofItems(Items.GLASS_PANE),
        //     Ingredient.ofItems(Items.GLASS_PANE), Ingredient.fromTag(ItemTags.SIGNS),   Ingredient.ofItems(Items.GLASS_PANE),
        //     Ingredient.ofItems(Items.REDSTONE),   Ingredient.ofItems(Items.REDSTONE),   Ingredient.ofItems(Items.REDSTONE)
        // );
        // ShapedRecipe shopItemRecipe = new ShapedRecipe(
        //     recipeIdentifier,
        //     "fancyplayershops",
        //     CraftingRecipeCategory.MISC,
        //     3, 3,
        //     recipeIngredients,
        //     shopItem
        // );
        // // Registry.register(Registries.RECIPE_SERIALIZER, recipeIdentifier, shopItemRecipe.getSerializer());

        // RecipeManager.setRecipes(List.of(shopItemRecipe));
        ServerLifecycleEvents.SERVER_STARTED.register((server) -> {

            // Load shop data
            Shop.loadData(server);

            // Log initialization success
            LOGGER.info("FancyPlayerShops initialized. :3");
        });


        // Create and register shop block rclick event
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            return Shop.onItemUse(world, player, hand, hitResult);
        });


        // Create and register focus features
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            Scheduler.tick(server);
            FocusFeatures.tick(server.getWorlds());
        });


        // Register focus display purge
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            Shop.onEntityLoad(entity);
        });
    }
}