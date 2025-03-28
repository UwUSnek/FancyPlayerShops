package com.snek.fancyplayershops;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snek.fancyplayershops.ShopComponentEntities.DetailsDisplay;
import com.snek.framework.ui.Elm;
import com.snek.framework.utils.Scheduler;
import com.snek.framework.utils.Txt;








public class FancyPlayerShops implements ModInitializer {
    public static final String MOD_ID = "fancyplayershops";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final Item SHOP_ITEM_ID = Items.REDSTONE;
    public static final ItemStack shopItem = new ItemStack(SHOP_ITEM_ID);
    public static final String SHOP_ITEM_NBT_KEY = MOD_ID + ".item.shop_item";
    static {
        shopItem.setCustomName(new Txt("Shop").get());
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

            // Schedule focus features look
            Scheduler.loop(0, 2, () -> { FocusFeatures.tick(server.getWorlds()); });

            // Log initialization success
            LOGGER.info("FancyPlayerShops initialized. :3");
        });


        // Create and register shop block rclick event
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            ActionResult r;
            // r = ClickFeatures.onClick(world, player, hand, hitResult, ClickType.RIGHT); //FIXME add ghost cooldown. add to lclick too
            /*if(r == ActionResult.PASS)*/ r = onItemUse(world, player, hand, hitResult);
            return r;
        });


        // Create and register shop block rclick event
        AttackBlockCallback.EVENT.register((player, world, hand, blockPos, diretion) -> {
            ActionResult r;
            // r = ClickFeatures.onClick(world, player, hand, blockPos, ClickType.LEFT); //FIXME add ghost cooldown. add to lclick too
            // return r;
            return ActionResult.PASS;
        });


        // Register scheduler
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            Scheduler.tick(server);
            Scheduler.loop(0, Elm.TRANSITION_REFRESH_TIME, Elm::processUpdateQueueTick);
        });


        // Register focus display purge
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            DetailsDisplay.onEntityLoad(entity);
        });
    }




    /**
     * Checks if the held item is a shop item. If it is, it spawns a new shop.
     */
    public static ActionResult onItemUse(World world, PlayerEntity player, Hand hand, BlockHitResult hitResult){
        ItemStack stack = player.getStackInHand(hand);
        if (stack.getItem() == SHOP_ITEM_ID && stack.hasNbt() && stack.getNbt().contains(SHOP_ITEM_NBT_KEY)) {
            if(world instanceof ServerWorld) {
                stack.setCount(stack.getCount() - 1);
                BlockPos blockPos = hitResult.getBlockPos().add(hitResult.getSide().getVector());
                new Shop((ServerWorld)world, blockPos, player);
                player.sendMessage(new Txt("New shop created! Right click it to configure.").green().get());
            }
            else {
                player.sendMessage(new Txt("You cannot create a shop here!").darkRed().get());
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }
}