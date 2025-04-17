package com.snek.framework.utils;

import java.util.function.UnaryOperator;

import org.jetbrains.annotations.NotNull;








/**
 * A class that can store and compute a unary operator on request.
 * This is meant for interpolation easings.
 */
public final class Easing {
    private @NotNull UnaryOperator<Double> f;


    /**
     * Creates a new Easing with the specified operator.
     * @param _f The operator.
     *     This function takes the linear progress and returns the corresponding progress that
     *     the custom easing would produce at the same point in time.
     */
    public Easing(@NotNull UnaryOperator<Double> _f) {
        f = _f;
    }


    /**
     * This function takes the linear progress and returns the corresponding progress that
     * the custom easing would produce at the same point in time.
     * @param x The linear progress.
     * @return The progress with the custom easing applied.
     */
    public double compute(double x) {
        return f.apply(x);
    }
}
