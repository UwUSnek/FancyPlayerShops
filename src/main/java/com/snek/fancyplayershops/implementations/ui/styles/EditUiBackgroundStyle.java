package com.snek.fancyplayershops.implementations.ui.styles;

import com.snek.fancyplayershops.implementations.ui.details.DetailsUi;
import com.snek.fancyplayershops.implementations.ui.edit.EditUi;
import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.utils.Easings;








/**
 * The style of the EditUiBackground UI element.
 */
public class EditUiBackgroundStyle extends ShopPanelElmStyle {

    /**
     * Creates a new EditUiBackgroundStyle.
     */
    public EditUiBackgroundStyle(){
        super();
    }


    @Override
    public Animation getDefaultPrimerAnimation(){
        return new Animation(
            new Transition()
            .additiveTransform(new Transform().scaleY(DetailsUi.BACKGROUND_HEIGHT).moveY(1 - DetailsUi.BACKGROUND_HEIGHT))
        );
    }


    @Override
    public Animation getDefaultSpawnAnimation(){
        return new Animation(
            new Transition(EditUi.SPAWN_SIZE_TIME, Easings.sineOut)
            .additiveTransform(new Transform().scaleY(1 / DetailsUi.BACKGROUND_HEIGHT).moveY(-(1 - DetailsUi.BACKGROUND_HEIGHT)))
        );
    }
}
//TODO REMOVE CLASS IF NOT NEEDED