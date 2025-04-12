package com.snek.fancyplayershops.implementations.ui.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector4i;

import com.snek.framework.ui.styles.PanelElmStyle;
import com.snek.framework.ui.styles.TextElmStyle;




public class ShopButtonStyle extends PanelElmStyle {
    public ShopButtonStyle() {
        super();
    }


    @Override
    public @NotNull Vector4i getDefaultColor () {
        return new Vector4i(255, 110, 130, 140);
    }
}
