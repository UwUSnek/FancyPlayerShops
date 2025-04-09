package com.snek.fancyplayershops.implementations.ui;

import com.snek.framework.ui.interfaces.Clickable;
import com.snek.framework.ui.interfaces.Hoverable;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector4i;

import com.snek.fancyplayershops.Shop;
import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.transitions.TextAdditiveTransition;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.utils.Easings;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ClickType;

import com.snek.framework.ui.styles.TextElmStyle;








public class ShopButton extends TrackedTextElm implements Hoverable, Clickable {
    public static final Vector4i BG_HOVER = new Vector4i(240, 95, 100, 100);

    protected final @NotNull Shop shop;




    public ShopButton(@NotNull Shop _shop) {
        super(_shop.getWorld());
        transform.edit().moveY(ShopTextElm.SHIFT_Y);
        flushStyle();
        shop = _shop;
    }


    @Override
    public void onHoverEnter() {
        applyAnimation(new Animation(new TextAdditiveTransition(new Transform(), 2, Easings.linear, BG_HOVER, 255)));
    }


    @Override
    public void onHoverExit() {
        applyAnimation(new Animation(new TextAdditiveTransition(new Transform(), 2, Easings.linear, ((TextElmStyle)style).getBackground(), 255)));
    }


    @Override
    public boolean onClick(@NotNull PlayerEntity player, @NotNull ClickType click) {
        return checkIntersection(player);
    }
}
