package com.snek.fancyplayershops.implementations.ui.details;

import com.snek.fancyplayershops.Shop;
import com.snek.fancyplayershops.implementations.ui.ShopCanvas;
import com.snek.fancyplayershops.implementations.ui.ShopPanelElm;
import com.snek.framework.data_types.ui.AlignmentX;
import com.snek.framework.ui.Div;








public class DetailsUi extends ShopCanvas {

    public DetailsUi(Shop _shop){
        super();
        Div bg;
        Div e;

        bg = addChild(new DetailsUiBackground(_shop));

        e = bg.addChild(new DetailsUiDisplay(_shop));
        e.moveY(0.6f);
        e.setAlignmentX(AlignmentX.CENTER);
    }
}
