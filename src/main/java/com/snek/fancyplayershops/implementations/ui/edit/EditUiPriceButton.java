package com.snek.fancyplayershops.implementations.ui.edit;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

import com.snek.fancyplayershops.ChatInput;
import com.snek.fancyplayershops.Shop;
import com.snek.fancyplayershops.implementations.ui.ShopButton;
import com.snek.fancyplayershops.implementations.ui.details.DetailsUiDisplay;
import com.snek.framework.generated.FontSize;
import com.snek.framework.ui.TextElm;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.Utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ClickType;




public class EditUiPriceButton extends ShopButton {

    public EditUiPriceButton(@NotNull Shop _shop) {
        super(_shop);
        updateDisplay();
    }




    public void updateDisplay() {
        text.set(new Txt()
            .cat(new Txt(" 🖍 ").lightGray())
            .cat(new Txt(Utils.formatPrice(shop.getPrice())).color(DetailsUiDisplay.C_RGB_PRICE))
            .cat(" ")
        .get());
        flushStyle();
    }




    @Override
    public boolean onClick(@NotNull PlayerEntity player, @NotNull ClickType click) {
        if(super.onClick(player, click)) {
            player.sendMessage(new Txt("Send the new price in chat!").green().get(), true);
            ChatInput.setCallback(player, this::messageCallback);
            return true;
        }
        return false;
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




    @Override
    public void spawn(Vector3d pos) {
        super.spawn(new Vector3d(pos).add(0, calcHeight() * 1.6 * 1 + 0.05, 0)); //TODO replace with calcLineHeight()
    }
}
