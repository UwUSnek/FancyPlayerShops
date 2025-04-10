package com.snek.fancyplayershops.implementations.ui.edit;

import com.snek.fancyplayershops.Shop;
import com.snek.fancyplayershops.implementations.ui.ShopCanvas;
import com.snek.framework.ui.TextElm;








public class EditUi extends ShopCanvas {

    public EditUi(Shop shop){
        super();
        addChild(new EditUiTitle         (shop)); getLastChild().moveY(0.6f + 0.05f + ((TextElm)getLastChild()).calcHeight() * 1.6f * 1f);
        addChild(new EditUiPriceButton   (shop)); getLastChild().moveY(0.6f + 0.05f + ((TextElm)getLastChild()).calcHeight() * 1.6f * 2f);
        addChild(new EditUiMaxStockButton(shop)); getLastChild().moveY(0.6f + 0.05f);
        addChild(new EditUiBackground    (shop)); getLastChild();
    }
}
