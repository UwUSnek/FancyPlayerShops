package com.snek.fancyplayershops.implementations.ui;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector4i;

import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.transitions.AdditiveTransition;
import com.snek.framework.data_types.animations.transitions.TextAdditiveTransition;
import com.snek.framework.ui.Canvas;
import com.snek.framework.ui.styles.ElmStyle;
import com.snek.framework.utils.Easings;







public class ShopCanvas extends Canvas {
    // Animation data //! Calculate translation from rotation
    public static final float    ANIMATION_ROT_Y    = (float)Math.toRadians(5);
    public static final Vector3f ANIMATION_MOVE_IN  = new Vector3f(0, 0, 1).rotateY(-ANIMATION_ROT_Y).sub(0, 0, 1);
    public static final Vector3f ANIMATION_MOVE_OUT = new Vector3f(ANIMATION_MOVE_IN.x, 0, -ANIMATION_MOVE_IN.z);


    // The animation to apply to the element to make it look like it starts from an "inactive" state
    public @Nullable Animation menuAnimationInitial;
    public @Nullable Animation menuAnimationIn;
    public @Nullable Animation menuAnimationOut;




    public ShopCanvas() {
        menuAnimationInitial = new Animation(new TextAdditiveTransition(
            new Transform().rotY(-ANIMATION_ROT_Y).move(new Vector3f(ANIMATION_MOVE_IN).negate()),
            0,
            Easings.linear,
            new Vector4i(0, 0, 0, 0),
            0
        ));

        menuAnimationIn = new Animation(new AdditiveTransition(
            new Transform().rotY(ANIMATION_ROT_Y).move(ANIMATION_MOVE_IN),
            ElmStyle.S_TIME,
            Easings.sineOut
        ));

        menuAnimationOut = new Animation(new AdditiveTransition(
            new Transform().rotY(ANIMATION_ROT_Y).move(ANIMATION_MOVE_OUT),
            ElmStyle.D_TIME,
            Easings.sineOut
        ));
    }




    @Override
    public void spawn(Vector3d pos){
        if(menuAnimationInitial != null) applyAnimationNow(menuAnimationInitial);
        super.spawn(pos);
        if(menuAnimationIn != null) applyAnimation(menuAnimationIn);
    }


    @Override
    public void despawn(){
        if(menuAnimationOut != null) applyAnimation(menuAnimationOut);
        super.despawn();
    }
}