package com.snek.fancyplayershops.implementations.ui.edit;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.Shop;
import com.snek.fancyplayershops.implementations.ui.ShopButton;
import com.snek.fancyplayershops.implementations.ui.ShopTextElm;
import com.snek.framework.ui.styles.__internal_TextElmStyle;
import com.snek.framework.utils.MinecraftUtils;
import com.snek.framework.utils.Txt;

import net.minecraft.item.Items;








public class EditUiTitle extends ShopTextElm {

    public EditUiTitle(@NotNull Shop _shop) {
        super(_shop, 0.5f, ShopTextElm.LINE_H);
        updateDisplay();
    }




    public void updateDisplay() {
        ((__internal_TextElmStyle)text.style).setText(new Txt()
            .cat(new Txt(" Editing ").white())
            .cat(new Txt(shop.getItem().getItem() == Items.AIR ? new Txt("an empty shop").white().get() : MinecraftUtils.getItemName(shop.getItem())))
            .cat("... ")
        .get());
        flushStyle();
    }
}