package com.snek.framework.ui.styles;

import org.jetbrains.annotations.NotNull;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;








public class ItemElmStyle extends ElmStyle {
    private @NotNull ItemStack item;




    /**
     * Creates a new default TextElmStyle.
     */
    public ItemElmStyle() {
        super();
        item = Items.AIR.getDefaultStack();
    }




    public ItemElmStyle setItem(ItemStack _item) { item = _item; return this; }

    public ItemStack getItem() { return item; }
}
