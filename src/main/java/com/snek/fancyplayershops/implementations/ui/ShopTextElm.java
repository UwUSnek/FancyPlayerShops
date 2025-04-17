package com.snek.fancyplayershops.implementations.ui;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.Shop;
import com.snek.framework.ui.elements.FancyTextElm;








/**
 * A generic FancyTextElm with configurable width and height and a target shop.
 */
public class ShopTextElm extends FancyTextElm {
    public static final float LINE_H = 0.1f;
    protected final @NotNull Shop shop;


    /**
     * Creates a new ShopTextElm.
     * @param _shop The target shop.
     * @param w The width of the text element, expressed in blocks.
     * @param h The height of the text element, expressed in blocks.
     */
    public ShopTextElm(@NotNull Shop _shop, float w, float h) {
        super(_shop.getWorld());
        shop = _shop;
        setSize(new Vector2f(w, h));
    }
}
