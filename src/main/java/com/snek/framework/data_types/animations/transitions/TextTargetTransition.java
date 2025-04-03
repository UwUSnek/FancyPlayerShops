package com.snek.framework.data_types.animations.transitions;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector4i;

import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.utils.Easing;




public class TextTargetTransition extends TargetTransition {
    private final @NotNull Vector4i bg;
    private final int alpha;


    public TextTargetTransition(@NotNull Transform _transform, int _duration, Easing _easing, @NotNull Vector4i _background, int _alpha) {
        super(_transform, _duration, _easing);
        bg = new Vector4i(_background);
        alpha = _alpha;
    }

    public TextTargetTransition(@NotNull Transition t, @NotNull Vector4i _background, int _alpha) {
        this(t.transform, t.getDuration(), t.getEasing(), _background, _alpha);
    }


    public Vector4i getBackground() { return bg;    };
    public int      getAlpha     () { return alpha; };
}

//! ---------------------------------------------------------------------------------
//! | COPY OF TextAdditiveTransition BECAUSE JAVA DOESN'T LIKE MULTIPLE INHERITANCE |
//! ---------------------------------------------------------------------------------