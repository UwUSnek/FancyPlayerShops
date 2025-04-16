package com.snek.fancyplayershops.implementations.ui.edit;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.ChatInput;
import com.snek.fancyplayershops.Shop;
import com.snek.fancyplayershops.implementations.ui.ShopButton;
import com.snek.fancyplayershops.implementations.ui.ShopTextElm;
import com.snek.fancyplayershops.implementations.ui.details.DetailsUiDisplay;
import com.snek.fancyplayershops.implementations.ui.styles.ShopButtonStyle;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.Utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ClickType;








/**
 * A button that allows the owner of the shop to change its price.
 */
public class EditUiPriceButton extends ShopButton {

    /**
     * Creates a new EditUiPriceButton.
     * @param _shop The target shop.
     */
    public EditUiPriceButton(@NotNull Shop _shop) {
        super(_shop, 0.5f, ShopTextElm.LINE_H);
        updateDisplay();
    }


    /**
     * Updates the displayed text, reading data from the target shop.
     */
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

            // Tell the player to send the price in chat and set the chat input callback
            player.sendMessage(new Txt("Send the new price in chat!").green().get(), true);
            ChatInput.setCallback(player, this::messageCallback);
        }
        return r;
    }




    private boolean messageCallback(String s) {
        try {

            // Try to set the new price and update the display if it's valid
            final boolean r = shop.setPrice(Float.parseFloat(s));
            if(r) updateDisplay();
            return r;
        } catch(NumberFormatException e) {
            return false;
        }
    }
}
