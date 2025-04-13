package com.snek.fancyplayershops.implementations.ui;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.Shop;
import com.snek.framework.ui.TextElm;




public class ShopTextElm extends TextElm {
    public static final float LINE_H = 0.1f;

    protected final @NotNull Shop shop;


    public ShopTextElm(@NotNull Shop _shop, float w, float h) {
        super(_shop.getWorld());
        // style.editTransform().moveY(SHIFT_Y);
        shop = _shop;
        setSize(new Vector2f(w, h));
    }
}
