package com.snek.framework.data_types.animations.steps;

import com.snek.framework.data_types.animations.Transform;




public class AnimationStep {
    public final Transform transform;
    public final float factor;
    public final boolean isAdditive;

    public AnimationStep(Transform _transform, float _factor, boolean _isAdditive) {
        transform  = _transform;
        factor     = _factor;
        isAdditive = _isAdditive;
    }
}
