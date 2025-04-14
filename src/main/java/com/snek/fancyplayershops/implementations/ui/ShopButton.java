package com.snek.fancyplayershops.implementations.ui;

import com.snek.framework.ui.elements.FancyTextElm;
import com.snek.framework.ui.interfaces.Clickable;
import com.snek.framework.ui.interfaces.Hoverable;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector4i;

import com.snek.fancyplayershops.Shop;
import com.snek.fancyplayershops.implementations.ui.styles.ShopButtonStyle;
import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.displays.CustomTextDisplay;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.utils.Easings;
import com.snek.framework.utils.scheduler.Scheduler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ClickType;

import com.snek.framework.ui.styles.PanelElmStyle;








public class ShopButton extends FancyTextElm implements Hoverable, Clickable {
    public static final Vector4i BG_HOVER = new Vector4i(240, 95, 100, 100);

    protected final @NotNull Shop shop;




    public ShopButton(@NotNull Shop _shop, float w, float h) {
        super(_shop.getWorld(), new ShopButtonStyle());
        shop = _shop;

        // // Change default color
        // style = ;

        // ((ShopButtonStyle)style).setColor(((ShopButtonStyle)style).getDefaultColor());
        setSize(new Vector2f(w, h));
    }


    @Override
    public void onHoverEnter() {
        applyAnimation(new Animation(
            new Transition(2, Easings.linear)
            .targetBackground(BG_HOVER)
            .targetOpacity(255)
        ));
    }


    @Override
    public void onHoverExit() {
        applyAnimation(new Animation(
            new Transition(2, Easings.linear)
            .targetBackground(((ShopButtonStyle)style).getDefaultBackground())
            .targetOpacity(255)
        ));
        // System.out.println("current color 2: " + ((CustomTextDisplay)entity).getBackground().toString()); //TODO REMOVE
    }


    @Override
    public boolean onClick(@NotNull PlayerEntity player, @NotNull ClickType click) {
        return checkIntersection(player);
    }
}
