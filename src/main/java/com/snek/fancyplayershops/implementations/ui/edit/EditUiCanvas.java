package com.snek.fancyplayershops.implementations.ui.edit;

import com.snek.fancyplayershops.Shop;
import com.snek.fancyplayershops.implementations.ui.ShopCanvas;
import com.snek.fancyplayershops.implementations.ui.details.DetailsDisplay;








public class EditUiCanvas extends ShopCanvas {

    public EditUiCanvas(Shop shop){
        super();
        addChild(new DetailsDisplay(shop));
    }
}
