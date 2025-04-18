package com.snek.framework.ui.interfaces;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.player.PlayerEntity;








/**
 * An interface that provides a hover callback method.
 */
public interface Hoverable {

    /**
     * Processes a hover enter event.
     * @param player The player that triggered the event.
     */
    public void onHoverEnter(@NotNull PlayerEntity player);


    /**
     * Processes a hover exit event.
     * @param player The player that triggered the event.
     */
    public void onHoverExit(@Nullable PlayerEntity player);
}
