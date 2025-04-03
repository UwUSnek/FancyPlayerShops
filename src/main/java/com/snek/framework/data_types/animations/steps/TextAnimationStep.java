package com.snek.framework.data_types.animations.steps;

import org.joml.Vector4i;

import com.snek.framework.data_types.animations.Transform;




public class TextAnimationStep extends AnimationStep {
    public final Vector4i background;
    public final int alpha;

    public TextAnimationStep(Transform _transform, float _factor, boolean _isAdditive, Vector4i _background, int _alpha) {
        super(_transform, _factor, _isAdditive);
        background = _background;
        alpha = _alpha;
    }
}
