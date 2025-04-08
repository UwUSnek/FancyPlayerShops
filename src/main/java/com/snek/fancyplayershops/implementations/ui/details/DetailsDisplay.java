package com.snek.fancyplayershops.implementations.ui.details;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3i;

import com.snek.fancyplayershops.FancyPlayerShops;
import com.snek.fancyplayershops.Shop;
import com.snek.framework.ui.TextElm;
import com.snek.framework.utils.MinecraftUtils;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.Utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.decoration.DisplayEntity.TextDisplayEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;








public class DetailsDisplay extends TextElm {
    private static final String ENTITY_CUSTOM_NAME = FancyPlayerShops.MOD_ID + ".ui.displayentity";
    Shop targetShop;

    private static final Vector3i C_RGB_PRICE      = new Vector3i(243, 255, 0);
    private static final Vector3f C_HSV_STOCK_HIGH = Utils.RGBtoHSV(new Vector3i(0, 223, 0)); //! Float instead of int for more precision
    private static final Vector3f C_HSV_STOCK_LOW  = Utils.RGBtoHSV(new Vector3i(200, 0, 0)); //! Float instead of int for more precision



    public DetailsDisplay(@NotNull Shop _targetShop){
        super(_targetShop.getWorld());
        targetShop = _targetShop;
        updateDisplay();
    }




    /**
     * Updates the displayed values using the current item name, price and stock.
     */
    public void updateDisplay(){
        float factor = 1.0f - targetShop.getStock() / 1000f;
        Vector3i col = Utils.HSVtoRGB(new Vector3f(C_HSV_STOCK_LOW).add(new Vector3f(C_HSV_STOCK_HIGH).sub(C_HSV_STOCK_LOW).mul(1.0f - (factor * factor))));


        // Empty shop case
        final ItemStack _item = targetShop.getItem();
        if(_item.getItem() == Items.AIR) {
            text.set(new Txt()
            .cat(Shop.EMPTY_SHOP_NAME)
            .cat(new Txt("\nPrice: -"))
            .cat(new Txt("\nStock: -"))
            .get());
        }

        // Configured shop case
        else {
            double price = targetShop.getPrice();
            text.set(new Txt()
                .cat(new Txt(MinecraftUtils.getItemName(_item)).get())
                .cat(new Txt("\nPrice: ")).cat(new Txt(price < 0.005 ? "Free" : Utils.formatPrice(price)).bold().color(C_RGB_PRICE))
                .cat(new Txt("\nStock: ")).cat(new Txt(Utils.formatAmount(targetShop.getStock())).bold().color(col))
            .get());
        }

        // Flush style
        flushStyle();
    }




    @Override
    public void spawn(Vector3d pos){
        super.spawn(pos);
        entity.setCustomName(new Txt(ENTITY_CUSTOM_NAME).get());
        entity.setCustomNameVisible(false);
    }




    /**
     * Checks for stray focus displays and purges them.
     * Any TextDisplayEntity not registered as active display and in the same block as a shop is considered a stray display.
     * Must be called on entity load event.
     * @param entity The entity.
     */
    public static void onEntityLoad(@NotNull Entity entity) {
        if (entity instanceof TextDisplayEntity) {
            World world = entity.getWorld();
            if(
                world != null &&
                entity.getCustomName() != null &&
                entity.getCustomName().getString().equals(ENTITY_CUSTOM_NAME)
            ) {
                entity.remove(RemovalReason.KILLED);
            }
        }
    }




    // //FIXME move to actual button elements
    // @Override
    // public void onHoverEnter() {
    //     applyAnimation(new Animation(new TextAdditiveTransition(new Transform(), 2, Easings.linear, BG_HOVER, 255)));
    // }




    // //FIXME move to actual button elements
    // @Override
    // public void onHoverExit() {
    //     applyAnimation(new Animation(new TextAdditiveTransition(new Transform(), 2, Easings.linear, ((TextElmStyle)style).getBackground(), 255)));
    // }
}
