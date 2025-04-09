package com.snek.framework.ui.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector4i;

import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.transitions.TextAdditiveTransition;
import com.snek.framework.utils.Easings;








public class PanelElmStyle extends ElmStyle {
    private @NotNull Vector4i color;

    // The amount of panel elements of default size would be needed to cover the width of a block
    public static final float ENTITY_BLOCK_RATIO_X = 40f;
    public static final float ENTITY_BLOCK_RATIO_Y = 40f;




    /**
     * Creates a new default PanelElmStyle.
     */
    public PanelElmStyle() {

        // Set values
        super();
        setTransform(new Transform().scaleX(ENTITY_BLOCK_RATIO_X).scaleY(ENTITY_BLOCK_RATIO_Y).moveX(-0.5f));
        color = new Vector4i(130, 2, 20, 20);


        // Set default spawning animation
        setSpawnAnimation(new Animation(new TextAdditiveTransition(new Transform(),
            ElmStyle.S_TIME,
            Easings.sineOut,
            color,
            255
        )));


        // Set default despawning animation
        setDespawnAnimation(new Animation(new TextAdditiveTransition(new Transform(),
            ElmStyle.D_TIME,
            Easings.sineOut,
            new Vector4i(0),
            0
        )));
    }




    public PanelElmStyle setColor (Vector4i _color ) { color = _color; return this; }

    public Vector4i getColor () { return color;  }
}
