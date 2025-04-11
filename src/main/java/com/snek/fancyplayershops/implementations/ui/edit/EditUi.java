package com.snek.fancyplayershops.implementations.ui.edit;

import com.snek.fancyplayershops.Shop;
import com.snek.fancyplayershops.implementations.ui.ShopCanvas;
import com.snek.fancyplayershops.implementations.ui.ShopPanelElm;
import com.snek.framework.data_types.ui.AlignmentX;
import com.snek.framework.ui.Div;
import com.snek.framework.ui.TextElm;








public class EditUi extends ShopCanvas {

    public EditUi(Shop _shop){
        super();
        Div bg;
        Div e;

        bg = addChild(new EditUiBackground(_shop));

        e = bg.addChild(new EditUiTitle(_shop));
        e.moveY(0.6f + 0.05f + ((TextElm)e).calcHeight() * 1.6f * 2f);
        e.setAlignmentX(AlignmentX.CENTER);

        e = bg.addChild(new EditUiPriceButton(_shop));
        e.moveY(0.6f + 0.05f + ((TextElm)e).calcHeight() * 1.6f * 1f);
        e.setAlignmentX(AlignmentX.LEFT);
        System.out.println(e.getAbsPos());

        e = bg.addChild(new EditUiMaxStockButton(_shop));
        e.moveY(0.6f + 0.05f);
        e.setAlignmentX(AlignmentX.LEFT);
    }
}
