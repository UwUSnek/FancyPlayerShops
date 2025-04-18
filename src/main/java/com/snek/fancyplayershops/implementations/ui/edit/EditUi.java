package com.snek.fancyplayershops.implementations.ui.edit;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3d;
import org.joml.Vector3i;

import com.snek.fancyplayershops.Shop;
import com.snek.fancyplayershops.implementations.ui.ShopCanvas;
import com.snek.fancyplayershops.implementations.ui.ShopFancyTextElm;
import com.snek.fancyplayershops.implementations.ui.ShopItemDisplay;
import com.snek.fancyplayershops.implementations.ui.details.DetailsUi;
import com.snek.fancyplayershops.implementations.ui.misc.ShopUiBorder;
import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.data_types.ui.AlignmentX;
import com.snek.framework.data_types.ui.AlignmentY;
import com.snek.framework.ui.Div;
import com.snek.framework.ui.elements.Elm;
import com.snek.framework.utils.Easings;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.Utils;







/**
 * A UI that allows the owner of the shop to edit it.
 */
public class EditUi extends ShopCanvas {
    private final @NotNull Elm bottomBorder;

    // Colors
    public static final Vector3i RGB_STOCK_COLOR = Utils.HSVtoRGB(DetailsUi.C_HSV_STOCK_HIGH);

    // Layout
    public static final int   SPAWN_SIZE_TIME            = 8;
    public static final float SQUARE_BUTTON_SIZE         = 0.12f;
    public static final float ROTATE_BUTTON_CENTER_Y     = 0.4f - SQUARE_BUTTON_SIZE / 2 + ShopItemDisplay.FOCUS_HEIGHT;
    public static final float ROTATE_BUTTON_CENTER_SHIFT = 0.3f;

    // Functionalities
    public static final float ROTATE_BUTTON_AMOUNT = (float)Math.toRadians(45);








    /**
     * Creates a new EditUi.
     * @param _shop The target shop.
     */
    public EditUi(Shop _shop){


        // Call superconstructo and add background
        super((Elm)_shop.getActiveCanvas().getChildren().get(0));
        Div e;

        // Instantly despawn and remove previous children
        for (Div c : bg.getChildren()) c.despawnNow();
        bg.clearChildren();

        // Reset size and position, visually simulate the previous values using an instant animation
        bg.setSizeY(1);
        bg.setPosY(0);
        bg.applyAnimationNow(new Animation(
            new Transition()
            .additiveTransform(new Transform().scaleY(DetailsUi.BACKGROUND_HEIGHT).moveY(1 - DetailsUi.BACKGROUND_HEIGHT))
        ));




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
        // e.setAlignmentY(AlignmentY.BOTTOM);
        e.applyAnimationNow(new Animation(
            new Transition(0,Easings.linear)
            .additiveTransform(new Transform().moveY(1 - DetailsUi.BACKGROUND_HEIGHT))
        ));
        bottomBorder = (Elm)e;
    }








    @Override
    public void spawn(Vector3d pos){

        // Only spawn the children of the background element. The background itself is already spawned
        for (Div c : bg.getChildren()) {
            c.spawn(pos);
        }

        // Apply an animation to the background to make it look like it's stretching back to the normal height
        bg.applyAnimation(new Animation(
            new Transition(EditUi.SPAWN_SIZE_TIME, Easings.sineOut)
            .additiveTransform(new Transform().scaleY(1 / DetailsUi.BACKGROUND_HEIGHT).moveY(-(1 - DetailsUi.BACKGROUND_HEIGHT)))
        ));
        bottomBorder.applyAnimation(new Animation(
            new Transition(SPAWN_SIZE_TIME, Easings.sineOut)
            .additiveTransform(new Transform().moveY(-(1 - DetailsUi.BACKGROUND_HEIGHT)))
        ));
    }
}
