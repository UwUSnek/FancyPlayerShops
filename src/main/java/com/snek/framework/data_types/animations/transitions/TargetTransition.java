package com.snek.framework.data_types.animations.transitions;

import org.jetbrains.annotations.NotNull;

import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.steps.AnimationStep;
import com.snek.framework.utils.Easing;




/**
 * This class identifies a single interpolated target transition.
 * It stores the transformation to apply, the interpolation time and the interpolation easing.
 * Target transitions replace the element's data instead of modifying it.
 */
public class TargetTransition extends Transition {

    public TargetTransition(@NotNull Transform _transform, int _duration, Easing _easing) {
        super(_transform, _duration, _easing);
    }


    @Override
    public Transform compute(Transform initialTransform) {
        return transform;
    }


    @Override
    public AnimationStep createStep(Transform targetTransform, float factor) {
        return new AnimationStep(compute(targetTransform), factor, false);
    }
}
