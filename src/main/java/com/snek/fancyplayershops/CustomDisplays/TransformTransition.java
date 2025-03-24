package com.snek.fancyplayershops.CustomDisplays;

import net.minecraft.util.math.AffineTransformation;


/**
 * This class identifies a single linearly interpolated transition.
 * It stores the target transformation and the interpolation time.
 */
public class TransformTransition {
    public final Transform transform;
    public final int duration;

    public TransformTransition(Transform _transform, int _duration) {
        transform = _transform;
        duration = _duration;
    }
}
