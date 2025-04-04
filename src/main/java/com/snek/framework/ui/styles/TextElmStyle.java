package com.snek.framework.ui.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector4i;

import com.snek.framework.data_types.IndexedArrayDeque;
import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
// import com.snek.framework.data_types.animations.TextTransform;
import com.snek.framework.data_types.animations.transitions.AdditiveTransition;
import com.snek.framework.data_types.animations.transitions.TargetTransition;
import com.snek.framework.data_types.animations.transitions.TextAdditiveTransition;
import com.snek.framework.data_types.animations.transitions.TextTargetTransition;
import com.snek.framework.data_types.animations.transitions.Transition;
import com.snek.framework.utils.Txt;

import net.minecraft.entity.decoration.DisplayEntity.TextDisplayEntity.TextAlignment;
import net.minecraft.text.Text;








public class TextElmStyle extends ElmStyle {

    public static final Vector4i S_BG    = new Vector4i(200, 20, 20, 20);
    public static final int      S_ALPHA = 255;

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
        background  = new Vector4i(0);
        text        = new Txt("").get();
        textOpacity = 0;


        // Adjust spawning animation //! and hope it only has 1 element
        //! Changed made by subclasses aren't aknowledged until this function ends, so the animation is never null
        Animation  _spawnAnimation = getSpawnAnimation();
        setSpawnAnimation(new Animation(new TextAdditiveTransition(
            _spawnAnimation.getTransitions().get(0),
            S_BG,
            S_ALPHA
        )));


        // Adjust despawning animation //! and hope it only has 1 element
        //! Changed made by subclasses aren't aknowledged until this function ends, so the animation is never null
        Animation _despawnAnimation = getDespawnAnimation();
        setDespawnAnimation(new Animation(new TextTargetTransition(
            _despawnAnimation.getTransitions().get(0),
            background,
            textOpacity
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
