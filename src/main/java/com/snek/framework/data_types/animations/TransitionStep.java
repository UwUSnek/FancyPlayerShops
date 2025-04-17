package com.snek.framework.data_types.animations;

import org.jetbrains.annotations.NotNull;








/**
 * A class that represets a single step of a transition.
 */
public class TransitionStep {
    private final float factor;
    private final boolean additive;
    public final InterpolatedData d;


    /**
     * Creates a new TransitionStep.
     * @param _factor The interpolation factor.
     * @param _additive Whether the transform is additive.
     * @param _d The interpolated data.
     */
    public TransitionStep(float _factor, boolean _additive, @NotNull InterpolatedData _d) {
        factor     = _factor;
        additive = _additive;
        d = _d;
    }


    // Getters
    public float   getFactor () { return factor; }
    public boolean isAdditive() { return additive; }
}
