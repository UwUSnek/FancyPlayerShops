package com.snek.framework.ui.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector4i;

import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.transitions.Transition;
import com.snek.framework.data_types.containers.Flagged;
import com.snek.framework.utils.Easings;
import com.snek.framework.utils.Txt;

import net.minecraft.entity.decoration.DisplayEntity.TextDisplayEntity.TextAlignment;
import net.minecraft.text.Text;








public class TextElmStyle extends ElmStyle {
    public static final float DEFAULT_TEXT_SCALE = 0.35f;

    private Flagged<Text>          text          = null;
    private Flagged<TextAlignment> textAlignment = null;
    private Flagged<Integer>       textOpacity   = null;




    @Override
    public @NotNull Transform getDefaultTransform () {
        return new Transform().scale(DEFAULT_TEXT_SCALE);
    }




    /**
     * Creates a new default TextElmStyle.
     */
    public TextElmStyle() {
        super();
    }


    @Override
    public void resetAll(){
        resetText();
        resetTextAlignment();
        resetTextOpacity();
        super.resetAll();
    }




    //TODO split animation between normal and fancy text styles, use new animation type
    @Override
    public Animation getDefaultSpawnAnimation() {
        return new Animation(
            new Transition(ElmStyle.S_TIME, Easings.sineOut)
            .targetOpacity(255)
        );
    }


    @Override
    public Animation getDefaultDespawnAnimation() {
        return new Animation(
            new Transition(ElmStyle.D_TIME, Easings.sineOut)
            .targetOpacity(0)
        );
    }




    public @NotNull Text          getDefaultText         () { return new Txt("").get()            ; }
    public @NotNull TextAlignment getDefaultTextAlignment() { return TextAlignment.CENTER         ; }
    public          int           getDefaultTextOpacity  () { return 255                          ; }


    public void resetText         () { text          = Flagged.from(getDefaultText()         ); }
    public void resetTextAlignment() { textAlignment = Flagged.from(getDefaultTextAlignment()); }
    public void resetTextOpacity  () { textOpacity   = Flagged.from(getDefaultTextOpacity()  ); }


    public void setText         (@NotNull Text          _text         ) { text         .set(_text         ); }
    public void setTextAlignment(@NotNull TextAlignment _textAlignment) { textAlignment.set(_textAlignment); }
    public void setTextOpacity  (         int           _textOpacity  ) { textOpacity  .set(_textOpacity  ); }


    public @NotNull Flagged<Text>          getFlaggedText         () { return text;          }
    public @NotNull Flagged<TextAlignment> getFlaggedTextAlignment() { return textAlignment; }
    public @NotNull Flagged<Integer>       getFlaggedTextOpacity  () { return textOpacity;   }


    public @NotNull Text          getText         () { return text         .get(); }
    public @NotNull TextAlignment getTextAlignment() { return textAlignment.get(); }
    public          int           getTextOpacity  () { return textOpacity  .get(); }


    public @NotNull Text     editText          () { return text         .edit(); }
    //!                      editTextAlignment Primitive types cannot be edited
    //!                      editTextOpacity   Primitive types cannot be edited
}
