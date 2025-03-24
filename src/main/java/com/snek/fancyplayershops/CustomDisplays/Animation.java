package com.snek.fancyplayershops.CustomDisplays;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;








/**
 * This class identifies an animation.
 * The animation is expressed as a list of Transitions.
 */
//TODO add custom interpolations. they create an approximated custom animation from a linear transition and an interpolation type
public class Animation {
    public final @NotNull List<Transition> transitions = new ArrayList<>();
    private int totalDuration = 0;


    public Animation(@NotNull Transition... _transitions) {
        for (Transition t : _transitions) {
            transitions.add(t);
            totalDuration += t.getDuration();
        }
    }


    public int getTotalDuration() {
        return totalDuration;
    }
}
