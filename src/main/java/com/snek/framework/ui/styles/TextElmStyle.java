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








public class TextElmStyle extends ElmStyle {
    private Flagged<Vector4i>      background    = null;
    private Flagged<Text>          text          = null;
    private Flagged<TextAlignment> textAlignment = null;
    private Flagged<Integer>       textOpacity   = null;

    @Override
    public @NotNull Transform getDefaultTransform () {
        return new Transform().scale(0.35f);
    }




    /**
     * Creates a new default TextElmStyle.
     */
    public TextElmStyle() {

        // Set values
        super();
        resetBackground();
        resetText();
        resetTextAlignment();
        resetTextOpacity();


        // Set default spawning animation
        setSpawnAnimation(new Animation(new TextAdditiveTransition(new Transform(),
            ElmStyle.S_TIME,
            Easings.sineOut,
            background.get(),
            255
        )));


        // Set default despawning animation
        setDespawnAnimation(new Animation(new TextAdditiveTransition(new Transform(),
            ElmStyle.D_TIME,
            Easings.sineOut,
            new Vector4i(0),
            0
        )));
    }




    public @NotNull Vector4i      getDefaultBackground   () { return new Vector4i(230, 37, 40, 40); }
    public @NotNull Text          getDefaultText         () { return new Txt("").get()            ; }
    public @NotNull TextAlignment getDefaultTextAlignment() { return TextAlignment.CENTER         ; }
    public          int           getDefaultTextOpacity  () { return 255                          ; }


    public void resetBackground   () { background    = Flagged.from(getDefaultBackground()   ); }
    public void resetText         () { text          = Flagged.from(getDefaultText()         ); }
    public void resetTextAlignment() { textAlignment = Flagged.from(getDefaultTextAlignment()); }
    public void resetTextOpacity  () { textOpacity   = Flagged.from(getDefaultTextOpacity()  ); }


    public void setBackground   (@NotNull Vector4i      _background   ) { background   .set(_background   ); }
    public void setText         (@NotNull Text          _text         ) { text         .set(_text         ); }
    public void setTextAlignment(@NotNull TextAlignment _textAlignment) { textAlignment.set(_textAlignment); }
    public void setTextOpacity  (         int           _textOpacity  ) { textOpacity  .set(_textOpacity  ); }


    public @NotNull Flagged<Vector4i>      getFlaggedBackground   () { return background;    }
    public @NotNull Flagged<Text>          getFlaggedText         () { return text;          }
    public @NotNull Flagged<TextAlignment> getFlaggedTextAlignment() { return textAlignment; }
    public @NotNull Flagged<Integer>       getFlaggedTextOpacity  () { return textOpacity;   }


    public @NotNull Vector4i      getBackground   () { return background   .get(); }
    public @NotNull Text          getText         () { return text         .get(); }
    public @NotNull TextAlignment getTextAlignment() { return textAlignment.get(); }
    public          int           getTextOpacity  () { return textOpacity  .get(); }


    public @NotNull Vector4i editBackground    () { return background   .edit(); }
    public @NotNull Text     editText          () { return text         .edit(); }
    //!                      editTextAlignment Primitive types cannot be edited
    //!                      editTextOpacity   Primitive types cannot be edited
}
