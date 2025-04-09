package com.snek.fancyplayershops.implementations.ui;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.Shop;




public class ShopTextElm extends TrackedTextElm {
    public static final float SHIFT_Y = 0.6f;

    protected final @NotNull Shop shop;


    public ShopTextElm(@NotNull Shop _shop) {
        super(_shop.getWorld());
        style.editTransform().moveY(SHIFT_Y);
        shop = _shop;
    }
}
