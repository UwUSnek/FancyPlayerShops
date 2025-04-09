package com.snek.fancyplayershops.implementations.ui.details;

import com.snek.fancyplayershops.Shop;
import com.snek.fancyplayershops.implementations.ui.ShopCanvas;








public class DetailsUiCanvas extends ShopCanvas {

    public DetailsUiCanvas(Shop shop){
        super();
        addChild(new DetailsUiDisplay(shop));
    }
}
