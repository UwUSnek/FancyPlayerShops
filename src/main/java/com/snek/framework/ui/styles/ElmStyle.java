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
    private @NotNull  Flagged<Transform>     transform;
    private @Nullable Flagged<Animation>     spawnAnimation;
    private @Nullable Flagged<Animation>     despawnAnimation;

    private @NotNull  Flagged<Float>         viewRange;
    private @NotNull  Flagged<BillboardMode> billboardMode;


    private static final float S_SCALE  = 1.02f;
    private static final float S_HEIGHT = 0.05f;
    private static final int   S_TIME   = 4; // Measured in ticks. MUST BE EVEN
    private static final int   D_TIME   = 8; // Measured in ticks. MUST BE EVEN




    /**
     * Creates a new default ElmStyle.
     */
    public ElmStyle() {
        transform        = Flagged.from(new Transform().scale(0.5f));
        viewRange        = Flagged.from(0.3f);
        billboardMode    = Flagged.from(BillboardMode.FIXED);

        spawnAnimation   = Flagged.from(new Animation(new TargetTransition(new Transform().moveY(S_HEIGHT).scale(S_SCALE), S_TIME, Easings.linear))); //TODO use better easing
        despawnAnimation = Flagged.from(new Animation(new TargetTransition(transform.get(),                                D_TIME, Easings.linear))); //TODO use better easing
    }




    /**
     * Flushes changeable style values to the entity.
     * This does not start an interpolation.
     * @param e The entity.
     */
    public void flushStyle(CustomDisplay e) {
        if(transform    .isFlagged()) e.setTransformation(transform    .get().get()); transform    .unflag();
        if(viewRange    .isFlagged()) e.setViewRange     (viewRange    .get()      ); viewRange    .unflag();
        if(billboardMode.isFlagged()) e.setBillboardMode (billboardMode.get()      ); billboardMode.unflag();
    }




    public ElmStyle setTransform    (Transform     _transform    ) { transform    .set(_transform    ); return this;}
    public ElmStyle setViewRange    (float         _viewRange    ) { viewRange    .set(_viewRange    ); return this;}
    public ElmStyle setBillboardMode(BillboardMode _billboardMode) { billboardMode.set(_billboardMode); return this;}

    public Animation editSpawnAnimation  () { return spawnAnimation  .edit(); }
    public Animation editDespawnAnimation() { return despawnAnimation.edit(); }
}
