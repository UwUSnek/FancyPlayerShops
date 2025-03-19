package com.snek.fancyplayershops;

import net.minecraft.server.world.ServerWorld;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class FancyPlayerShops implements ModInitializer {
    public static final String MOD_ID = "fancyplayershops";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Item shopItemId = Items.REDSTONE;
    public static final ItemStack shopItem = new ItemStack(shopItemId);
    public static final String shopItemNbtTagKey = MOD_ID + ".item.shop_item";
    static {
        shopItem.setCustomName(Text.of("Shop shelf"));
        NbtCompound nbt = shopItem.getOrCreateNbt();
        nbt.putBoolean(shopItemNbtTagKey, true);
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


        // // Create and register shop item rclick event
        // UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
        //     if(world. hitResult.getBlockPos())
        //     ItemStack stack = player.getStackInHand(hand);
        //     if (stack.getItem() == shopItemId && stack.getNbt().contains(shopItemNbtTagKey)) {
        //         BlockPos blockPos = hitResult.getBlockPos().add(hitResult.getSide().getVector());
        //         new Shop(world, blockPos, player);
        //         player.sendMessage(Text.of("New shop created! Right click it to configure."));
        //         return ActionResult.SUCCESS;
        //     }
        //     return ActionResult.PASS;
        // });

        // Create and register shop block rclick event
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            ItemStack stack = player.getStackInHand(hand);
            if (stack.getItem() == shopItemId && stack.getNbt().contains(shopItemNbtTagKey)) {
                BlockPos blockPos = hitResult.getBlockPos().add(hitResult.getSide().getVector());
                new Shop(world, blockPos, player);
                player.sendMessage(Text.of("New shop created! Right click it to configure."));
                return ActionResult.SUCCESS;
            }
            return ActionResult.PASS;
        });


        // Log initialization success
        LOGGER.info("FancyPlayerShops initialized. :3");
    }
}