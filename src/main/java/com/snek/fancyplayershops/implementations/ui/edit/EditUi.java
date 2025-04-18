package com.snek.fancyplayershops.implementations.ui.edit;

import org.joml.Vector2f;
import org.joml.Vector3i;

import com.snek.fancyplayershops.Shop;
import com.snek.fancyplayershops.implementations.ui.ShopCanvas;
import com.snek.fancyplayershops.implementations.ui.ShopFancyTextElm;
import com.snek.fancyplayershops.implementations.ui.ShopItemDisplay;
import com.snek.fancyplayershops.implementations.ui.details.DetailsUi;
import com.snek.fancyplayershops.implementations.ui.misc.ShopUiBorder;
import com.snek.framework.data_types.ui.AlignmentX;
import com.snek.framework.data_types.ui.AlignmentY;
import com.snek.framework.ui.Div;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.Utils;







/**
 * A UI that allows the owner of the shop to edit it.
 */
public class EditUi extends ShopCanvas {

    // Colors
    public static final Vector3i RGB_STOCK_COLOR = Utils.HSVtoRGB(DetailsUi.C_HSV_STOCK_HIGH);

    // Layout
    public static final float SQUARE_BUTTON_SIZE         = 0.2f;
    public static final float ROTATE_BUTTON_CENTER_Y     = 0.4f - SQUARE_BUTTON_SIZE / 2 + ShopItemDisplay.FOCUS_HEIGHT;
    public static final float ROTATE_BUTTON_CENTER_SHIFT = 0.3f;

    // Functionalities
    public static final float ROTATE_BUTTON_AMOUNT = (float)Math.toRadians(45);




    /**
     * Creates a new EditUi.
     * @param _shop The target shop.
     */
    public EditUi(Shop _shop){
        super();

        Div bg;
        Div e;

        // Add background
        bg = addChild(new EditUiBackground(_shop));

        // Add title
        e = bg.addChild(new EditUiTitle(_shop));
        e.moveY(1f - ShopFancyTextElm.LINE_H * 1f);
        e.setAlignmentX(AlignmentX.CENTER);

        // Add price button
        e = bg.addChild(new EditUiPriceButton(_shop));
        e.moveY(1f - ShopFancyTextElm.LINE_H * 2f);
        e.setAlignmentX(AlignmentX.LEFT);

        // Add stock limit button
        e = bg.addChild(new EditUiStockLimitButton(_shop));
        e.moveY(1f - ShopFancyTextElm.LINE_H * 3f);
        e.setAlignmentX(AlignmentX.LEFT);

        // Add rotation buttons
        e = bg.addChild(new EditUiRotateButton(_shop, -ROTATE_BUTTON_AMOUNT, new Txt("◀").get()));
        e.move(new Vector2f(-ROTATE_BUTTON_CENTER_SHIFT, ROTATE_BUTTON_CENTER_Y));
        e = bg.addChild(new EditUiRotateButton(_shop, +ROTATE_BUTTON_AMOUNT, new Txt("▶").get()));
        e.move(new Vector2f(+ROTATE_BUTTON_CENTER_SHIFT, ROTATE_BUTTON_CENTER_Y));

        // Add bottom border
        e = bg.addChild(new ShopUiBorder(_shop));
        e.setAlignmentY(AlignmentY.BOTTOM);
    }
}
