package com.snek.framework.ui.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.framework.custom_displays.CustomDisplay;
import com.snek.framework.data_types.Animation;
import com.snek.framework.data_types.Flagged;
import com.snek.framework.data_types.Transform;
import com.snek.framework.utils.Easings;
import com.snek.framework.data_types.TargetTransition;

import net.minecraft.entity.decoration.DisplayEntity.BillboardMode;








public class ElmStyle {
    private @NotNull  Transform     transform;
    private @NotNull  Float         viewRange;
    private @NotNull  BillboardMode billboardMode;

    private @Nullable Animation     spawnAnimation;
    private @Nullable Animation     despawnAnimation;


    private static final float S_SCALE  = 1.02f;
    private static final float S_HEIGHT = 0.05f;
    private static final int   S_TIME   = 4; // Measured in ticks. MUST BE EVEN
    private static final int   D_TIME   = 8; // Measured in ticks. MUST BE EVEN




    /**
     * Creates a new default ElmStyle.
     */
    public ElmStyle() {
        transform        = new Transform().scale(0.5f);
        viewRange        = 0.3f;
        billboardMode    = BillboardMode.FIXED;

        spawnAnimation   = new Animation(new TargetTransition(new Transform().moveY(S_HEIGHT).scale(S_SCALE), S_TIME, Easings.linear)); //TODO use better easing
        despawnAnimation = new Animation(new TargetTransition(transform,                                      D_TIME, Easings.linear)); //TODO use better easing
    }




    public ElmStyle setTransform    (Transform     _transform    ) { transform     = _transform;     return this; }
    public ElmStyle setViewRange    (float         _viewRange    ) { viewRange     = _viewRange;     return this; }
    public ElmStyle setBillboardMode(BillboardMode _billboardMode) { billboardMode = _billboardMode; return this; }

    public Transform     getTransform    () { return transform;     }
    public float         getViewRange    () { return viewRange;     }
    public BillboardMode getBillboardMode() { return billboardMode; }




    public ElmStyle  setSpawnAnimation  (@Nullable Animation _animation) { spawnAnimation   = _animation; return this; }
    public ElmStyle  setDespawnAnimation(@Nullable Animation _animation) { despawnAnimation = _animation; return this; }

    public Animation getSpawnAnimation  () { return spawnAnimation;   }
    public Animation getDespawnAnimation() { return despawnAnimation; }
}
