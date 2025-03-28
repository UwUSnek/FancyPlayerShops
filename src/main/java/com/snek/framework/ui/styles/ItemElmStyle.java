package com.snek.framework.ui.styles;

import org.jetbrains.annotations.NotNull;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;








public class ItemElmStyle extends ElmStyle {
    private @NotNull ItemStack item;

    // private @NotNull Flagged<Vector4i>      despawnBackground;
    // private @NotNull Flagged<Integer>       despawnTextOpacity;




    /**
     * Creates a new default TextElmStyle.
     */
    public ItemElmStyle() {
        super();
        item   = Items.BARRIER.getDefaultStack();

        // despawnBackground  = Flagged.from(new Vector4i(0,  0, 0, 0));
        // despawnTextOpacity = Flagged.from(128);
    }




    // /**
    //  * Flushes changeable style values to the entity.
    //  * This does not start an interpolation.
    //  * @param e The entity.
    //  */
    // @Override
    // public void flushStyle(CustomDisplay e) {
    //     super.flushStyle(e);
    //     CustomTextDisplay e2 = (CustomTextDisplay)e;
    //     if(alignment  .isFlagged()) e2.setAlignment  (alignment  .get()); alignment  .unflag();
    //     if(background .isFlagged()) e2.setBackground (background .get()); background .unflag();
    //     if(text       .isFlagged()) e2.setText       (text       .get()); text       .unflag();
    //     if(textOpacity.isFlagged()) e2.setTextOpacity(textOpacity.get()); textOpacity.unflag();
    // }




    public ItemElmStyle setItem(ItemStack _item) { item = _item; return this; }

    public ItemStack getItem() { return item; }

    // public TextElmStyle setDespawnTextOpacity(int      _despawnTextOpacity) { despawnTextOpacity.set(_despawnTextOpacity); return this; }
    // public TextElmStyle setDespawnBackground (Vector4i _despawnBackground ) { despawnBackground .set(_despawnBackground ); return this; }
}
