package com.snek.fancyplayershops.implementations.ui.edit;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.ChatInput;
import com.snek.fancyplayershops.Shop;
import com.snek.fancyplayershops.implementations.ui.ShopButton;
import com.snek.fancyplayershops.implementations.ui.ShopTextElm;
import com.snek.fancyplayershops.implementations.ui.details.DetailsUiDisplay;
import com.snek.fancyplayershops.implementations.ui.styles.ShopButtonStyle;
import com.snek.framework.ui.styles.TextElmStyle;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.Utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ClickType;




public class EditUiPriceButton extends ShopButton {

    public EditUiPriceButton(@NotNull Shop _shop) {
        super(_shop, 0.5f, ShopTextElm.LINE_H);
        updateDisplay();
        // System.out.println("Scale after: " + __calcTransform().getScale().toString());
        // System.out.println("Scale value: " + getAbsSize().toString());
    }




    public void updateDisplay() {
        ((ShopButtonStyle)style).setText(new Txt()
            .cat(new Txt(" 🖍 ").lightGray())
            .cat(new Txt(Utils.formatPrice(shop.getPrice())).color(DetailsUiDisplay.C_RGB_PRICE))
            .cat(" ")
        .get());
        flushStyle();
    }




    @Override
    public boolean onClick(@NotNull PlayerEntity player, @NotNull ClickType click) {
        boolean r = super.onClick(player, click);
        if(r) {
            player.sendMessage(new Txt("Send the new price in chat!").green().get(), true);
            ChatInput.setCallback(player, this::messageCallback);
        }
        return r;
    }




    private boolean messageCallback(String s) {
        try {
            final boolean r = shop.setPrice(Float.parseFloat(s));
            if(r) updateDisplay();
            return r;
        } catch(NumberFormatException e) {
            return false;
        }
    }
}
