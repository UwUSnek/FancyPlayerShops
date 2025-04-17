package com.snek.fancyplayershops.implementations.ui.edit;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.Shop;
import com.snek.fancyplayershops.implementations.ui.ShopFancyTextElm;
import com.snek.fancyplayershops.implementations.ui.ShopTextElm;
import com.snek.framework.ui.styles.TextElmStyle;
import com.snek.framework.utils.MinecraftUtils;
import com.snek.framework.utils.Txt;

import net.minecraft.item.Items;








/**
 * A text display that shows the name of the shop that is currently being edited.
 */
public class EditUiTitle extends ShopTextElm {

    /**
     * Creates a new EditUiTitle.
     * @param _shop The target shop.
     */
    public EditUiTitle(@NotNull Shop _shop) {
        super(_shop, 0.5f, ShopFancyTextElm.LINE_H);
        updateDisplay();
    }


    /**
     * Updates the displayed text, reading data from the target shop.
     */
    public void updateDisplay() {
        ((TextElmStyle)style).setText(new Txt()
            .cat(new Txt("Editing ").white())
            .cat(new Txt(shop.getItem().getItem() == Items.AIR ? new Txt("an empty shop").white().get() : MinecraftUtils.getItemName(shop.getItem())))
            .cat("...")
        .get());
        flushStyle();
    }
}