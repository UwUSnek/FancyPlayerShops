package com.snek.framework.ui;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

import com.snek.framework.custom_displays.CustomDisplay;
import com.snek.framework.custom_displays.CustomItemDisplay;
import com.snek.framework.custom_displays.CustomTextDisplay;
import com.snek.framework.data_types.Flagged;
import com.snek.framework.ui.styles.ElmStyle;
import com.snek.framework.ui.styles.ItemElmStyle;

import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;








public class ItemElm extends Elm {

    private @NotNull Flagged<ItemStack> item;




    protected ItemElm(@NotNull ServerWorld _world, @NotNull CustomDisplay _entity, @NotNull ElmStyle _style) {
        super(_world, _entity, _style);

        item = Flagged.from(((ItemElmStyle)style).getItem());
    }

    public ItemElm(@NotNull ServerWorld _world){
        this(_world, new CustomTextDisplay(_world), new ItemElmStyle());
    }

    protected ItemElm(@NotNull ServerWorld _world, @NotNull ElmStyle _style) {
        this(_world, new CustomItemDisplay(_world), _style);
    }




    /**
     * Flushes changeable style values to the entity.
     * This does not start an interpolation.
     */
    @Override
    public void flushStyle() {
        super.flushStyle();
        CustomItemDisplay e2 = (CustomItemDisplay)entity;
        if(item.isFlagged()) e2.setItemStack(item.get()); item.unflag();
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
    public void tick(){
        super.tick();
    }
}
