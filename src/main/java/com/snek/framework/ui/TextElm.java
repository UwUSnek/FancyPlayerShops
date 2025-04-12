package com.snek.framework.ui;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector4i;

import com.snek.framework.data_types.displays.CustomDisplay;
import com.snek.framework.data_types.displays.CustomTextDisplay;
import com.snek.framework.ui.styles.ElmStyle;
import com.snek.framework.ui.styles.PanelElmStyle;

import net.minecraft.server.world.ServerWorld;

public class TextElm extends PanelElm {
    public __internal_TextElm text;
    // private TextElmStyle getStyle() { return (TextElmStyle)style; }

    // This value identifies the amount of rendered text pixels that fit in a minecraft block
    public static final int TEXT_PIXEL_BLOCK_RATIO = 40;




    protected TextElm(@NotNull ServerWorld _world, @NotNull CustomDisplay _entity, @NotNull ElmStyle _style) {

        // Create element and background element
        super(_world, _entity, _style);
        text = (__internal_TextElm)addChild(new __internal_TextElm(_world));

        // // Copy background color to background element, then make the background transparent
        // ((PanelElmStyle)style).setColor(((TextElmStyle)text.style).getBackground());
        // ((TextElmStyle)text.style).setBackground(new Vector4i(0, 0, 0, 0));
    }


    protected TextElm(@NotNull ServerWorld _world, @NotNull ElmStyle _style) {
        this(_world, new CustomTextDisplay(_world), _style);
    }

    public TextElm(@NotNull ServerWorld _world){
        this(_world, new CustomTextDisplay(_world), new PanelElmStyle());
    }



    // @Override
    // public void flushStyle(){
        // ((TextElmStyle)text.style).setBackground(new Vector4i(0, 0, 0, 0));
        // text.flushStyle();
        // super.flushStyle();
    // }
}