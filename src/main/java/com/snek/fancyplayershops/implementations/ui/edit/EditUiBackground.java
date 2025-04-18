package com.snek.fancyplayershops.implementations.ui.edit;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

import com.snek.fancyplayershops.Shop;
import com.snek.fancyplayershops.implementations.ui.ShopPanelElm;
import com.snek.fancyplayershops.implementations.ui.details.DetailsUi;
import com.snek.fancyplayershops.implementations.ui.styles.EditUiBackgroundStyle;
import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.utils.Easings;








/**
 * The background panel of EditUi.
 */
public class EditUiBackground extends ShopPanelElm {

    /**
     * Creates a new EditUiBackground.
     * @param _shop The target shop.
     */
    public EditUiBackground(@NotNull Shop _shop) {
        super(_shop, new EditUiBackgroundStyle());
    }


    // @Override
    // public void spawn(Vector3d pos){
        // applyAnimationNow(new Animation(
        //     new Transition(0, Easings.linear)
        //     .targetTransform(new Transform().scaleY(1 - DetailsUi.BACKGROUND_HEIGHT).moveY(DetailsUi.BACKGROUND_HEIGHT))
        // ));

    //     super.spawn(pos);
    //     applyAnimation(new Animation(
    //         new Transition(EditUi.SPAWN_SIZE_TIME, Easings.sineOut)
    //         .targetTransform(new Transform().scaleY(1 / (1 - DetailsUi.BACKGROUND_HEIGHT)).moveY(-DetailsUi.BACKGROUND_HEIGHT))
    //     ));
    // }
}
