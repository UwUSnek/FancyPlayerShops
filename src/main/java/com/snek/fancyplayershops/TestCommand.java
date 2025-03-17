package com.snek.fancyplayershops;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.snek.fancyplayershops.CustomDisplays.CustomTextDisplay;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;



public class TestCommand {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(
                LiteralArgumentBuilder.<ServerCommandSource>literal("testcommand").executes(context -> {
                    ServerCommandSource source = context.getSource();
                    ServerWorld world = source.getWorld();
                    BlockPos pos = source.getPlayer().getBlockPos().add(0, 1, 0);


                    // Create and setup the Text Display entity
                    CustomTextDisplay display = new CustomTextDisplay(world);
                    display.getRawDisplay().setPosition(pos.getX(), pos.getY(), pos.getZ());
                    display.setText(Text.of("meow :3"));
                    display.getRawDisplay().setGlowing(true);
                    world.spawnEntity(display.getRawDisplay());


                    source.sendFeedback(() -> Text.of("New shop shelf created! Right click to configure."), false);
                    return 1;
                })
            );
        });
    }
}
