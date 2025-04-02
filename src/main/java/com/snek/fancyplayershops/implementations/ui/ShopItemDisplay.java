package com.snek.fancyplayershops.implementations.ui;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import org.joml.Vector3f;

import com.snek.fancyplayershops.Shop;
import com.snek.framework.custom_displays.CustomItemDisplay;
import com.snek.framework.data_types.AdditiveTransition;
import com.snek.framework.data_types.Animation;
import com.snek.framework.data_types.TargetTransition;
import com.snek.framework.data_types.Transform;
import com.snek.framework.ui.ItemElm;
import com.snek.framework.ui.styles.ElmStyle;
import com.snek.framework.ui.styles.ItemElmStyle;
import com.snek.framework.utils.Easings;
import com.snek.framework.utils.Scheduler;
import com.snek.framework.utils.TaskHandler;
import com.snek.framework.utils.Utils;

import net.minecraft.entity.decoration.DisplayEntity.ItemDisplayEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;








public class ShopItemDisplay extends ItemElm {

    Shop targetShop;

    // public void setDefaultRotation(float r) { defaultRotation = r; } //FIXME save in shop instance
    // private static       float defaultRotation       = (float) Math.toRadians(45);

    private @Nullable TaskHandler loopHandler = null;
    public static final int L_TIME = 32;
    // public static final float L_ROT   = (float) Math.toRadians(120);
    // public static final float L_ROT_A = L_ROT * (float)((double)S_TIME / ((double)L_TIME / 3) * 3d);





    private final @NotNull Animation focusAnimation;
    private final @NotNull Animation unfocusAnimation;
    private final @NotNull Animation loopAnimation;




    public ShopItemDisplay(@NotNull Shop _targetShop, @NotNull CustomItemDisplay _display) {
        super(_targetShop.getWorld(), _display, new ItemElmStyle().setDespawnAnimation(null));
        targetShop = _targetShop;
        entity.setCustomNameVisible(true);
        updateDisplay();

        focusAnimation   = new Animation(new AdditiveTransition(new Transform().moveY(ElmStyle.S_HEIGHT).scale(ElmStyle.S_SCALE), ElmStyle.S_TIME, Easings.sineOut)); //TODO use better easing
        unfocusAnimation = new Animation(new   TargetTransition(transform.get(),                                                  ElmStyle.D_TIME, Easings.sineOut)); //TODO use better easing

        loopAnimation = new Animation(
            new AdditiveTransition(new Transform().rotY((float)Math.toRadians(120)), L_TIME, Easings.linear), //FIXME remove
            new AdditiveTransition(new Transform().rotY((float)Math.toRadians(120)), L_TIME, Easings.linear), //FIXME remove
            new AdditiveTransition(new Transform().rotY((float)Math.toRadians(120)), L_TIME, Easings.linear)
        );
    }


    public ShopItemDisplay(@NotNull Shop _targetShop, @NotNull ItemDisplayEntity _rawDisplay) {
        this(_targetShop, new CustomItemDisplay(_rawDisplay));
    }
    public ShopItemDisplay(@NotNull Shop _targetShop) {
        this(_targetShop, new CustomItemDisplay(_targetShop.getWorld()));
    }




    /**
     * Updates the displayed values using the current item name, price and stock.
     */
    public void updateDisplay(){
        ItemStack _item = targetShop.getItem();
        if(_item.getItem() == Items.AIR) {
            ItemStack noItem = Items.BARRIER.getDefaultStack();
            noItem.setCustomName(Shop.EMPTY_SHOP_NAME);
            item.set(noItem);
            entity.setCustomName(Utils.getItemName(noItem));
        }
        else {
            item.set(_item);
            entity.setCustomName(Utils.getItemName(item.get()));
        }

        flushStyle();
    }




    @Override
    public void spawn(Vector3d pos){
        super.spawn(pos);
    }




    @Override
    public void despawn(){
        super.despawn();
    }




    public void enterFocusState(){

        // Hide custom name and start focus animation
        entity.setCustomNameVisible(false);
        applyAnimation(focusAnimation);
        for (Transform transform : transformQueue) {
            Vector3f v = new Vector3f();
            transform.get().getLeftRotation().getEulerAnglesXYZ(v);
            System.out.println("before: " + v.toString());
        }

        // Queue loop animation
        loopHandler = Scheduler.loop(0, loopAnimation.getTotalDuration(), () -> {
            for (Transform transform : transformQueue) {
                Vector3f v = new Vector3f();
                transform.get().getLeftRotation().getEulerAnglesXYZ(v);
                System.out.println("before 2: " + v.toString());
            }
            applyAnimation(loopAnimation);
            for (Transform transform : transformQueue) {
                Vector3f v = new Vector3f();
                transform.get().getLeftRotation().getEulerAnglesXYZ(v);
                System.out.println("after: " + v.toString());
            }
        });
    }




    public void leaveFocusState(){

        // Stop loop animation and start unfocus animation
        loopHandler.cancel();
        transformQueue.clear();
        applyAnimation(unfocusAnimation);

        // Show custom name after animations end
        Scheduler.schedule(unfocusAnimation.getTotalDuration(), () -> {
            entity.setCustomNameVisible(true);
        });
    }
}
