package com.snek.framework.data_types.animations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.snek.framework.data_types.animations.transitions.Transition;








/**
 * This class identifies an animation.
 * The animation is expressed as a list of Transitions.
 */
public class Animation {
    private final @NotNull List<Transition> transitions = new ArrayList<>();
    private int totalDuration = 0;


    public Animation(@NotNull Transition... _transitions) {
        for (Transition t : _transitions) {
            transitions.add(t);
            totalDuration += t.getDuration();
        }
    }


    /**
     * Returns the list of transitions that make up this animation as a read-only list
     * @return The translations.
     */
    public List<Transition> getTransitions() {
        return Collections.unmodifiableList(transitions);
    }


    public int getTotalDuration() {
        return totalDuration;
    }
}
