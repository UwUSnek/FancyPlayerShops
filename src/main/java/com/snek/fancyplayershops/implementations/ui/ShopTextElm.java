package com.snek.fancyplayershops.implementations.ui;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.Shop;




public class ShopTextElm extends TrackedTextElm {

    protected final @NotNull Shop shop;


    public ShopTextElm(@NotNull Shop _shop) {
        super(_shop.getWorld());
        shop = _shop;
    }
}
