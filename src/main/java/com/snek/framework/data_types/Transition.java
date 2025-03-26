package com.snek.framework.data_types;

import org.jetbrains.annotations.NotNull;

import com.snek.framework.utils.Easing;




/**
 * This class identifies a single transition.
 * It can either be a target transition or an additive transition.
 * use .compute() to calculate the resulting transformation.
 */
public abstract class Transition {
    public  final @NotNull Transform transform;
    private final int duration;
    private final @NotNull Easing easing;


    public Transition(@NotNull Transform _transform, int _duration, Easing _easing) {
        transform = _transform;
        duration  = _duration;
        easing    = _easing;
    }
    public abstract Transform compute(Transform initialTransform);


    public int getDuration() {
        return duration;
    }
}
