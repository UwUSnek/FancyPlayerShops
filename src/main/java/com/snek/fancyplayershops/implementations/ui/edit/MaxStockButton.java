package com.snek.fancyplayershops.implementations.ui.edit;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

import com.snek.fancyplayershops.Shop;
import com.snek.fancyplayershops.implementations.ui.ShopButton;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.Utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ClickType;




public class MaxStockButton extends ShopButton {

    public MaxStockButton(@NotNull Shop _shop) {
        super(_shop);
        updateDisplay();
    }


    public void updateDisplay() {
        text.set(new Txt("Stock size: " + Utils.formatPrice(shop.getMaxStock())).get());
    }


    @Override
    public boolean onClick(@NotNull PlayerEntity player, @NotNull ClickType click) {
        if(super.onClick(player, click)) {
            player.sendMessage(new Txt("Send the new stock size in chat!").bold().green().get());
            //FIXME catch stock size
            return true;
        }
        return false;
    }


    @Override
    public void spawn(Vector3d pos) {
        super.spawn(pos.add(0, 0.1, 0));
    }
}
