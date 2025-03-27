package com.snek.framework.ui;

import org.jetbrains.annotations.NotNull;

import com.snek.framework.custom_displays.CustomDisplay;
import com.snek.framework.custom_displays.CustomTextDisplay;
import com.snek.framework.ui.styles.ButtonElmStyle;
import com.snek.framework.ui.styles.TextElmStyle;

import net.minecraft.server.world.ServerWorld;








public class ButtonElm extends TextElm {
    //TODO action




    public ButtonElm(ServerWorld _world){
        super(_world, new ButtonElmStyle());
    }




    /**
     * Flushes changeable style values to the entity.
     * This does not start an interpolation.
     */
    @Override
    public void flushStyle() {
        super.flushStyle();
    }




    @Override
    public void spawn() {
        super.spawn();
    }


    @Override
    public void despawn() {
        super.despawn();
    }


    @Override
    public void tick(){
        super.tick();
    }
}
