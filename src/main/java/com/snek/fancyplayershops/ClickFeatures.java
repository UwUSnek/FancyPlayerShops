package com.snek.fancyplayershops;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;








public class ClickFeatures {
    public ClickFeatures() { throw new UnsupportedOperationException("Utility class \"FocusFeatures\" cannot be instantiated"); }




    public static ActionResult onRclick(World world, PlayerEntity player, Hand hand, BlockHitResult hitResult) {
        Shop.findShop(null, world)
        return ActionResult.SUCCESS;
        return ActionResult.PASS;
    }
}
