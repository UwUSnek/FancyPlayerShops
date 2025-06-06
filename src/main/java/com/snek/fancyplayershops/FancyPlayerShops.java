package com.snek.fancyplayershops;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
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

import com.snek.fancyplayershops.implementations.ui.misc.InteractionBlocker;
import com.snek.framework.ui.elements.Elm;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.scheduler.Scheduler;








/**
 * The main class of the mod FancyPlayerShops.
 */
public class FancyPlayerShops implements ModInitializer {

    // Mod ID and console logger
    public static final String MOD_ID = "fancyplayershops";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


    // Shop item data
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



        ServerLifecycleEvents.SERVER_STARTED.register(server -> {

            // Load shop data
            Shop.loadData(server);

            // Schedule UI element update loop
            Scheduler.loop(0, Elm.TRANSITION_REFRESH_TIME, Elm::processUpdateQueue);

            // Schedule focus features loop
            Scheduler.loop(0, 2, () -> FocusFeatures.tick(server.getWorlds()));

            // Log initialization success
            LOGGER.info("FancyPlayerShops initialized. :3");
        });




        // Create and register shop block rclick event
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            ActionResult r;
            r = ClickFeatures.onClick(world, player, hand, ClickType.RIGHT);
            if(r == ActionResult.PASS) r = onItemUse(world, player, hand, hitResult);
            return r;
        });
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            return ClickFeatures.onClickEntity(world, player, hand, ClickType.RIGHT, entity);
        });




        // Create and register shop block lclick event
        AttackBlockCallback.EVENT.register((player, world, hand, blockPos, diretion) -> {
            ActionResult r;
            r = ClickFeatures.onClick(world, player, hand, ClickType.LEFT);
            return r;
        });
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            return ClickFeatures.onClickEntity(world, player, hand, ClickType.LEFT, entity);
        });




        // Register scheduler
        ServerTickEvents.END_SERVER_TICK.register(server -> { Scheduler.tick(); });


        // Register focus display purge
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            Elm.onEntityLoad(entity);
            InteractionBlocker.onEntityLoad(entity);
        });




        // Register chat input handler
        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register((message, sender, params) -> {
            return !ChatInput.onMessage(message, sender);
        });
    }








    /**
     * Callback for item use events.
     * Checks if the held item is a shop item. If it is, it spawns a new shop.
     */
    public static ActionResult onItemUse(World world, PlayerEntity player, Hand hand, BlockHitResult hitResult){
        ItemStack stack = player.getStackInHand(hand);
        if (stack.getItem() == SHOP_ITEM_ID && stack.hasNbt() && stack.getNbt().contains(SHOP_ITEM_NBT_KEY)) {

            // If the world is a server world and the player is allowed to modify the world
            if(world instanceof ServerWorld serverWorld && player.getAbilities().allowModifyWorld) {

                // Remove item if the player is not in creative mode
                if(!player.getAbilities().creativeMode) stack.setCount(stack.getCount() - 1);

                // Calculate block position and create the new shop. Send a feedback message to the player
                BlockPos blockPos = hitResult.getBlockPos().add(hitResult.getSide().getVector());
                new Shop(serverWorld, blockPos, player);
                player.sendMessage(new Txt("New shop created! Right click it to configure.").green().get(), true);
            }

            // If not, send an error message to the player
            else player.sendMessage(new Txt("You cannot create a shop here!").darkRed().get(), true);
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }
}