package com.snek.fancyplayershops.implementations.ui.edit;

import com.snek.fancyplayershops.Shop;
import com.snek.fancyplayershops.implementations.ui.ShopCanvas;
import com.snek.fancyplayershops.implementations.ui.ShopTextElm;
import com.snek.framework.data_types.ui.AlignmentX;
import com.snek.framework.ui.Div;







/**
 * A UI that allows the owner of the shop to edit it.
 */
public class EditUi extends ShopCanvas {


    /**
     * Creates a new EditUi.
     * @param _shop The target shop.
     */
    public EditUi(Shop _shop){
        super();

        Div bg;
        Div e;

        bg = addChild(new EditUiBackground(_shop));

        e = bg.addChild(new EditUiTitle(_shop));
        e.moveY(1f - ShopTextElm.LINE_H * 1f);
        e.setAlignmentX(AlignmentX.CENTER);

        e = bg.addChild(new EditUiPriceButton(_shop));
        e.moveY(1f - ShopTextElm.LINE_H * 2f);
        e.setAlignmentX(AlignmentX.LEFT);

        e = bg.addChild(new EditUiStockLimitButton(_shop));
        e.moveY(1f - ShopTextElm.LINE_H * 3f);
        e.setAlignmentX(AlignmentX.LEFT);
    }
}
