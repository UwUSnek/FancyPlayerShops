package com.snek.framework.ui;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.ui.interfaces.Clickable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ClickType;








public class Canvas {
    protected final List<Elm> children = new ArrayList<>();




    public Canvas() {}




    public void addChild(Elm elm) {
        children.add(elm);
    }
    public void removeChild(Elm elm) {
        children.remove(elm);
    }
    public void clearChildren() {
        children.clear();
    }




    /**
     * Applies an animation to all the elements of this canvas.
     * Use right rotations to rotate around the center of the canvas.
     * ! Partial steps at the end of the animation are expanded to cover the entire step.
     * @param animation The animation to apply.
     */
    public void applyAnimation(@NotNull Animation animation) {
        for (Elm elm : children) {
            elm.applyAnimation(animation);
        }
    }




    /**
     * Forwards a click to all the clickable elements of this canvas.
     * This method stops at the first element that consumes a click.
     * @param player The player that clicked.
     * @param clickType The type of click.
     */
    public void forwardClick(PlayerEntity player, ClickType clickType) {
        for (Elm elm : children) {
            if(elm instanceof Clickable c) {
                if(c.onClick(player, clickType)) return;
            }
        }
    }




    /**
     * Forwards a hover event to all the hoverable elements of this canvas.
     * @param player The player that clicked.
     */
    public void forwardHover(PlayerEntity player) {
        for (Elm elm : children) {
            elm.updateHoverStatus(player);
        }
    }




    public void spawn(Vector3d pos){
        for (Elm elm : children) {
            elm.spawn(pos);
        }
    }




    public void despawn(){
        for (Elm elm : children) {
            elm.despawn();
        }
    }




    public void despawnNow(){
        for (Elm elm : children) {
            elm.despawnNow();
        }
    }
}
