package com.snek.fancyplayershops.implementations.ui.edit;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.Shop;
import com.snek.fancyplayershops.implementations.ui.ShopFancyTextElm;
import com.snek.fancyplayershops.implementations.ui.ShopTextInput;
import com.snek.fancyplayershops.implementations.ui.details.DetailsUi;
import com.snek.fancyplayershops.implementations.ui.styles.ShopButtonStyle;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.Utils;

import net.minecraft.text.Text;








/**
 * A button that allows the owner of the shop to change its price.
 */
public class EditUiPriceButton extends ShopTextInput {

    /**
     * Creates a new EditUiPriceButton.
     * @param _shop The target shop.
     */
    public EditUiPriceButton(@NotNull Shop _shop) {
        super(_shop, 0.75f, ShopFancyTextElm.LINE_H, new Txt("Send the new price in chat!").green().get());
        updateDisplay(null);
    }


    @Override
    public void updateDisplay(Text textOverride) {
        ((ShopButtonStyle)style).setText(textOverride != null ? textOverride : new Txt()
            .cat(new Txt(Utils.formatPrice(shop.getPrice())).color(DetailsUi.C_RGB_PRICE))
        .get());
        flushStyle();
    }



    @Override
    protected boolean messageCallback(String s) {
        try {

            // Try to set the new price and update the display if it's valid
            final boolean r = shop.setPrice(Float.parseFloat(s));
            if(r) updateDisplay(null);
            return r;
        } catch(NumberFormatException e) {
            return false;
        }
    }
}
