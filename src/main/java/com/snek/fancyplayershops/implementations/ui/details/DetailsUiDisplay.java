package com.snek.fancyplayershops.implementations.ui.details;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.joml.Vector3i;

import com.snek.fancyplayershops.Shop;
import com.snek.fancyplayershops.implementations.ui.ShopTextElm;
import com.snek.framework.ui.styles.TextElmStyle;
import com.snek.framework.utils.MinecraftUtils;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.Utils;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;








public class DetailsUiDisplay extends ShopTextElm {

    public static final Vector3i C_RGB_PRICE      = new Vector3i(243, 255, 0);
    public static final Vector3f C_HSV_STOCK_HIGH = Utils.RGBtoHSV(new Vector3i(0, 223, 0)); //! Float instead of int for more precision
    public static final Vector3f C_HSV_STOCK_LOW  = Utils.RGBtoHSV(new Vector3i(200, 0, 0)); //! Float instead of int for more precision



    public DetailsUiDisplay(@NotNull Shop _shop){
        super(_shop);
        updateDisplay();
    }




    /**
     * Updates the displayed values using the current item name, price and stock.
     */
    public void updateDisplay(){
        float factor = 1.0f - shop.getStock() / 1000f;
        Vector3i col = Utils.HSVtoRGB(new Vector3f(C_HSV_STOCK_LOW).add(new Vector3f(C_HSV_STOCK_HIGH).sub(C_HSV_STOCK_LOW).mul(1.0f - (factor * factor))));


        // Empty shop case
        final ItemStack _item = shop.getItem();
        if(_item.getItem() == Items.AIR) {
            ((TextElmStyle)style).setText(new Txt(" ")
            .cat(Shop.EMPTY_SHOP_NAME).cat(" ")
            .cat(new Txt("\n Price: -")).cat(" ")
            .cat(new Txt("\n Stock: -")).cat(" ")
            .get());
        }

        // Configured shop case
        else {
            double price = shop.getPrice();
            ((TextElmStyle)style).setText(new Txt()
                .cat(new Txt(MinecraftUtils.getItemName(_item)).get()).cat(" ")
                .cat(new Txt("\n Price: ")).cat(new Txt(price < 0.005 ? "Free" : Utils.formatPrice(price)).bold().color(C_RGB_PRICE)).cat(" ")
                .cat(new Txt("\n Stock: ")).cat(new Txt(Utils.formatAmount(shop.getStock())).bold().color(col)).cat(" ")
            .get());
        }

        // Flush style
        flushStyle();
    }
}
