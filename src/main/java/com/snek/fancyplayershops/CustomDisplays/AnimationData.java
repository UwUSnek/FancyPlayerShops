package com.snek.fancyplayershops.CustomDisplays;

import java.util.List;

import org.jetbrains.annotations.Nullable;




/**
 * This class identifies the spawn, loop and despawn animations.
 * They are stored as lists of transformation transitions.
 */
public class AnimationData {
    public final @Nullable Animation spawn;
    public final @Nullable Animation loop;
    public final @Nullable Animation despawn;
    private int totalDuration = 0;


    public AnimationData(@Nullable Animation _spawn, @Nullable Animation _loop, @Nullable Animation _despawn) {
        spawn = _spawn;
        loop = _loop;
        despawn = _despawn;

        if(spawn   != null) totalDuration += spawn.  getTotalDuration();
        if(loop    != null) totalDuration += loop.   getTotalDuration();
        if(despawn != null) totalDuration += despawn.getTotalDuration();
    }


    public int getTotalDuration() {
        return totalDuration;
    }
}
