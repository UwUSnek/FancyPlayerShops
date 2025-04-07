package com.snek.fancyplayershops.implementations.ui;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.joml.Vector4i;

import com.snek.fancyplayershops.FancyPlayerShops;
import com.snek.fancyplayershops.Shop;
import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.transitions.TextAdditiveTransition;
import com.snek.framework.ui.TextElm;
import com.snek.framework.ui.interfaces.Clickable;
import com.snek.framework.ui.interfaces.Hoverable;
import com.snek.framework.ui.styles.TextElmStyle;
import com.snek.framework.utils.Easings;
import com.snek.framework.utils.MinecraftUtils;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.Utils;
import com.snek.framework.utils.scheduler.Scheduler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.decoration.DisplayEntity.TextDisplayEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ClickType;
import net.minecraft.world.World;








public class DetailsDisplay extends TextElm implements Clickable, Hoverable { //FIXME this should not be hoverable
    private static final String ENTITY_CUSTOM_NAME = FancyPlayerShops.MOD_ID + ".ui.displayentity";
    Shop targetShop;

    private static final Vector3i C_RGB_PRICE      = new Vector3i(243, 255, 0);
    private static final Vector3f C_HSV_STOCK_HIGH = Utils.RGBtoHSV(new Vector3i(0, 223, 0)); //! Float instead of int for more precision
    private static final Vector3f C_HSV_STOCK_LOW  = Utils.RGBtoHSV(new Vector3i(200, 0, 0)); //! Float instead of int for more precision

    private static final Vector4i BG_HOVER = new Vector4i(255, 100, 100, 100); //FIXME this element should not be hoverable. move to actual buttons

    // //FIXME move to button element class
    // public boolean isHighlighted = false;
    //TODO REMOVE ^



    public DetailsDisplay(@NotNull Shop _targetShop){
        super(_targetShop.getWorld());
        targetShop = _targetShop;
        updateDisplay();
    }




    /**
     * Updates the displayed values using the current item name, price and stock.
     */
    public void updateDisplay(){
        float factor = 1.0f - (float)targetShop.getStock() / 1000f;
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




    @Override
    public void despawn(){
        super.despawn();
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




    @Override
    public void onClick(@NotNull PlayerEntity player, @NotNull ClickType click) {
        player.sendMessage(new Txt("CLICKED @" + Scheduler.getTickNum()).get());
    }




    //FIXME this element should not be hoverable
    @Override
    public void onHoverEnter() {
        applyAnimation(new Animation(new TextAdditiveTransition(new Transform(), 2, Easings.linear, BG_HOVER, 255)));
    }




    //FIXME this element should not be hoverable
    @Override
    public void onHoverExit() {
        applyAnimation(new Animation(new TextAdditiveTransition(new Transform(), 2, Easings.linear, ((TextElmStyle)style).getBackground(), 255)));
    }
}
