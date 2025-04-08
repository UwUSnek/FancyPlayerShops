package com.snek.framework.ui.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector4i;

import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.transitions.TextAdditiveTransition;
import com.snek.framework.utils.Easings;
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

        // Set values
        super();
        alignment   = TextAlignment.CENTER;
        background  = new Vector4i(200, 20, 20, 20);
        text        = new Txt("").get();
        textOpacity = 255;


        // Set default spawning animation
        setSpawnAnimation(new Animation(new TextAdditiveTransition(new Transform(),
            ElmStyle.S_TIME,
            Easings.sineOut,
            background,
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




    public TextElmStyle setAlignment  (TextAlignment _alignment  ) { alignment   = _alignment;   return this; }
    public TextElmStyle setBackground (Vector4i      _background ) { background  = _background;  return this; }
    public TextElmStyle setText       (Text          _text       ) { text        = _text;        return this; }
    public TextElmStyle setTextOpacity(int           _textOpacity) { textOpacity = _textOpacity; return this; }

    public TextAlignment getAlignment  () { return alignment;   }
    public Vector4i      getBackground () { return background;  }
    public Text          getText       () { return text;        }
    public int           getTextOpacity() { return textOpacity; }
}
