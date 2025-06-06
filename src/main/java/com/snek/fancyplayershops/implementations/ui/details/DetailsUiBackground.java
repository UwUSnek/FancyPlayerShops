package com.snek.fancyplayershops.implementations.ui.details;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.Shop;
import com.snek.fancyplayershops.implementations.ui.ShopPanelElm;








/**
 * The background panel of DetailsUi.
 */
public class DetailsUiBackground extends ShopPanelElm {

    /**
     * Creates a new DetailsUiBackground.
     * @param _shop The target shop.
     */
    public DetailsUiBackground(@NotNull Shop _shop) {
        super(_shop);
        setSize(new Vector2f(1, DetailsUi.BACKGROUND_HEIGHT));
    }
}