package com.snek.framework.ui;

import org.joml.Vector3d;

import com.snek.framework.ui.styles.ButtonElmStyle;

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
    public void spawn(Vector3d pos) {
        super.spawn(pos);
    }


    @Override
    public void despawn() {
        super.despawn();
    }


    @Override
    public boolean tick(){
        return super.tick();
    }
}
