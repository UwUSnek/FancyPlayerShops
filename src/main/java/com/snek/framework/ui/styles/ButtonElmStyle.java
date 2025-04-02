package com.snek.framework.ui.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;




public class ButtonElmStyle extends TextElmStyle {
    private @NotNull Vector3i hoverColor;


    /**
     * Creates a new default ButtonElmStyle
     */
    public ButtonElmStyle() {
        hoverColor = new Vector3i(255, 255, 255);
    }


    public ButtonElmStyle setHoverColor(Vector3i _hoverColor) { hoverColor.set(_hoverColor); return this; }
}
