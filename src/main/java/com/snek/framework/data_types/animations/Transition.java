package com.snek.framework.data_types.animations;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector4i;

import com.snek.framework.utils.Easing;
import com.snek.framework.utils.Easings;








/**
 * A single interpolated transition.
 */
public class Transition {

    // Transition data
    private final int duration;
    private final @NotNull Easing easing;

    // Optional data
    private boolean additive;
    public final InterpolatedData d;




    /**
     * Creates a new Transition.
     * @param _duration The total duration of the transition.
     * @param _easing The type of easing to use.
     */
    public Transition(int _duration, Easing _easing) {
        duration  = _duration;
        easing    = _easing;

        additive   = false;
        d = new InterpolatedData(null, null, null);
    }


    /**
     * Creates a new Transition with 0 duration and linear easing.
     */
    public Transition() {
        this(0, Easings.linear);
    }




    /**
     * Specifies that the transform of the element this transition is applied to needs to reach a certain value.
     * @param _transform The transform value.
     * @return This transition.
     */
    public Transition targetTransform(Transform _transform) {
        d.setTransform(_transform);
        additive = false;
        return this;
    }
    /**
     * Specifies that a certain transform value has to be applied to the transform of the element this transition is applied to.
     * @param _transform The transform value.
     * @return This transition.
     */
    public Transition additiveTransform(Transform _transform) {
        d.setTransform(_transform);
        additive = true;
        return this;
    }


    /**
     * Specifies that the foreground transform of the element this transition is applied to needs to reach a certain value.
     * @param _transform The transform value.
     * @return This transition.
     */
    public Transition targetTransformFg(Transform _transform) {
        d.setTransformFg(_transform);
        additive = false;
        return this;
    }
    /**
     * Specifies that a certain transform value has to be applied to the foreground transform of the element this transition is applied to.
     * @param _transform The transform value.
     * @return This transition.
     */
    public Transition additiveTransformFg(Transform _transform) {
        d.setTransformFg(_transform);
        additive = true;
        return this;
    }


    /**
     * Specifies that the background transform of the element this transition is applied to needs to reach a certain value.
     * @param _transform The transform value.
     * @return This transition.
     */
    public Transition targetTransformBg(Transform _transform) {
        d.setTransformBg(_transform);
        additive = false;
        return this;
    }
    /**
     * Specifies that a certain transform value has to be applied to the background transform of the element this transition is applied to.
     * @param _transform The transform value.
     * @return This transition.
     */
    public Transition additiveTransformBg(Transform _transform) {
        d.setTransformBg(_transform);
        additive = true;
        return this;
    }


    /**
     * Specifies that the background color of the element this transition is applied to needs to reach a certain value.
     * @param _background The background color value.
     * @return This transition.
     */
    public Transition targetBackground(Vector4i _background) {
        d.setBackground(_background);
        return this;
    }


    /**
     * Specifies that the text opacity of the element this transition is applied to needs to reach a certain value.
     * @param _opacity The text opacity value.
     * @return This transition.
     */
    public Transition targetOpacity(Integer _opacity) {
        d.setOpacity(_opacity);
        return this;
    }




    /**
     * Creates an animation step from this transition based on the interpolation factor.
     * @param factor The interpolation factor.
     * @return The animation step.
     */
    public TransitionStep createStep(float factor) {
        return new TransitionStep(factor, additive, d);
    }




    // Getters
    public int     getDuration() { return duration; }
    public Easing  getEasing  () { return easing;   }
    public boolean isAdditive () { return additive; }
}
