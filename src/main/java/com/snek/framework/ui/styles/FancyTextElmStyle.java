package com.snek.framework.ui.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector4i;

import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.data_types.containers.Flagged;
import com.snek.framework.utils.Easings;








public class FancyTextElmStyle extends TextElmStyle {
    private Flagged<Vector4i> background = null;


    /**
     * Creates a new default FancyTextElmStyle.
     */
    public FancyTextElmStyle() {
        super();
    }


    @Override
    public Animation getDefaultPrimerAnimation() {
        return new Animation(
            new Transition(ElmStyle.D_TIME, Easings.sineOut)
            .targetBackground(new Vector4i(0))
            .targetOpacity(0)
        );
    }


    @Override
    public Animation getDefaultSpawnAnimation() {
        return new Animation(
            new Transition(ElmStyle.S_TIME, Easings.sineOut)
            .targetBackground(background.get())
            .targetOpacity(255)
        );
    }


    @Override
    public Animation getDefaultDespawnAnimation() {
        return new Animation(
            new Transition(ElmStyle.D_TIME, Easings.sineOut)
            .targetBackground(new Vector4i(0))
            .targetOpacity(0)
        );
    }


    @Override
    public @NotNull Transform getDefaultTransform () {
        return new Transform();
    }


    @Override
    public void resetAll(){
        resetBackground();
        super.resetAll();
    }




    public @NotNull Vector4i getDefaultBackground () { return new Vector4i(130, 2, 20, 20); }
    public void resetBackground () { background = Flagged.from(getDefaultBackground() ); }
    public void setBackground (@NotNull Vector4i _background ) { background .set(_background ); }
    public @NotNull Flagged<Vector4i> getFlaggedBackground () { return background; }
    public @NotNull Vector4i getBackground () { return background .get(); }
    public @NotNull Vector4i editBackground () { return background .edit(); }
}
