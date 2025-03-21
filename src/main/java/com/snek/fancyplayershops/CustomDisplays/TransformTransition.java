package com.snek.fancyplayershops.CustomDisplays;

import net.minecraft.util.math.AffineTransformation;


/**
 * This class identifies a single linearly interpolated transition.
 * It stores the target transformation and the interpolation time.
 */
public class TransformTransition {
    AffineTransformation transform;
    int duration;
}
