package com.snek.fancyplayershops.implementations.ui.edit;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.Shop;
import com.snek.fancyplayershops.implementations.ui.ShopFancyTextElm;
import com.snek.fancyplayershops.implementations.ui.ShopTextInput;
import com.snek.fancyplayershops.implementations.ui.styles.ShopButtonStyle;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.Utils;

import net.minecraft.text.Text;








/**
 * A button that allows the owner of the shop to change its stock limit.
 */
public class EditUiStockLimitButton extends ShopTextInput {

    public EditUiStockLimitButton(@NotNull Shop _shop) {
        super(_shop, 0.75f, ShopFancyTextElm.LINE_H, new Txt("Send the new stock limit in chat!").green().get());
        updateDisplay(null);
    }


    @Override
    public void updateDisplay(Text textOverride) {
        ((ShopButtonStyle)style).setText(textOverride != null ? textOverride : new Txt()
            .cat(new Txt(Utils.formatAmount(shop.getMaxStock(), true, true)).color(EditUi.RGB_STOCK_COLOR))
        .get());
        flushStyle();
    }




    @Override
    protected boolean messageCallback(String s) {
        try {

            // Try to set the new stock limit, update the display if it's valid
            final boolean r = shop.setStockLimit(Integer.parseInt(s));
            if(r) updateDisplay(null);
            return r;
        } catch(NumberFormatException e) {
            try {

                // Try to set the new stock limit, update the display if it's valid
                final boolean r = shop.setStockLimit(Float.parseFloat(s));
                if(r) updateDisplay(null);
                return r;
            } catch(NumberFormatException e2) {
                return false;
            }
        }
    }
}
