package com.snek.fancyplayershops.implementations.ui;

import com.snek.framework.ui.elements.FancyTextElm;
import com.snek.framework.ui.interfaces.Clickable;
import com.snek.framework.ui.interfaces.Hoverable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector3d;

import com.snek.fancyplayershops.Shop;
import com.snek.fancyplayershops.implementations.ui.styles.ShopButtonStyle;
import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.utils.Easings;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;








/**
 * A generic button class with clicking and hovering capabilities.
 */
public abstract class ShopButton extends FancyTextElm implements Hoverable, Clickable {
    public static final float UNHOVERED_W = 0.05f;
    protected final @NotNull Shop shop;

    // Animations //FIXME MOVE TO STYLE
    private final Animation hoverEnterAnimation;
    private final Animation hoverExitAnimation;




    /**
     * Creates a new ShopButton.
     * @param _shop The target shop.
     * @param w The width of the button, expressed in blocks.
     * @param h The height of the button, expressed in blocks.
     */
    protected ShopButton(@NotNull Shop _shop, float w, float h) {
        super(_shop.getWorld(), new ShopButtonStyle());
        shop = _shop;
        setSize(new Vector2f(w, h));


        // Initialize animations
        hoverEnterAnimation = new Animation(
            new Transition(6, Easings.cubicOut)
            .additiveTransformBg(new Transform().scaleX(1f / UNHOVERED_W))
        );
        hoverExitAnimation = new Animation(
            new Transition(6, Easings.cubicOut)
            .additiveTransformBg(new Transform().scaleX(UNHOVERED_W))
        );
    }




    @Override
    public void spawn(Vector3d pos){
        applyAnimation(hoverExitAnimation);
        super.spawn(pos);
    }


    @Override
    public void onHoverEnter(PlayerEntity player) {
        if(player != shop.user) return;
        applyAnimation(hoverEnterAnimation);
    }


    @Override
    public void onHoverExit(PlayerEntity player) {
        if(player != shop.user) return;
        applyAnimation(hoverExitAnimation);
    }


    @Override
    public boolean onClick(@NotNull PlayerEntity player, @NotNull ClickType click) {
        return checkIntersection(player);
    }


    /**
     * Updates the displayed text, reading data from the target shop.
     * @param textOverride If not null, it replaces the shop's data.
     */
    public abstract void updateDisplay(@Nullable Text textOverride);
}
