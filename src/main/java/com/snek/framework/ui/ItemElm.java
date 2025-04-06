package com.snek.framework.ui;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector3f;

import com.snek.framework.data_types.containers.Flagged;
import com.snek.framework.data_types.displays.CustomDisplay;
import com.snek.framework.data_types.displays.CustomItemDisplay;
import com.snek.framework.data_types.displays.CustomTextDisplay;
import com.snek.framework.ui.styles.ElmStyle;
import com.snek.framework.ui.styles.ItemElmStyle;

import net.minecraft.entity.decoration.DisplayEntity.BillboardMode;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;








public class ItemElm extends Elm {

    public @NotNull Flagged<ItemStack> item;




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
        if(item.isFlagged()) { e2.setItemStack(item.get()); item.unflag(); }
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




    @Override
    public boolean checkIntersection(PlayerEntity player) {
        if(!isSpawned) return false;


        // Calculate the world coordinates of the display's origin. //! Left rotation and scale are ignored as they doesn't affect this
        Vector3f origin =
            entity.getPosCopy()
            .add   (transform.get().getPos())
            .rotate(transform.get().getRrot())
        ;


        //TODO
        return false;
    }
}
