package com.snek.fancyplayershops;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.snek.fancyplayershops.inventories.ShopConfigUI_Factory;
import com.snek.framework.utils.Txt;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;








/**
 * A utility class that registers and handles in-game commands.
 */
public abstract class ShopCommand {
    private ShopCommand(){}


    /**
     * Registers the /shop command
     */
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(LiteralArgumentBuilder.<ServerCommandSource>literal("shop")


                .then(LiteralArgumentBuilder.<ServerCommandSource>literal("config_test").executes(context -> {
                    PlayerEntity player = context.getSource().getPlayer();
                    try {
                        player.sendMessage(new Txt("Opening shop inventory...").get(), false);
                        player.openHandledScreen(new ShopConfigUI_Factory());
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                    }
                    return 1;
                }))


                //TODO replace with crafting recipe
                .then(LiteralArgumentBuilder.<ServerCommandSource>literal("create").executes(context -> {
                    PlayerEntity player = context.getSource().getPlayer();
                    player.dropStack(FancyPlayerShops.shopItem.copy());
                    return 1;
                }))


                .then(LiteralArgumentBuilder.<ServerCommandSource>literal("claim").executes(context -> {
                    PlayerEntity player = context.getSource().getPlayer();
                    player.sendMessage(new Txt("You claimed your shop balance.").get(), false);
                    return 1;
                }))


                .then(LiteralArgumentBuilder.<ServerCommandSource>literal("stats").executes(context -> {
                    PlayerEntity player = context.getSource().getPlayer();
                    player.sendMessage(new Txt("opened stats //todo remove message").get(), false);
                    return 1;
                }))


                .then(LiteralArgumentBuilder.<ServerCommandSource>literal("help").executes(context -> {
                    PlayerEntity player = context.getSource().getPlayer();
                    //TODO add colors and styles
                    player.sendMessage(new Txt(
                        """
                        Craft shop blocks using glass panes, a sign and redstone.
                        You can rotate shops using a wrench or pick them up by shift-rclicking them with it.
                        Right click a shop to configure and restock it. Each shop can contain up to a set amount of the same item.
                        You can see details about your shops and sales history using the command /shop stats.
                        """
                    ).get(), false);
                    return 1;
                }))
            );
        });
    }
}
