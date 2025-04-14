package com.snek.framework.data_types.animations;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4i;

import com.snek.framework.data_types.animations.InterpolatedData;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.TransitionStep;
import com.snek.framework.utils.Easing;








/**
 * This class identifies a single transition.
 */
public class Transition {

    // Transition data
    private final int duration;
    private final @NotNull Easing easing;

    // Optional data
    private boolean additive;
    public final InterpolatedData d;




    public Transition(int _duration, Easing _easing) {
        duration  = _duration;
        easing    = _easing;

        additive   = false;
        d = new InterpolatedData(null, null, null);
    }


    public Transition targetTransform(Transform _transfor) {
        d.setTransform(_transfor);
        additive = false;
        return this;
    }

    public Transition additiveTransform(Transform _transfor) {
        d.setTransform(_transfor);
        additive = true;
        return this;
    }

    public Transition targetBackground(Vector4i _background) {
        d.setBackground(_background);
        return this;
    }

    public Transition targetOpacity(Integer _opacity) {
        d.setOpacity(_opacity);
        return this;
    }


    // /**
    //  * Computes the target transform based on the transition type.
    //  * @param initialTransform The initial transform.
    //  * @return The target transform.
    //  */
    // public abstract Transform compute(Transform initialTransform);


    /**
     * Creates an animation step based on the interpolation factor.
     * @param factor The interpolation factor.
     * @return The animation step.
     */
    public TransitionStep createStep(float factor) {
        // return new TransitionStep(factor, additive, d.getTransform(), d.getBackground(), d.getOpacity());
        return new TransitionStep(factor, additive, d);
    }


    public int     getDuration() { return duration; }
    public Easing  getEasing  () { return easing;   }
    public boolean isAdditive () { return additive; }
}
