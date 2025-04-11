package com.snek.fancyplayershops.implementations.ui.edit;

import com.snek.fancyplayershops.Shop;
import com.snek.fancyplayershops.implementations.ui.ShopCanvas;
import com.snek.framework.data_types.ui.AlignmentX;
import com.snek.framework.ui.Div;
import com.snek.framework.ui.TextElm;








public class EditUi extends ShopCanvas {

    public EditUi(Shop shop){
        super();
        Div e;

        e = addChild(new EditUiTitle(shop));
        e.moveY(0.6f + 0.05f + ((TextElm)e).calcHeight() * 1.6f * 2f);
        e.setAlignmentX(AlignmentX.CENTER);

        e = addChild(new EditUiPriceButton(shop));
        e.moveY(0.6f + 0.05f + ((TextElm)e).calcHeight() * 1.6f * 1f);
        e.setAlignmentX(AlignmentX.LEFT);
        System.out.println(e.getAbsPos());

        e = addChild(new EditUiMaxStockButton(shop));
        e.moveY(0.6f + 0.05f);
        e.setAlignmentX(AlignmentX.LEFT);

        e = addChild(new EditUiBackground(shop));
    }
}
