package com.snek.fancyplayershops.implementations.ui.details;

import com.snek.fancyplayershops.Shop;
import com.snek.fancyplayershops.implementations.ui.ShopCanvas;
import com.snek.fancyplayershops.implementations.ui.ShopTextElm;








public class DetailsUi extends ShopCanvas {

    public DetailsUi(Shop shop){
        super();
        addChild(new DetailsUiDisplay   (shop)); getLastChild().moveY(0.6f);
        addChild(new DetailsUiBackground(shop));
    }
}
