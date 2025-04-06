package com.snek.framework.ui.interfaces;

import org.jetbrains.annotations.NotNull;

import net.minecraft.entity.decoration.DisplayEntity.BillboardMode;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ClickType;








public interface Clickable {

    /**
     * Processes a click event using the player's view angle and current children.
     * Reach distance is not accounted for.
     * Calling this method on an element that hasn't been spawned yet is allowed and has no effect.
     *
     * NOTICE: Click detection is only available for elements with Fixed billboard mode.
     * Calling this function on elements with a different billboard mode is allowed and has no effect.
     *
     * @param player The player
     */
    public void onClick(@NotNull PlayerEntity player, @NotNull ClickType click);
}
