package com.snek.fancyplayershops.implementations.ui.edit;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

import com.snek.fancyplayershops.Shop;
import com.snek.fancyplayershops.implementations.ui.ShopTextElm;
import com.snek.framework.generated.FontSize;
import com.snek.framework.ui.TextElm;
import com.snek.framework.utils.MinecraftUtils;
import com.snek.framework.utils.Txt;

import net.minecraft.item.Items;








public class EditUiTitle extends ShopTextElm {

    public EditUiTitle(@NotNull Shop _shop) {
        super(_shop);
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




    @Override
    public void spawn(Vector3d pos) {
        super.spawn(new Vector3d(pos).add(0, calcHeight() * 1.6 * 2 + 0.05, 0)); //TODO replace with calcLineHeight()
    }
}