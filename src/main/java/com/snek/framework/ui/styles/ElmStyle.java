package com.snek.framework.ui.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;

import net.minecraft.entity.decoration.DisplayEntity.BillboardMode;








public class ElmStyle {
    private @NotNull  Transform     transform;          // The current transformation
    private @NotNull  Float         viewRange;          // The view range. 1.0f = 64 blocks
    private @NotNull  BillboardMode billboardMode;      // The billboard mode. Defines how the rendered entity is rotates relatively to the player's camera

    private @Nullable Animation     spawnAnimation;     // The spawning animation. Played when the entity is spawned into the world
    private @Nullable Animation     despawnAnimation;   // The despawning animation. Played before the entity is removed from the world

    public static final float S_SCALE  = 1.02f;         // The scale applied by the spawning animation
    public static final float S_HEIGHT = 0.05f;         // The Y translation applied by the spawning animation
    public static final int   S_TIME   = 4;             // Spawn   time. Measured in ticks
    public static final int   D_TIME   = 8;             // Despawn time. Measured in ticks




    /**
     * Creates a new default ElmStyle.
     */
    public ElmStyle() {
        transform        = new Transform().scale(0.5f);
        viewRange        = 0.3f;
        billboardMode    = BillboardMode.FIXED;

        spawnAnimation   = null;
        despawnAnimation = null;
    }




    public ElmStyle setTransform    (Transform     _transform    ) { transform     = _transform;     return this; }
    public ElmStyle setViewRange    (float         _viewRange    ) { viewRange     = _viewRange;     return this; }
    public ElmStyle setBillboardMode(BillboardMode _billboardMode) { billboardMode = _billboardMode; return this; }

    public Transform     getTransform    () { return transform;     }
    public float         getViewRange    () { return viewRange;     }
    public BillboardMode getBillboardMode() { return billboardMode; }




    public ElmStyle  setSpawnAnimation  (@Nullable Animation _animation) { spawnAnimation   = _animation; return this; }
    public ElmStyle  setDespawnAnimation(@Nullable Animation _animation) { despawnAnimation = _animation; return this; }

    public @Nullable Animation getSpawnAnimation  () { return spawnAnimation;   }
    public @Nullable Animation getDespawnAnimation() { return despawnAnimation; }
}
