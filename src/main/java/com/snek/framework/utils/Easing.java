package com.snek.framework.utils;

import java.util.function.Function;

import org.jetbrains.annotations.NotNull;








public final class Easing {
    private @NotNull Function<Double, Double> f;

    public Easing(@NotNull Function<Double, Double> _f) {
        f = _f;
    }

    public double compute(double x) {
        return f.apply(x);
    }
}
