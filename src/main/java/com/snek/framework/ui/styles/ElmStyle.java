package com.snek.framework.ui.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.containers.Flagged;
import com.snek.framework.data_types.ui.AlignmentX;
import com.snek.framework.data_types.ui.AlignmentY;

import net.minecraft.entity.decoration.DisplayEntity.BillboardMode;








public class ElmStyle {
    public static final float S_SCALE  = 1.02f;                 // The scale applied by the spawning animation
    public static final float S_HEIGHT = 0.05f;                 // The Y translation applied by the spawning animation
    public static final int   S_TIME   = 4;                     // Spawn   time. Measured in ticks
    public static final int   D_TIME   = 8;                     // Despawn time. Measured in ticks


    private Flagged<Transform>     transform        = null;     // The current transformation
    private Flagged<Float>         viewRange        = null;     // The view range. 1.0f = 64 blocks
    private Flagged<BillboardMode> billboardMode    = null;     // The billboard mode. Defines how the rendered entity is rotates relatively to the player's camera

    // private Flagged<AlignmentX>    alignmentX       = null;     // The horizontal alignment of the element
    // private Flagged<AlignmentY>    alignmentY       = null;     // The vertical alignment of the element
    private Flagged<Animation>     spawnAnimation   = null;     // The spawning animation. Played when the entity is spawned into the world
    private Flagged<Animation>     despawnAnimation = null;     // The despawning animation. Played before the entity is removed from the world





    /**
     * Creates a new default ElmStyle.
     */
    public ElmStyle() {
        resetTransform();
        resetViewRange();
        resetBillboardMode();
        // resetAlignmentX();
        // resetAlignmentY();
        resetSpawnAnimation();
        resetDespawnAnimation();
    }





    public void resetTransform       () { transform        = Flagged.from(new Transform().scale(0.5f)); }
    public void resetViewRange       () { viewRange        = Flagged.from(0.3f                       ); }
    public void resetBillboardMode   () { billboardMode    = Flagged.from(BillboardMode.FIXED        ); }
    // public void resetAlignmentX      () { alignmentX       = Flagged.from(AlignmentX.NONE            ); }
    // public void resetAlignmentY      () { alignmentY       = Flagged.from(AlignmentY.NONE            ); }
    public void resetSpawnAnimation  () { spawnAnimation   = Flagged.from(null                       ); }
    public void resetDespawnAnimation() { despawnAnimation = Flagged.from(null                       ); }


    public void setTransform       (@NotNull  Transform     _transform    ) { transform       .set(_transform    ); }
    public void setViewRange       (          float         _viewRange    ) { viewRange       .set(_viewRange    ); }
    public void setBillboardMode   (@NotNull  BillboardMode _billboardMode) { billboardMode   .set(_billboardMode); }
    // public void setAlignmentX      (@NotNull  AlignmentX    _alignment    ) { alignmentX      .set(_alignment    ); }
    // public void setAlignmentY      (@NotNull  AlignmentY    _alignment    ) { alignmentY      .set(_alignment    ); }
    public void setSpawnAnimation  (@Nullable Animation     _animation    ) { spawnAnimation  .set(_animation    ); }
    public void setDespawnAnimation(@Nullable Animation     _animation    ) { despawnAnimation.set(_animation    ); }


    public @NotNull  Transform     getTransform       () { return transform       .get(); }
    public           float         getViewRange       () { return viewRange       .get(); }
    public @NotNull  BillboardMode getBillboardMode   () { return billboardMode   .get(); }
    // public @NotNull  AlignmentX    getAlignmentX      () { return alignmentX      .get(); }
    // public @NotNull  AlignmentY    getAlignmentY      () { return alignmentY      .get(); }
    public @Nullable Animation     getSpawnAnimation  () { return spawnAnimation  .get(); }
    public @Nullable Animation     getDespawnAnimation() { return despawnAnimation.get(); }

    public @NotNull  Flagged<Transform>     getFlaggedTransform       () { return transform;        }
    public           Flagged<Float>         getFlaggedViewRange       () { return viewRange;        }
    public @NotNull  Flagged<BillboardMode> getFlaggedBillboardMode   () { return billboardMode;    }
    // public @NotNull  Flagged<AlignmentX>    getFlaggedAlignmentX      () { return alignmentX;       }
    // public @NotNull  Flagged<AlignmentY>    getFlaggedAlignmentY      () { return alignmentY;       }
    public @Nullable Flagged<Animation>     getFlaggedSpawnAnimation  () { return spawnAnimation;   }
    public @Nullable Flagged<Animation>     getFlaggedDespawnAnimation() { return despawnAnimation; }


    public @NotNull  Transform     editTransform       () { return transform       .edit(); }
    //!                            editViewRange       Primitive types cannot be edited
    //!                            editBillboardMode   Primitive types cannot be edited
    // //                            editAlignmentX      Primitive types cannot be edited
    // //                            editAlignmentY      Primitive types cannot be edited
    public @Nullable Animation     editSpawnAnimation  () { return spawnAnimation  .edit(); }
    public @Nullable Animation     editDespawnAnimation() { return despawnAnimation.edit(); }
}
