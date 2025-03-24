package com.snek.fancyplayershops.CustomDisplays;

import org.jetbrains.annotations.NotNull;

/**
 * This class identifies a single linearly interpolated transition.
 * It stores the target transformation and the interpolation time.
 */
public class Transition {
    public final @NotNull Transform transform;
    private final int duration;


    public Transition(@NotNull Transform _transform, int _duration) {
        transform = _transform;
        duration = _duration;
    }


    public int getDuration() {
        return duration;
    }
}
