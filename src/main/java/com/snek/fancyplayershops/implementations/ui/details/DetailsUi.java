package com.snek.fancyplayershops.implementations.ui.details;

import com.snek.fancyplayershops.Shop;
import com.snek.fancyplayershops.implementations.ui.ShopCanvas;
import com.snek.framework.data_types.ui.AlignmentX;
import com.snek.framework.ui.Div;








public class DetailsUi extends ShopCanvas {

    public DetailsUi(Shop shop){
        super();
        Div e;

        e = addChild(new DetailsUiDisplay(shop));
        e.moveY(0.6f);
        e.setAlignmentX(AlignmentX.CENTER);

        addChild(new DetailsUiBackground(shop));
    }
}
