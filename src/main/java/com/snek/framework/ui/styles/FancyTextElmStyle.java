package com.snek.framework.ui.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector4i;

import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.transitions.TextAdditiveTransition;
import com.snek.framework.data_types.containers.Flagged;
import com.snek.framework.utils.Easings;
import com.snek.framework.utils.Txt;

import net.minecraft.entity.decoration.DisplayEntity.TextDisplayEntity.TextAlignment;
import net.minecraft.text.Text;




public class FancyTextElmStyle extends TextElmStyle {
    private Flagged<Vector4i> background = null;


    /**
     * Creates a new default TextElmStyle.
     */
    public FancyTextElmStyle() {
        super();
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
