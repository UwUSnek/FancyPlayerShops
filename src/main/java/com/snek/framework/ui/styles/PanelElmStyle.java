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


    private Flagged<Vector4i> color = null;





    /**
     * Creates a new default PanelElmStyle.
     */
    public PanelElmStyle() {

        // Set values
        super();
        resetColor();
        setTransform(new Transform().scaleX(ENTITY_BLOCK_RATIO_X).scaleY(ENTITY_BLOCK_RATIO_Y).moveX(-0.5f));


        //TODO make additive and target interfaces
        // Set default spawning animation
        setSpawnAnimation(new Animation(new TextAdditiveTransition(
            new Transform(), //TODO dont include in text transitions
            ElmStyle.S_TIME,
            Easings.sineOut,
            color.get(),
            255
        )));


        // Set default despawning animation
        setDespawnAnimation(new Animation(new TextAdditiveTransition(
            new Transform(), //TODO dont include in text transitions
            ElmStyle.D_TIME,
            Easings.sineOut,
            new Vector4i(0),
            0
        )));
    }




    public @NotNull Vector4i getDefaultColor () { return new Vector4i(130, 2, 20, 20); }
    public void resetColor () { color = Flagged.from(getDefaultColor()); }
    public void setColor (@NotNull Vector4i _color ) { color.set(_color); }
    public @NotNull Flagged<Vector4i> getFlaggedColor () { return color; }
    public @NotNull Vector4i getColor () { return color.get(); }
    public @NotNull Vector4i editColor () { return color.edit(); }
}
