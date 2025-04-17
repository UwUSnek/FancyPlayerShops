package com.snek.fancyplayershops.implementations.ui;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.Shop;
import com.snek.fancyplayershops.implementations.ui.styles.ShopPanelElmStyle;
import com.snek.framework.ui.elements.PanelElm;








/**
 * A generic PanelElm with a target shop.
 */
public class ShopPanelElm extends PanelElm {
    protected final @NotNull Shop shop;


    /**
     * Creates a new ShopPanelElm.
     * @param _shop The target shop.
     */
    protected ShopPanelElm(@NotNull Shop _shop) {
        super(_shop.getWorld(), new ShopPanelElmStyle());
        shop = _shop;
    }
}
