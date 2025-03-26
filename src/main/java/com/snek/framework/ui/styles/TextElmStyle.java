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
    private @NotNull Flagged<TextAlignment> alignment;
    private @NotNull Flagged<Vector4i>      background;
    private @NotNull Flagged<Text>          text;
    private @NotNull Flagged<Integer>       textOpacity;

    private @NotNull Flagged<Vector4i>      despawnBackground;
    private @NotNull Flagged<Integer>       despawnTextOpacity;




    /**
     * Creates a new default TextElmStyle.
     */
    public TextElmStyle() {
        super();
        alignment   = Flagged.from(TextAlignment.CENTER);
        background  = Flagged.from(new Vector4i(200, 20, 20, 20));
        text        = Flagged.from(new Txt("").get());
        textOpacity = Flagged.from(256);

        despawnBackground  = Flagged.from(new Vector4i(0,  0, 0, 0));
        despawnTextOpacity = Flagged.from(128);
    }




    /**
     * Flushes changeable style values to the entity.
     * This does not start an interpolation.
     * @param e The entity.
     */
    public void flushStyle(CustomTextDisplay e) {
        super.flushStyle(e);
        if(alignment  .isFlagged()) e.setAlignment  (alignment  .get()); alignment  .unflag();
        if(background .isFlagged()) e.setBackground (background .get()); background .unflag();
        if(text       .isFlagged()) e.setText       (text       .get()); text       .unflag();
        if(textOpacity.isFlagged()) e.setTextOpacity(textOpacity.get()); textOpacity.unflag();
    }




    public TextElmStyle setAlignment  (TextAlignment _alignment  ) { alignment  .set(_alignment  ); return this; }
    public TextElmStyle setBackground (Vector4i      _background ) { background .set(_background ); return this; }
    public TextElmStyle setText       (Text          _text       ) { text       .set(_text       ); return this; }
    public TextElmStyle setTextOpacity(int           _textOpacity) { textOpacity.set(_textOpacity); return this; }

    public TextElmStyle setDespawnTextOpacity(int      _despawnTextOpacity) { despawnTextOpacity.set(_despawnTextOpacity); return this; }
    public TextElmStyle setDespawnBackground (Vector4i _despawnBackground ) { despawnBackground .set(_despawnBackground ); return this; }
}
