package com.snek.fancyplayershops.implementations.ui;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

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
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.Utils;

import net.minecraft.entity.decoration.DisplayEntity.ItemDisplayEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;








public class ShopItemDisplay extends ItemElm {

    Shop targetShop;

    // public void setDefaultRotation(float r) { defaultRotation = r; } //FIXME save in shop instance
    // private static       float defaultRotation       = (float) Math.toRadians(45);
    // private static final Transform DEFAULT_TRANSFORM = new Transform().scale(0.5f).rotY(defaultRotation);

    // public static final int S_TIME = DetailsDisplay.S_TIME + 2;
    public static final int L_TIME = 32 * 3; //! Must be a multiple of 3
    // public static final int D_TIME = DetailsDisplay.D_TIME + 2;

    // private static final float S_SCALE  = 1.02f;
    // private static final float S_HEIGHT = 0.05f;

    // public static final float L_ROT   = (float) Math.toRadians(120);
    // public static final float L_ROT_A = L_ROT * (float)((double)S_TIME / ((double)L_TIME / 3) * 3d);





    private final @NotNull Animation focusAnimation;
    private final @NotNull Animation unfocusAnimation;
    // new Animation(
    //     new TargetTransition(DEFAULT_TRANSFORM.clone().moveY(S_HEIGHT).scale(S_SCALE).rotY(L_ROT_A + L_ROT * 1), L_TIME / 3),
    //     new TargetTransition(DEFAULT_TRANSFORM.clone().moveY(S_HEIGHT).scale(S_SCALE).rotY(L_ROT_A + L_ROT * 2), L_TIME / 3),
    //     new TargetTransition(DEFAULT_TRANSFORM.clone().moveY(S_HEIGHT).scale(S_SCALE).rotY(L_ROT_A + L_ROT * 3), L_TIME / 3)
    // ),




    // public ShopItemDisplay(@NotNull ItemDisplayEntity _rawDisplay, @NotNull AnimationData _animation) {
        // public ShopItemDisplay(@NotNull ServerWorld _world, @NotNull CustomItemDisplay _rawDisplay) {
    public ShopItemDisplay(@NotNull Shop _targetShop, @NotNull CustomItemDisplay _display) {
        super(_targetShop.getWorld(), _display, new ItemElmStyle().setDespawnAnimation(null));
        targetShop = _targetShop;
        entity.setCustomNameVisible(true);
        updateDisplay();

        focusAnimation   = new Animation(new AdditiveTransition(new Transform().moveY(ElmStyle.S_HEIGHT).scale(ElmStyle.S_SCALE), ElmStyle.S_TIME, Easings.sineOut)); //TODO use better easing
        unfocusAnimation = new Animation(new   TargetTransition(transform.get(),                                                  ElmStyle.D_TIME, Easings.sineOut)); //TODO use better easing
    }


    public ShopItemDisplay(@NotNull Shop _targetShop, @NotNull ItemDisplayEntity _rawDisplay) {
        this(_targetShop, new CustomItemDisplay(_rawDisplay));
    }
    public ShopItemDisplay(@NotNull Shop _targetShop) {
        this(_targetShop, new CustomItemDisplay(_targetShop.getWorld()));
    }

    // public ShopItemDisplay(@NotNull Shop targetShop) {
    //     super(
    //         targetShop.getWorld(),
    //         targetShop.calcDisplayPos(),
    //         DEFAULT_TRANSFORM,
    //         targetShop.getItem(),
    //         true,
    //         false,
    //         null
    //     );
    //     rawDisplay.setCustomName(Utils.getItemName(targetShop.getItem()));
    // }

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
        entity.setCustomNameVisible(false);
        applyAnimation(focusAnimation);

        //FIXME add loop animations back
        // currentHandlers.add(Scheduler.schedule(focusAnimation.spawn.getTotalDuration(), () -> {
            // loopAnimation(focusAnimation.loop, focusAnimation.loop.getTotalDuration());
        // }));
    }




    public void leaveFocusState(){
        applyAnimation(unfocusAnimation);

        Scheduler.schedule(unfocusAnimation.getTotalDuration(), () -> {
            entity.setCustomNameVisible(true);
        });
    }
}
