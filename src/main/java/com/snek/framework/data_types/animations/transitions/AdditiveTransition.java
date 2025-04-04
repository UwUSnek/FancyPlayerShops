package com.snek.framework.data_types.animations.transitions;

import org.jetbrains.annotations.NotNull;

import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.steps.AnimationStep;
import com.snek.framework.utils.Easing;




/**
 * This class identifies a single interpolated target transition.
 * It stores the transformation to apply, the interpolation time and the interpolation easing.
 * Additive transitions are applied on top of the element's data instead of replacing it.
 */
public class AdditiveTransition extends Transition {

    public AdditiveTransition(@NotNull Transform _transform, int _duration, Easing _easing) {
        super(_transform, _duration, _easing);
    }


    @Override
    public Transform compute(Transform initialTransform) {
        return initialTransform.clone().apply(transform);
    }


    @Override
    public AnimationStep createStep(Transform targetTransform, float factor) {
        return new AnimationStep(compute(targetTransform), factor, true);
    }
}
