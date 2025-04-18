package com.snek.fancyplayershops.implementations.ui;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import org.joml.Vector3f;

import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.ui.Div;
import com.snek.framework.ui.elements.Elm;
import com.snek.framework.ui.styles.ElmStyle;
import com.snek.framework.utils.Easings;








/**
 * A generic canvas class used to create shop menus.
 */
public class ShopCanvas extends Div {
    protected final @NotNull Elm bg;
    public @NotNull Elm getBackground() { return bg; }


    // Animation data //! Calculate translation from rotation
    public static final float    ANIMATION_ROT_Y    = (float)Math.toRadians(2);
    public static final Vector3f ANIMATION_MOVE_IN  = new Vector3f(0, 0, 1).rotateY(-ANIMATION_ROT_Y).sub(0, 0, 1);
    public static final Vector3f ANIMATION_MOVE_OUT = new Vector3f(ANIMATION_MOVE_IN.x, 0, -ANIMATION_MOVE_IN.z);


    // // Animations
    // public @Nullable Animation menuAnimationInitial;
    // public @Nullable Animation menuAnimationIn;
    // public @Nullable Animation menuAnimationOut;




    /**
     * Creates a new ShopCanvas
     * @param _bg The background element
     */
    public ShopCanvas(Elm _bg) {
        super();
        addChild(_bg);
        bg = _bg;

        // menuAnimationInitial = new Animation(
        //     new Transition(0, Easings.linear)
        //     .additiveTransform(
        //         new Transform()
        //         .move(new Vector3f(ANIMATION_MOVE_IN).negate())
        //         .rotGlobalY(-ANIMATION_ROT_Y)
        //     )
        // );

        // menuAnimationIn = new Animation(
        //     new Transition(ElmStyle.S_TIME, Easings.sineOut)
        //     .additiveTransform(
        //         new Transform()
        //         .move(ANIMATION_MOVE_IN)
        //         .rotGlobalY(ANIMATION_ROT_Y)
        //     )
        // );

        // menuAnimationOut = new Animation(
        //     new Transition(ElmStyle.D_TIME, Easings.sineOut)
        //     .additiveTransform(
        //         new Transform()
        //         .move(ANIMATION_MOVE_OUT)
        //         .rotGlobalY(ANIMATION_ROT_Y)
        //     )
        // );
    }




    @Override
    public void spawn(Vector3d pos){
        // if(menuAnimationInitial != null) applyAnimationNowRecursive(menuAnimationInitial);
        super.spawn(pos);
        // if(menuAnimationIn != null) applyAnimationRecursive(menuAnimationIn);
    }


    @Override
    public void despawn(){
        // if(menuAnimationOut != null) applyAnimationRecursive(menuAnimationOut);
        super.despawn();
    }
}