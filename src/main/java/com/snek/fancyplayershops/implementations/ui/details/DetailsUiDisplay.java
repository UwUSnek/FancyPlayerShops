package com.snek.fancyplayershops.implementations.ui.details;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.joml.Vector3i;

import com.snek.fancyplayershops.Shop;
import com.snek.fancyplayershops.implementations.ui.misc.ShopTextElm;
import com.snek.framework.ui.elements.TextElm;
import com.snek.framework.ui.styles.TextElmStyle;
import com.snek.framework.utils.MinecraftUtils;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.Utils;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;








/**
 * The main display of DetailsUi.
 * It shows informations about the shop.
 */
public class DetailsUiDisplay extends ShopTextElm {




    /**
     * Creates a new DetailsUiDisplay.
     * @param _shop The target shop.
     */
    public DetailsUiDisplay(@NotNull Shop _shop){
        super(_shop, 1, DetailsUi.BACKGROUND_HEIGHT);
        updateDisplay();
        setSizeY(TextElm.calcHeight(this));
        flushStyle();
    }




    /**
     * Updates the displayed values using the current item name, price and stock.
     */
    public void updateDisplay(){

        // Calculate the color of the stock amount
        float factor = 1.0f - shop.getStock() / 1000f;
        Vector3i col = Utils.HSVtoRGB(new Vector3f(DetailsUi.C_HSV_STOCK_LOW).add(new Vector3f(DetailsUi.C_HSV_STOCK_HIGH).sub(DetailsUi.C_HSV_STOCK_LOW).mul(1.0f - (factor * factor))));


        // Empty shop case
        final ItemStack _item = shop.getItem();
        if(_item.getItem() == Items.AIR) {
            ((TextElmStyle)style).setText(new Txt()
            .cat(Shop.EMPTY_SHOP_NAME)
            .cat(new Txt("\nPrice: -"))
            .cat(new Txt("\nStock: -"))
            .get());
        }

        // Configured shop case
        else {
            double price = shop.getPrice();
            ((TextElmStyle)style).setText(new Txt()
                .cat(new Txt(MinecraftUtils.getItemName(_item)).get())
                .cat(new Txt("\nPrice: ")).cat(new Txt(price < 0.005 ? "Free" : Utils.formatPrice(price)).bold().color(DetailsUi.C_RGB_PRICE))
                .cat(new Txt("\nStock: ")).cat(new Txt(Utils.formatAmount(shop.getStock())).bold().color(col))
            .get());
        }

        // Flush style
        flushStyle();
    }
}
