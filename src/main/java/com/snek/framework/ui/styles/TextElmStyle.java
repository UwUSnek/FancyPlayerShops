package com.snek.framework.ui.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector4i;

import com.snek.framework.utils.Txt;

import net.minecraft.entity.decoration.DisplayEntity.TextDisplayEntity.TextAlignment;
import net.minecraft.text.Text;








public class TextElmStyle extends ElmStyle {
    private @NotNull TextAlignment alignment;
    private @NotNull Vector4i      background;
    private @NotNull Text          text;
    private @NotNull int           textOpacity;




    /**
     * Creates a new default TextElmStyle.
     */
    public TextElmStyle() {
        super();
        alignment   = TextAlignment.CENTER;
        background  = new Vector4i(200, 20, 20, 20);
        text        = new Txt("").get();
        textOpacity = 255;
    }




    public TextElmStyle setAlignment  (TextAlignment _alignment  ) { alignment   = _alignment;   return this; }
    public TextElmStyle setBackground (Vector4i      _background ) { background  = _background;  return this; }
    public TextElmStyle setText       (Text          _text       ) { text        = _text;        return this; }
    public TextElmStyle setTextOpacity(int           _textOpacity) { textOpacity = _textOpacity; return this; }

    public TextAlignment getAlignment  () { return alignment;   }
    public Vector4i      getBackground () { return background;  }
    public Text          getText       () { return text;        }
    public int           getTextOpacity() { return textOpacity; }
}
