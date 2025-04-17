package com.snek.fancyplayershops.implementations.ui.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector4i;

import com.snek.framework.ui.styles.PanelElmStyle;








/**
 * The style of the generic ShopPanelElm UI element.
 */
public class ShopPanelElmStyle extends PanelElmStyle {

    /**
     * Creates a new ShopButtonStyle.
     */
    public ShopPanelElmStyle() {
        super();
    }


    @Override
    public @NotNull Vector4i getDefaultColor() {
        return new Vector4i(130, 40, 50, 50);
    }
}
