package com.snek.fancyplayershops.implementations.ui;

import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector4i;

import com.snek.fancyplayershops.Shop;
import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.ui.Div;
import com.snek.framework.ui.styles.ElmStyle;
import com.snek.framework.utils.Easings;







public class ShopCanvas extends Div {

    // Animation data //! Calculate translation from rotation
    public static final float    ANIMATION_ROT_Y    = (float)Math.toRadians(2);
    // public static final float    ANIMATION_ROT_Y    = (float)Math.toRadians(0);

    public static final Vector3f ANIMATION_MOVE_IN  = new Vector3f(0, 0, 1).rotateY(-ANIMATION_ROT_Y).sub(0, 0, 1);
    public static final Vector3f ANIMATION_MOVE_OUT = new Vector3f(ANIMATION_MOVE_IN.x, 0, -ANIMATION_MOVE_IN.z);
    // public static final Vector3f ANIMATION_MOVE_IN  = new Vector3f(0, 0, 0);
    // public static final Vector3f ANIMATION_MOVE_OUT = new Vector3f(0, 0, 0);


    // The animation to apply to the element to make it look like it starts from an "inactive" state
    public @Nullable Animation menuAnimationInitial;
    public @Nullable Animation menuAnimationIn;
    public @Nullable Animation menuAnimationOut;




    public ShopCanvas() {
        super();

        menuAnimationInitial = new Animation(
            new Transition(0, Easings.linear)
            .additiveTransform(
                new Transform()
                .move(new Vector3f(ANIMATION_MOVE_IN).negate())
                .rotGlobalY(-ANIMATION_ROT_Y)
            )
        );

        menuAnimationIn = new Animation(
            new Transition(ElmStyle.S_TIME, Easings.sineOut)
            .additiveTransform(
                new Transform()
                .move(ANIMATION_MOVE_IN)
                .rotGlobalY(ANIMATION_ROT_Y)
            )
        );

        menuAnimationOut = new Animation(
            new Transition(ElmStyle.D_TIME, Easings.sineOut)
            .additiveTransform(
                new Transform()
                .move(ANIMATION_MOVE_OUT)
                .rotGlobalY(ANIMATION_ROT_Y)
            )
        );
    }




    @Override
    public void spawn(Vector3d pos){
        if(menuAnimationInitial != null) applyAnimationNowRecursive(menuAnimationInitial);
        super.spawn(pos);
        if(menuAnimationIn != null) applyAnimationRecursive(menuAnimationIn);
    }


    @Override
    public void despawn(){
        if(menuAnimationOut != null) applyAnimationRecursive(menuAnimationOut);
        super.despawn();
    }
}