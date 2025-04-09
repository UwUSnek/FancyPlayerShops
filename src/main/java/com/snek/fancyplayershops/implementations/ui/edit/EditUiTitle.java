package com.snek.fancyplayershops.implementations.ui.edit;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.Shop;
import com.snek.fancyplayershops.implementations.ui.ShopTextElm;
import com.snek.framework.utils.MinecraftUtils;
import com.snek.framework.utils.Txt;

import net.minecraft.item.Items;








public class EditUiTitle extends ShopTextElm {

    public EditUiTitle(@NotNull Shop _shop) {
        super(_shop);
        transform.edit().moveY(calcHeight() * 1.6f * 2f + 0.05f + ShopTextElm.SHIFT_Y);
        updateDisplay();
    }




    public void updateDisplay() {
        text.set(new Txt()
            .cat(new Txt(" Editing ").white())
            .cat(new Txt(shop.getItem().getItem() == Items.AIR ? new Txt("an empty shop").white().get() : MinecraftUtils.getItemName(shop.getItem())))
            .cat("... ")
        .get());
        flushStyle();
    }
}