package com.snek.fancyplayershops.implementations.ui.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4i;

import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.data_types.containers.Flagged;
import com.snek.framework.ui.styles.FancyTextElmStyle;
import com.snek.framework.utils.Easings;








/**
 * The style of the generic ShopButton UI element.
 */
public class ShopButtonStyle extends FancyTextElmStyle {
    public static final Vector4i HOVER_COLOR          = new Vector4i(255, 110, 160, 130);
    public static final float    UNHOVERED_W          = 0.05f;
    public static final int      HOVER_ANIMATION_TIME = 6;

    private @Nullable Flagged<Animation> hoverPrimerAnimation = null;
    private @Nullable Flagged<Animation> hoverEnterAnimation  = null;
    private @Nullable Flagged<Animation> hoverLeaveAnimation  = null;




    /**
     * Creates a new ShopButtonStyle.
     */
    public ShopButtonStyle() {
        super();
    }




    @Override
    public void resetAll(){
        super.resetAll();
        resetHoverPrimerAnimation();
        resetHoverEnterAnimation();
        resetHoverLeaveAnimation();
    }




    @Override
    public @NotNull Vector4i getDefaultBackground() {
        return new Vector4i(HOVER_COLOR);
    }




    // Default value providers
    public @Nullable Animation getDefaultHoverPrimerAnimation() {
        return new Animation(
            new Transition(HOVER_ANIMATION_TIME, Easings.cubicOut)
            .additiveTransformBg(new Transform().scaleX(UNHOVERED_W))
        );
    }
    public @Nullable Animation getDefaultHoverEnterAnimation () {
        return new Animation(
            new Transition(HOVER_ANIMATION_TIME, Easings.cubicOut)
            .additiveTransformBg(new Transform().scaleX(1f / UNHOVERED_W))
        );
    }
    public @Nullable Animation getDefaultHoverLeaveAnimation () {
        return new Animation(
            new Transition(HOVER_ANIMATION_TIME, Easings.cubicOut)
            .additiveTransformBg(new Transform().scaleX(UNHOVERED_W))
        );
    }


    // Reset functions
    public void resetHoverPrimerAnimation() { hoverPrimerAnimation = Flagged.from(getDefaultHoverPrimerAnimation()); }
    public void resetHoverEnterAnimation () { hoverEnterAnimation  = Flagged.from(getDefaultHoverEnterAnimation ()); }
    public void resetHoverLeaveAnimation () { hoverLeaveAnimation  = Flagged.from(getDefaultHoverLeaveAnimation ()); }


    // Setters
    public void setHoverPrimerAnimation(@Nullable Animation _hoverPrimerAnimation) { hoverPrimerAnimation.set(_hoverPrimerAnimation); }
    public void setHoverEnterAnimation (@Nullable Animation _hoverEnterAnimation ) { hoverEnterAnimation .set(_hoverEnterAnimation ); }
    public void setHoverLeaveAnimation (@Nullable Animation _hoverLeaveAnimation ) { hoverLeaveAnimation .set(_hoverLeaveAnimation ); }


    // Flagged getters
    public @Nullable Flagged<Animation> getFlaggedHoverPrimerAnimation() { return hoverPrimerAnimation;}
    public @Nullable Flagged<Animation> getFlaggedHoverEnterAnimation () { return hoverEnterAnimation ;}
    public @Nullable Flagged<Animation> getFlaggedHoverLeaveAnimation () { return hoverLeaveAnimation ;}


    // Getters
    public @Nullable Animation getHoverPrimerAnimation() { return hoverPrimerAnimation.get(); }
    public @Nullable Animation getHoverEnterAnimation () { return hoverEnterAnimation .get(); }
    public @Nullable Animation getHoverLeaveAnimation () { return hoverLeaveAnimation .get(); }


    // Edit getters
    public @Nullable Animation editHoverPrimerAnimation() { return hoverPrimerAnimation.edit(); }
    public @Nullable Animation editHoverEnterAnimation () { return hoverEnterAnimation .edit(); }
    public @Nullable Animation editHoverLeaveAnimation () { return hoverLeaveAnimation .edit(); }
}
