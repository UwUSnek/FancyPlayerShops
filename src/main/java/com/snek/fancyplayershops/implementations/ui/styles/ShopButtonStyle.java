package com.snek.fancyplayershops.implementations.ui.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector4i;

import com.snek.framework.ui.styles.FancyTextElmStyle;








/**
 * The style of the generic ShopButton UI element.
 */
public class ShopButtonStyle extends FancyTextElmStyle {

    /**
     * Creates a new ShopButtonStyle.
     */
    public ShopButtonStyle() {
        super();
    }


    @Override
    public @NotNull Vector4i getDefaultBackground() {
        return new Vector4i(255, 110, 160, 130);
    }
}
