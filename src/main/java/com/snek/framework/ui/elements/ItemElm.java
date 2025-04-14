package com.snek.framework.ui.elements;

import org.jetbrains.annotations.NotNull;
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
    private ItemElmStyle getStyle() { return (ItemElmStyle)style; }




    protected ItemElm(@NotNull ServerWorld _world, @NotNull CustomDisplay _entity, @NotNull ElmStyle _style) {
        super(_world, _entity, _style);
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
        { Flagged<ItemStack> f = getStyle().getFlaggedItem(); if(f.isFlagged()) { e2.setItemStack(f.get()); f.unflag(); }}
    }




    // @Override
    // public boolean checkIntersection(PlayerEntity player) {
    //     if(!isSpawned) return false;


    //     // Calculate the world coordinates of the display's origin. //! Left rotation and scale are ignored as they doesn't affect this
    //     Vector3f origin =
    //         entity.getPosCopy()
    //         .add   (style.getTransform().getPos())
    //         .rotate(style.getTransform().getGlobalRot())
    //     ;


    //     //TODO
    //     return false;
    // }
}
