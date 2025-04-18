package com.snek.fancyplayershops.implementations.ui.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4i;

import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.utils.Easings;








/**
 * The style of the EditUiPriceButton UI element.
 */
public class EditUiRotateButtonStyle extends ShopButtonStyle {

    /**
     * Creates a new EditUiPriceButtonStyle.
     */
    public EditUiRotateButtonStyle(){
        super();
    }




    @Override
    public @NotNull Vector4i getDefaultBackground() {
        return new Vector4i(0, 0, 0, 0);
    }


    @Override
    public @Nullable Animation getDefaultHoverPrimerAnimation() {
        return null;
    }

    @Override
    public @Nullable Animation getDefaultHoverEnterAnimation() {
        return new Animation(
            new Transition(HOVER_ANIMATION_TIME, Easings.cubicOut)
            .targetBackground(HOVER_COLOR)
        );
    }

    @Override
    public @Nullable Animation getDefaultHoverLeaveAnimation() {
        return new Animation(
            new Transition(HOVER_ANIMATION_TIME, Easings.cubicOut)
            .targetBackground(new Vector4i(0, 0, 0, 0))
        );
    }
}
