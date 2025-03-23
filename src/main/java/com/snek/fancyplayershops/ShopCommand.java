package com.snek.fancyplayershops;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.snek.fancyplayershops.Containers.ShopConfigUI;
import com.snek.fancyplayershops.Containers.ShopConfigUI_Factory;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;




public class ShopCommand {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(LiteralArgumentBuilder.<ServerCommandSource>literal("shop")

                .then(LiteralArgumentBuilder.<ServerCommandSource>literal("config_test").executes(context -> {
                    PlayerEntity player = context.getSource().getPlayer();
                    try {
                        player.sendMessage(Text.literal("Opening shop inventory..."), false);
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
                    player.sendMessage(Text.of("You claimed your shop balance."), false);
                    return 1;
                }))


                .then(LiteralArgumentBuilder.<ServerCommandSource>literal("stats").executes(context -> {
                    PlayerEntity player = context.getSource().getPlayer();
                    player.sendMessage(Text.of("opened stats //todo remove message"), false);
                    return 1;
                }))


                .then(LiteralArgumentBuilder.<ServerCommandSource>literal("help").executes(context -> {
                    PlayerEntity player = context.getSource().getPlayer();
                    //TODO add colors and styles
                    player.sendMessage(Text.of(
                        "Craft shop blocks using glass panes, a sign and redstone.\n" +
                        "You can rotate shops using a wrench or pick them up by shift-rclicking them with it.\n" +
                        "Right click a shop to configure and restock it. Each shop can contain up to a set amount of the same item.\n" +
                        "You can see details about your shops and sales history using the command /shop stats."
                    ), false);
                    return 1;
                }))
            );
        });
    }
}
