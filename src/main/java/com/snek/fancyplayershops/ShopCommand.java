package com.snek.fancyplayershops;

import net.minecraft.block.Blocks;
import net.minecraft.entity.decoration.DisplayEntity.BillboardMode;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.snek.fancyplayershops.CustomDisplays.CustomTextDisplay;
import com.snek.fancyplayershops.CustomDisplays.CustomItemDisplay;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;




public class ShopCommand {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(LiteralArgumentBuilder.<ServerCommandSource>literal("shop")
                .then(LiteralArgumentBuilder.<ServerCommandSource>literal("create").executes(context -> {
                    context.getSource().getEntity().dropStack(FancyPlayerShops.shopItem.copy());
                //     ServerCommandSource source = context.getSource();
                //     ServerWorld world = source.getWorld();
                //     BlockPos blockPos = source.getPlayer().getBlockPos();
                //     new Shop(world, blockPos);

                //     source.sendFeedback(() -> Text.of("New shop created! Right click to configure."), false);
                    return 1;
                }))




                .then(LiteralArgumentBuilder.<ServerCommandSource>literal("claim").executes(context -> {
                    ServerCommandSource source = context.getSource();
                    source.sendFeedback(() -> Text.of("You claimed your shop balance."), false);
                    return 1;
                }))




                .then(LiteralArgumentBuilder.<ServerCommandSource>literal("stats").executes(context -> {
                    ServerCommandSource source = context.getSource();
                    source.sendFeedback(() -> Text.of("opened stats //todo remove message"), false);
                    return 1;
                }))




                .then(LiteralArgumentBuilder.<ServerCommandSource>literal("help").executes(context -> {
                    ServerCommandSource source = context.getSource();
                    source.sendFeedback(() -> Text.of(
                        "Use /shop create to create a new shop!\n" +
                        "You can rotate existing shops using a wrench or pick them up by shift-rclicking them with it.\n" +
                        "Right click a shop to set the item and price and restock it. Each shop can contain up to 1000 of the same item.\n" +
                        "You can see details about your shops and sales history using the command /shop stats."
                    ), false);
                    return 1;
                }))
            );
        });
    }
}
