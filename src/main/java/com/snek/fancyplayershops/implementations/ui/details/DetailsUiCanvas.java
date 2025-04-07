package com.snek.fancyplayershops.implementations.ui.details;

import com.snek.fancyplayershops.Shop;
import com.snek.framework.ui.Canvas;








public class DetailsUiCanvas extends Canvas {

    public DetailsUiCanvas(Shop shop){
        super();
        addChild(new DetailsDisplay(shop));
    }
}
