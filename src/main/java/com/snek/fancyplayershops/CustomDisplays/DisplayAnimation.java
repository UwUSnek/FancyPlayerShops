package com.snek.fancyplayershops.CustomDisplays;

import java.util.List;




/**
 * This class identifies the spawn, loop and despawn animations.
 * They are stored as lists of transformation transitions.
 */
public class DisplayAnimation {
    public final List<TransformTransition> spawn;
    public final List<TransformTransition> loop;
    public final List<TransformTransition> despawn;

    public DisplayAnimation(List<TransformTransition> _spawn, List<TransformTransition> _loop, List<TransformTransition> _despawn) {
        spawn = _spawn;
        loop = _loop;
        despawn = _despawn;
    }
}
