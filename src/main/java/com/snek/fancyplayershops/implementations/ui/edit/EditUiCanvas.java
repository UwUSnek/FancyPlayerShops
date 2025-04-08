package com.snek.fancyplayershops.implementations.ui.edit;

import com.snek.fancyplayershops.Shop;
import com.snek.fancyplayershops.implementations.ui.ShopCanvas;








public class EditUiCanvas extends ShopCanvas {

    public EditUiCanvas(Shop shop){
        super();
        addChild(new PriceButton(shop));
        addChild(new MaxStockButton(shop));
    }
}
