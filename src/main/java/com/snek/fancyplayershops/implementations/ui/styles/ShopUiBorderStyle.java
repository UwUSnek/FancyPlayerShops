package com.snek.fancyplayershops.implementations.ui.styles;

import org.joml.Vector4i;








/**
 * The style of the ShopUiBorder UI element.
 */
public class ShopUiBorderStyle extends ShopPanelElmStyle {
    public static final Vector4i COLOR = new Vector4i(255, 38, 38, 40);


    /**
     * Creates a new ShopUiBorderStyle
     */
    public ShopUiBorderStyle(){
        super();
    }


    @Override
    public Vector4i getDefaultColor(){
        return new Vector4i(COLOR);
    }
}
