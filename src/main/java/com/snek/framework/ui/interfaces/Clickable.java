package com.snek.framework.ui.interfaces;

import org.jetbrains.annotations.NotNull;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ClickType;








/**
 * An interface that provides a click callback method.
 */
public interface Clickable {

    /**
     * Processes a click event.
     * Calling this method on an element that hasn't been spawned yet is allowed and has no effect.
     *
     * NOTICE: Click detection is only available for elements with Fixed billboard mode.
     * Calling this function on elements with a different billboard mode is allowed and has no effect.
     *
     * @param player The player.
     * @param click The type of click.
     * @return Whether the function consumed the click.
     */
    public boolean onClick(@NotNull PlayerEntity player, @NotNull ClickType click);
}
