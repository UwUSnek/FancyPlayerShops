package com.snek.framework.ui.styles;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.framework.custom_displays.CustomDisplay;
import com.snek.framework.data_types.Animation;
import com.snek.framework.data_types.Flagged;
import com.snek.framework.data_types.Transform;

import net.minecraft.entity.decoration.DisplayEntity.BillboardMode;








/**
 * This class identifies the spawn and despawn animations.
 * They are stored as lists of transformation transitions.
 */
public class AnimationData {
    public final @Nullable Animation spawn;
    public final @Nullable Animation despawn;




    /**
     * Creates a new AnimationData
     * @param _transform The default transformation. This is where the spawning animation will start from.
     * @param _spawn The spawning animation.
     * @param _despawn The despawning animation.
     */
    public AnimationData(@Nullable Animation _spawn, @Nullable Animation _despawn) {
        spawn         = _spawn;
        despawn       = _despawn;
    }
}
