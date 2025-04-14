package com.snek.framework.data_types.animations.steps;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4i;

import com.snek.framework.data_types.animations.InterpolatedData;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.utils.Easing;




public class AnimationStep {
    private final float factor;
    private final boolean additive;
    public final InterpolatedData d;


    public AnimationStep(float _factor, boolean _additive, @NotNull InterpolatedData _d) {
        factor     = _factor;
        additive = _additive;
        d = _d;
    }


    public float   getFactor () { return factor; }
    public boolean isAdditive() { return additive; }
}
