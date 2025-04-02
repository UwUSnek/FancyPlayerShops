package com.snek.framework.data_types;

import org.jetbrains.annotations.NotNull;

import com.snek.framework.utils.Easing;




/**
 * This class identifies a single interpolated target transition.
 * It stores the transformation to apply, the interpolation time and the interpolation easing.
 */
public class TargetTransition extends Transition {

    public TargetTransition(@NotNull Transform _transform, int _duration, Easing _easing) {
        super(_transform, _duration, _easing);
    }


    @Override
    public Transform compute(Transform initialTransform) {
        return transform;
    }
}
