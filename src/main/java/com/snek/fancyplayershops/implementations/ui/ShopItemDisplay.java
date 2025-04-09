package com.snek.fancyplayershops.implementations.ui;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import com.snek.fancyplayershops.Shop;
import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.transitions.AdditiveTransition;
import com.snek.framework.data_types.animations.transitions.TargetTransition;
import com.snek.framework.data_types.displays.CustomItemDisplay;
import com.snek.framework.ui.ItemElm;
import com.snek.framework.ui.styles.ElmStyle;
import com.snek.framework.ui.styles.ItemElmStyle;
import com.snek.framework.utils.Easings;
import com.snek.framework.utils.MinecraftUtils;
import com.snek.framework.utils.scheduler.Scheduler;
import com.snek.framework.utils.scheduler.TaskHandler;

import net.minecraft.entity.decoration.DisplayEntity.ItemDisplayEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;








public class ShopItemDisplay extends ItemElm {

    Shop targetShop;

    // public void setDefaultRotation(float r) { defaultRotation = r; } //FIXME save in shop instance
    // private static       float defaultRotation       = (float) Math.toRadians(45);

    private @Nullable TaskHandler loopHandler = null;
    private @Nullable TaskHandler nameToggleHandler = null;


    public static final int      LOOP_TIME   = 32;
    public static final float    LOOP_ROT    = (float)Math.toRadians(120);

    public static final Vector3f EDIT_SCALE  = new Vector3f(0.5f);
    public static final Vector3f EDIT_MOVE   = new Vector3f(-0.25f, 0.25f, 0).mul(1f - 0.5f);
    public static final Vector3f EDIT_MOVE_2 = new Vector3f(-0.2f, 0, 0);





    private final @NotNull Animation focusAnimation;
    private final @NotNull Animation unfocusAnimation;
    private final @NotNull Animation loopAnimation;

    private final @NotNull Animation enterEditAnimation;
    //! leaveEditAnimation not needed as the unfocus animation uses a target transform




    public ShopItemDisplay(@NotNull Shop _targetShop, @NotNull CustomItemDisplay _display) {
        super(_targetShop.getWorld(), _display, new ItemElmStyle().setDespawnAnimation(null));
        targetShop = _targetShop;
        entity.setCustomNameVisible(true);
        updateDisplay();


        // Setup spawn and despawn animations animation
        focusAnimation = new Animation(new AdditiveTransition(
            new Transform().moveY(ElmStyle.S_HEIGHT).scale(ElmStyle.S_SCALE).rotY(LOOP_ROT / 2),
            ElmStyle.S_TIME,
            Easings.sineOut
        ));
        unfocusAnimation = new Animation(new TargetTransition(
            transform.get(),
            ElmStyle.D_TIME,
            Easings.sineOut
        ));


        // Setup loop animation
        loopAnimation = new Animation(
            new AdditiveTransition(new Transform().rotY(LOOP_ROT), LOOP_TIME, Easings.linear)
        );


        // Setup edit animiations
        enterEditAnimation = new Animation(
            new AdditiveTransition(
                new Transform().scale(EDIT_SCALE).move(EDIT_MOVE).rotY(LOOP_ROT / 2),
                Shop.CANVAS_ANIMATION_DELAY,
                Easings.sineOut
            ),
            new AdditiveTransition(
                new Transform().move(EDIT_MOVE_2),
                ElmStyle.S_TIME,
                Easings.sineOut
            )
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
            entity.setCustomName(MinecraftUtils.getItemName(noItem));
        }
        else {
            item.set(_item);
            entity.setCustomName(MinecraftUtils.getItemName(item.get()));
        }

        flushStyle();
    }




    public void enterFocusState(){

        // Hide custom name and start focus animation
        if(nameToggleHandler != null) nameToggleHandler.cancel();
        entity.setCustomNameVisible(false);
        applyAnimation(focusAnimation);

        // Queue loop animation
        loopHandler = Scheduler.loop(0, loopAnimation.getTotalDuration(), () -> applyAnimation(loopAnimation));
    }




    public void leaveFocusState(){

        // Stop loop animation and start unfocus animation
        loopHandler.cancel();
        transformQueue.clear();
        applyAnimation(unfocusAnimation);

        // Show custom name after animations end
        nameToggleHandler = Scheduler.schedule(unfocusAnimation.getTotalDuration(), () -> entity.setCustomNameVisible(true));
    }




    public void enterEditState(){
        applyAnimation(enterEditAnimation);
    }




    public void leaveEditState(){
        // Empty
        //! leaveEditAnimation not needed as the unfocus animation uses a target transform
    }
}
