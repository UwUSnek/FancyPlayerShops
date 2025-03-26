package com.snek.framework.ui.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.framework.custom_displays.CustomDisplay;
import com.snek.framework.data_types.Animation;
import com.snek.framework.data_types.Flagged;
import com.snek.framework.data_types.Transform;
import com.snek.framework.data_types.Transition;

import net.minecraft.entity.decoration.DisplayEntity.BillboardMode;








public class ElmStyle {
    private @NotNull Flagged<Float>         viewRange;
    private @NotNull Flagged<BillboardMode> billboardMode;


    private static final float S_SCALE  = 1.02f;
    private static final float S_HEIGHT = 0.05f;
    private static final int S_TIME     = 4; // Measured in ticks. MUST BE EVEN
    private static final int D_TIME     = 8; // Measured in ticks. MUST BE EVEN

    private @NotNull  final Transform startingTransform = new Transform().scale(0.5f);
    private @Nullable final Animation spawnAnimation    = new Animation(new Transition(startingTransform.clone().moveY(S_HEIGHT).scale(S_SCALE), S_TIME));
    private @Nullable final Animation despawnAnimation  = new Animation(new Transition(startingTransform, D_TIME));




    /**
     * Creates a new default ElmStyle.
     */
    public ElmStyle() {
        viewRange     = Flagged.from(0.3f);
        billboardMode = Flagged.from(BillboardMode.FIXED);
    }




    /**
     * Flushes changeable style values to the entity.
     * This does not start an interpolation.
     * @param e The entity.
     */
    public void flushStyle(CustomDisplay e) {
        if(viewRange    .isFlagged()) e.setViewRange    (viewRange    .get()); viewRange    .unflag();
        if(billboardMode.isFlagged()) e.setBillboardMode(billboardMode.get()); billboardMode.unflag();
    }




    public void setViewRange    (float         _viewRange    ) { viewRange    .set(_viewRange    ); }
    public void setBillboardMode(BillboardMode _billboardMode) { billboardMode.set(_billboardMode); }
}
