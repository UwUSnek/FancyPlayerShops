package com.snek.framework.data_types.animations;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4i;

import com.snek.framework.data_types.animations.InterpolatedData;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.utils.Easing;




public class TransitionStep {
    private final float factor;
    private final boolean additive;
    public final InterpolatedData d;


    public TransitionStep(float _factor, boolean _additive, @NotNull InterpolatedData _d) {
        factor     = _factor;
        additive = _additive;
        d = _d;
    }


    public float   getFactor () { return factor; }
    public boolean isAdditive() { return additive; }
}
