package com.snek.framework.ui.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector4i;

import com.snek.framework.custom_displays.CustomDisplay;
import com.snek.framework.custom_displays.CustomTextDisplay;
import com.snek.framework.data_types.Flagged;
import com.snek.framework.utils.Txt;

import net.minecraft.entity.decoration.DisplayEntity.BillboardMode;
import net.minecraft.entity.decoration.DisplayEntity.TextDisplayEntity.TextAlignment;
import net.minecraft.text.Text;








public class TextElmStyle extends ElmStyle {
    private @NotNull TextAlignment alignment;
    private @NotNull Vector4i      background;
    private @NotNull Text          text;
    private @NotNull int           textOpacity;

    // private @NotNull Flagged<Vector4i>      despawnBackground;
    // private @NotNull Flagged<Integer>       despawnTextOpacity;




    /**
     * Creates a new default TextElmStyle.
     */
    public TextElmStyle() {
        super();
        alignment   = TextAlignment.CENTER;
        background  = new Vector4i(200, 20, 20, 20);
        text        = new Txt("").get();
        textOpacity = 256;

        // despawnBackground  = Flagged.from(new Vector4i(0,  0, 0, 0));
        // despawnTextOpacity = Flagged.from(128);
    }




    // /**
    //  * Flushes changeable style values to the entity.
    //  * This does not start an interpolation.
    //  * @param e The entity.
    //  */
    // @Override
    // public void flushStyle(CustomDisplay e) {
    //     super.flushStyle(e);
    //     CustomTextDisplay e2 = (CustomTextDisplay)e;
    //     if(alignment  .isFlagged()) e2.setAlignment  (alignment  .get()); alignment  .unflag();
    //     if(background .isFlagged()) e2.setBackground (background .get()); background .unflag();
    //     if(text       .isFlagged()) e2.setText       (text       .get()); text       .unflag();
    //     if(textOpacity.isFlagged()) e2.setTextOpacity(textOpacity.get()); textOpacity.unflag();
    // }




    public TextElmStyle setAlignment  (TextAlignment _alignment  ) { alignment   = _alignment;   return this; }
    public TextElmStyle setBackground (Vector4i      _background ) { background  = _background;  return this; }
    public TextElmStyle setText       (Text          _text       ) { text        = _text;        return this; }
    public TextElmStyle setTextOpacity(int           _textOpacity) { textOpacity = _textOpacity; return this; }

    public TextAlignment getAlignment  () { return alignment;   }
    public Vector4i      getBackground () { return background;  }
    public Text          getText       () { return text;        }
    public int           getTextOpacity() { return textOpacity; }

    // public TextElmStyle setDespawnTextOpacity(int      _despawnTextOpacity) { despawnTextOpacity.set(_despawnTextOpacity); return this; }
    // public TextElmStyle setDespawnBackground (Vector4i _despawnBackground ) { despawnBackground .set(_despawnBackground ); return this; }
}
