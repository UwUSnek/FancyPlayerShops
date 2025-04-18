package com.snek.fancyplayershops.implementations.ui.details;

import org.joml.Vector3f;
import org.joml.Vector3i;

import com.snek.fancyplayershops.Shop;
import com.snek.fancyplayershops.implementations.ui.ShopCanvas;
import com.snek.fancyplayershops.implementations.ui.misc.ShopUiBorder;
import com.snek.framework.data_types.ui.AlignmentX;
import com.snek.framework.data_types.ui.AlignmentY;
import com.snek.framework.ui.Div;
import com.snek.framework.utils.Utils;








/**
 * A UI that shows informations about the shop.
 */
public class DetailsUi extends ShopCanvas {

    // Colors
    public static final Vector3i C_RGB_PRICE      = new Vector3i(243, 255, 0);
    public static final Vector3f C_HSV_STOCK_HIGH = Utils.RGBtoHSV(new Vector3i(0, 223, 0)); //! Float instead of int for more precision
    public static final Vector3f C_HSV_STOCK_LOW  = Utils.RGBtoHSV(new Vector3i(200, 0, 0)); //! Float instead of int for more precision

    // Layout
    public static final float BACKGROUND_HEIGHT = 0.4f;




    /**
     * Creates a new DetailsUi.
     * @param _shop The target shop.
     */
    public DetailsUi(Shop _shop){

        // Call superconstructo and add background
        super(new DetailsUiBackground(_shop));
        bg.setPosY(1 - DetailsUi.BACKGROUND_HEIGHT);
        Div e;

        // Add details display
        e = bg.addChild(new DetailsUiDisplay(_shop));
        e.setAlignmentX(AlignmentX.CENTER);
        e.setAlignmentY(AlignmentY.CENTER);

        // Add borders
        e = bg.addChild(new ShopUiBorder(_shop));
        e.setAlignmentY(AlignmentY.BOTTOM);
        e = bg.addChild(new ShopUiBorder(_shop));
        e.setAlignmentY(AlignmentY.TOP);
    }
}
