package com.snek.framework.ui;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector4i;

import com.snek.framework.custom_displays.CustomDisplay;
import com.snek.framework.custom_displays.CustomTextDisplay;
import com.snek.framework.data_types.Flagged;
import com.snek.framework.ui.styles.ElmStyle;
import com.snek.framework.ui.styles.TextElmStyle;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;








public class TextElm extends Elm {

    protected @NotNull Flagged<Text>          text;
    protected @NotNull Flagged<Integer>       textOpacity;
    protected @NotNull Flagged<Vector4i>      background;




    protected TextElm(@NotNull ServerWorld _world, @NotNull CustomDisplay _entity, @NotNull ElmStyle _style) {
        super(_world, _entity, _style);

        text        = Flagged.from(((TextElmStyle)style).getText());
        textOpacity = Flagged.from(128);              // Changed on spawn
        background  = Flagged.from(new Vector4i(0));  // Changed on spawn
    }

    protected TextElm(@NotNull ServerWorld _world, @NotNull ElmStyle _style) {
        this(_world, new CustomTextDisplay(_world), _style);
    }

    public TextElm(@NotNull ServerWorld _world){
        this(_world, new CustomTextDisplay(_world), new TextElmStyle());
    }




    /**
     * Flushes changeable style values to the entity.
     * This does not start an interpolation.
     */
    @Override
    public void flushStyle() {
        super.flushStyle();
        CustomTextDisplay e2 = (CustomTextDisplay)entity;
        if(text       .isFlagged()) { e2.setText       (text       .get()); text       .unflag(); }
        if(textOpacity.isFlagged()) { e2.setTextOpacity(textOpacity.get()); textOpacity.unflag(); }
        if(background .isFlagged()) { e2.setBackground (background .get()); background .unflag(); }
    }




    @Override
    public void spawn(Vector3d pos) {
        // textOpacity.set(128);
        // background .set(new Vector4i(0));

        // Set new opacity and background, then spawn the entity
        textOpacity.set(((TextElmStyle)style).getTextOpacity());
        background .set(((TextElmStyle)style).getBackground ());
        super.spawn(pos);
    }


    @Override
    public void despawn() {
        textOpacity.set(128);
        background .set(new Vector4i(0));
        super.despawn();
    }


    @Override
    public boolean tick(){
        return super.tick();
    }
}
