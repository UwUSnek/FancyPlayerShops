package com.snek.fancyplayershops.implementations.ui.edit;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

import com.snek.fancyplayershops.Shop;
import com.snek.fancyplayershops.implementations.ui.ShopTextElm;
import com.snek.framework.utils.MinecraftUtils;
import com.snek.framework.utils.Txt;








public class EditUiTitle extends ShopTextElm {

    public EditUiTitle(@NotNull Shop _shop) {
        super(_shop);
        updateDisplay();
    }




    public void updateDisplay() {
        text.set(new Txt(" Editing ").cat(new Txt(MinecraftUtils.getItemName(shop.getItem()))).cat("... ").get());
        flushStyle();
    }




    @Override
    public void spawn(Vector3d pos) {
        super.spawn(new Vector3d(pos).add(0, 0.4, 0));
    }
}