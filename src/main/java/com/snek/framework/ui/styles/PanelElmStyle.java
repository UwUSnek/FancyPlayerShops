package com.snek.framework.ui.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector4i;

import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.transitions.TextAdditiveTransition;
import com.snek.framework.data_types.containers.Flagged;
import com.snek.framework.utils.Easings;








public class PanelElmStyle extends ElmStyle {

    // The amount of panel elements of default size would be needed to cover the width of a block
    public static final float ENTITY_BLOCK_RATIO_X = 40f;
    public static final float ENTITY_BLOCK_RATIO_Y = 40f;

    // The translation on the X axis needed to align the panel entity with the element's bounding box
    public static final float ENTITY_SHIFT_X = -0.5f;


    private Flagged<Vector4i> color = null;





    /**
     * Creates a new default PanelElmStyle.
     */
    public PanelElmStyle() {
        super();
    }


    @Override
    public void resetAll(){
        resetColor();
        super.resetAll();
    }




    @Override
    public Transform getDefaultTransform(){
        return new Transform()
            .scaleX(ENTITY_BLOCK_RATIO_X)
            .scaleY(ENTITY_BLOCK_RATIO_Y)
            .moveX(ENTITY_SHIFT_X)
        ;
    }


    @Override
    public Animation getDefaultSpawnAnimation() {
        // System.out.println("Chosen color: " +  getDefaultColor().toString());
        return new Animation(new TextAdditiveTransition(
            new Transform(), //TODO dont include in text transitions
            ElmStyle.S_TIME,
            Easings.sineOut,
            // color.get(),
            getDefaultColor(),
            255
        ));
    }


    @Override
    public Animation getDefaultDespawnAnimation() {
        return new Animation(new TextAdditiveTransition(
            new Transform(), //TODO dont include in text transitions
            ElmStyle.D_TIME,
            Easings.sineOut,
            // new Vector4i(0),
            new Vector4i(getDefaultColor().mul(new Vector4i(0, 1, 1, 1))),
            0
        ));
    }




    public @NotNull Vector4i getDefaultColor () { return new Vector4i(130, 2, 20, 20); }
    public void resetColor () { color = Flagged.from(getDefaultColor()); }
    public void setColor (@NotNull Vector4i _color ) { color.set(_color); }
    public @NotNull Flagged<Vector4i> getFlaggedColor () { return color; }
    public @NotNull Vector4i getColor () { return color.get(); }
    public @NotNull Vector4i editColor () { return color.edit(); }
}
