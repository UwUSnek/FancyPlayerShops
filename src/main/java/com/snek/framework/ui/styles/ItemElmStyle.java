package com.snek.framework.ui.styles;

import org.jetbrains.annotations.NotNull;

import com.snek.framework.data_types.containers.Flagged;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;








public class ItemElmStyle extends ElmStyle {
    private Flagged<ItemStack> item = null;




    /**
     * Creates a new default TextElmStyle.
     */
    public ItemElmStyle() {
        super();
        resetItem();
    }




    public @NotNull ItemStack getDefaultItem() { return Items.AIR.getDefaultStack(); }
    public void resetItem() { item = Flagged.from(getDefaultItem()); }
    public void setItem(@NotNull ItemStack _item) { item.set(_item); }
    public @NotNull Flagged<ItemStack> getFlaggedItem() { return item; }
    public @NotNull ItemStack getItem() { return item.get(); }
    public @NotNull ItemStack editItem() { return item.edit(); }
}
