package com.snek.framework.ui.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.framework.data_types.containers.Flagged;
import com.snek.framework.data_types.displays.CustomDisplay;
import com.snek.framework.data_types.displays.CustomItemDisplay;
import com.snek.framework.data_types.displays.CustomTextDisplay;
import com.snek.framework.ui.styles.ElmStyle;
import com.snek.framework.ui.styles.ItemElmStyle;

import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;








/**
 * An element that can display items.
 */
public class ItemElm extends Elm {
    private ItemElmStyle getStyle() { return (ItemElmStyle)style; }




    /**
     * Creates a new ItemElm using an existing entity and a custom style.
     * @param _world The world in which to place the element.
     * @param _entity The display entity.
     * @param _style The custom style.
     */
    protected ItemElm(@NotNull ServerWorld _world, @NotNull CustomDisplay _entity, @NotNull ElmStyle _style) {
        super(_world, _entity, _style);
    }


    /**
     * Creates a new ItemElm using a custom style.
     * @param _world The world in which to place the element.
     * @param _style The custom style.
     */
    protected ItemElm(@NotNull ServerWorld _world, @NotNull ElmStyle _style) {
        this(_world, new CustomItemDisplay(_world), _style);
    }


    /**
     * Creates a new ItemElm using the default style.
     * @param _world The world in which to place the element.
     */
    public ItemElm(@NotNull ServerWorld _world){
        this(_world, new CustomTextDisplay(_world), new ItemElmStyle());
    }




    @Override
    public void flushStyle() {
        super.flushStyle();
        CustomItemDisplay e2 = (CustomItemDisplay)entity;
        { Flagged<ItemStack> f = getStyle().getFlaggedItem(); if(f.isFlagged()) { e2.setItemStack(f.get()); f.unflag(); }}
    }
}
