package com.snek.fancyplayershops;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;








public abstract class ClickFeatures {
    private ClickFeatures(){}


    //FIXME detect item use events as well
    //FIXME check if it stacks with block use event, make them not stack
    /**
     * Handles left and right clicks on shop blocks.
     * Must be called on AttackBlockCallback and UseBlockCallback events.
     * @param world The world the player is in.
     * @param player The player.
     * @param hand The hand used.
     * @param clickType The type of click (LEFT click or RIGHT click)
     * @return SUCCESS if the player clicked a shop, PASS if not.
     */
    public static ActionResult onClick(World world, PlayerEntity player, Hand hand, ClickType clickType) {
        if(hand == Hand.MAIN_HAND && world instanceof ServerWorld serverWorld) {
            Shop targetShop = FocusFeatures.getLookedAtShop(player, serverWorld);
            if(targetShop != null) {
                targetShop.onClick(player, clickType);
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }
}
