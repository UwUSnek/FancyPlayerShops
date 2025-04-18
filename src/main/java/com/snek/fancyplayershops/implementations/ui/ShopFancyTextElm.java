package com.snek.fancyplayershops.implementations.ui;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.Shop;
import com.snek.framework.ui.elements.FancyTextElm;
import com.snek.framework.ui.styles.ElmStyle;








/**
 * A generic FancyTextElm with configurable width and height and a target shop.
 */
public class ShopFancyTextElm extends FancyTextElm {
    public static final float LINE_H = 0.1f;
    protected final @NotNull Shop shop;


    /**
     * Creates a new ShopFancyTextElm ustin a custom style.
     * @param _shop The target shop.
     * @param w The width of the text element, expressed in blocks.
     * @param h The height of the text element, expressed in blocks.
     * @param _style The custom style.
     */
    public ShopFancyTextElm(@NotNull Shop _shop, float w, float h, ElmStyle _style) {
        super(_shop.getWorld(), _style);
        shop = _shop;
        setSize(new Vector2f(w, h));
    }


    /**
     * Creates a new ShopFancyTextElm using the default style.
     * @param _shop The target shop.
     * @param w The width of the text element, expressed in blocks.
     * @param h The height of the text element, expressed in blocks.
     */
    public ShopFancyTextElm(@NotNull Shop _shop, float w, float h) {
        super(_shop.getWorld());
        shop = _shop;
        setSize(new Vector2f(w, h));
    }
}
