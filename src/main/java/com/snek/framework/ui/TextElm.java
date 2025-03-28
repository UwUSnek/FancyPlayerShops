package com.snek.framework.ui;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector4i;

import com.snek.framework.custom_displays.CustomDisplay;
import com.snek.framework.custom_displays.CustomTextDisplay;
import com.snek.framework.data_types.Flagged;
import com.snek.framework.ui.styles.ElmStyle;
import com.snek.framework.ui.styles.TextElmStyle;

import net.minecraft.entity.decoration.DisplayEntity.TextDisplayEntity.TextAlignment;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;








public class TextElm extends Elm {

    private @NotNull Flagged<TextAlignment> alignment;
    private @NotNull Flagged<Vector4i>      background;
    private @NotNull Flagged<Text>          text;
    private @NotNull Flagged<Integer>       textOpacity;




    public TextElm(@NotNull ServerWorld _world){
        super(_world, new CustomTextDisplay(_world), new TextElmStyle());
    }
    protected TextElm(@NotNull ServerWorld _world, @NotNull ElmStyle _style) {
        super(_world, new CustomTextDisplay(_world), _style);
    }
    protected TextElm(@NotNull ServerWorld _world, @NotNull CustomDisplay _entity, @NotNull ElmStyle _style) {
        super(_world, _entity, _style);
    }




    /**
     * Flushes changeable style values to the entity.
     * This does not start an interpolation.
     */
    @Override
    public void flushStyle() {
        super.flushStyle();
        CustomTextDisplay e2 = (CustomTextDisplay)entity;
        if(alignment  .isFlagged()) e2.setAlignment  (alignment  .get()); alignment  .unflag();
        if(background .isFlagged()) e2.setBackground (background .get()); background .unflag();
        if(text       .isFlagged()) e2.setText       (text       .get()); text       .unflag();
        if(textOpacity.isFlagged()) e2.setTextOpacity(textOpacity.get()); textOpacity.unflag();
    }




    @Override
    public void spawn() {
        textOpacity.set(128);
        background .set(new Vector4i(0));

        super.spawn();
        textOpacity.set(((TextElmStyle)style).getTextOpacity());
        background .set(((TextElmStyle)style).getBackground ());
    }


    @Override
    public void despawn() {
        textOpacity.set(128);
        background .set(new Vector4i(0));
        super.despawn();
    }


    @Override
    public void tick(){
        super.tick();
    }
}
