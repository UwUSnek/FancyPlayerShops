package com.snek.fancyplayershops.implementations.ui.edit;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import com.snek.fancyplayershops.ChatInput;
import com.snek.fancyplayershops.Shop;
import com.snek.fancyplayershops.implementations.ui.ShopButton;
import com.snek.fancyplayershops.implementations.ui.ShopTextElm;
import com.snek.fancyplayershops.implementations.ui.details.DetailsUiDisplay;
import com.snek.framework.ui.styles.__internal_TextElmStyle;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.Utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ClickType;








public class EditUiMaxStockButton extends ShopButton {
    private static Vector3i RGB_STOCK_COLOR = Utils.HSVtoRGB(DetailsUiDisplay.C_HSV_STOCK_HIGH);

    public EditUiMaxStockButton(@NotNull Shop _shop) {
        super(_shop, 0.5f, 0.1f);
        updateDisplay();
    }




    public void updateDisplay() {
        ((__internal_TextElmStyle)text.style).setText(new Txt()
            .cat(new Txt(" 🖍 ").lightGray())
            .cat(new Txt(Utils.formatAmount(shop.getMaxStock(), true, true)).color(RGB_STOCK_COLOR))
            .cat(" ")
        .get());
        flushStyle();
    }




    @Override
    public boolean onClick(@NotNull PlayerEntity player, @NotNull ClickType click) {
        boolean r = super.onClick(player, click);
        if(r) {
            player.sendMessage(new Txt("Send the new stock limit in chat!").green().get(), true);
            ChatInput.setCallback(player, this::messageCallback);
        }
        return r;
    }




    private boolean messageCallback(String s) {
        try {
            final boolean r = shop.setStockLimit(Integer.parseInt(s));
            if(r) updateDisplay();
            return r;
        } catch(NumberFormatException e) {
            try {
            final boolean r = shop.setStockLimit(Float.parseFloat(s));
            if(r) updateDisplay();
            return r;
            } catch(NumberFormatException e2) {
                return false;
            }
        }
    }
}
