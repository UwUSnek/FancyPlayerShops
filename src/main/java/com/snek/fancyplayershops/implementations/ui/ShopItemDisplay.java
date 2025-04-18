package com.snek.fancyplayershops.implementations.ui;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import org.joml.Vector3f;

import com.snek.fancyplayershops.Shop;
import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.data_types.displays.CustomItemDisplay;
import com.snek.framework.ui.elements.ItemElm;
import com.snek.framework.ui.styles.ElmStyle;
import com.snek.framework.ui.styles.ItemElmStyle;
import com.snek.framework.utils.Easings;
import com.snek.framework.utils.MinecraftUtils;
import com.snek.framework.utils.scheduler.Scheduler;
import com.snek.framework.utils.scheduler.TaskHandler;

import net.minecraft.entity.decoration.DisplayEntity.ItemDisplayEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;








/**
 * An item display that shows the item currently being sold by a shop.
 * Unconfigured shops show a barrier item.
 */
public class ShopItemDisplay extends ItemElm {
    Shop shop;

    // Task handlers. Used to cancel animations and other visual changes
    private @Nullable TaskHandler loopHandler = null;
    private @Nullable TaskHandler nameToggleHandler = null;


    // The Y translation applied by the spawning animation
    public static final float FOCUS_HEIGHT = 0.05f;

    // Loop animation duration and rotation
    public static final int      LOOP_TIME   = 32;
    public static final float    LOOP_ROT    = (float)Math.toRadians(120);

    // Edit animation scale and transition
    public static final Vector3f EDIT_SCALE  = new Vector3f(0.5f);
    public static final Vector3f EDIT_MOVE   = new Vector3f(0, 0.25f, 0.25f).mul(1f - 0.5f);




    // Focus and loop animations
    private final @NotNull Animation focusAnimation;
    private final @NotNull Animation unfocusAnimation;
    private final @NotNull Animation loopAnimation;

    // Edit animations
    private final @NotNull Animation enterEditAnimation;
    //! leaveEditAnimation not needed as the unfocus animation uses a target transform




    /**
     * Creates a new ShopItemDisplay.
     * @param _shop The target shop.
     * @param _display A CustomItemDisplay to use to display the item.
     */
    public ShopItemDisplay(@NotNull Shop _shop, @NotNull CustomItemDisplay _display) {
        super(_shop.getWorld(), _display, new ItemElmStyle());
        shop = _shop;
        updateDisplay();


        // Setup spawn and despawn animations
        focusAnimation = new Animation(
            new Transition(ElmStyle.S_TIME, Easings.sineOut)
            .additiveTransform(
                new Transform()
                .moveY(FOCUS_HEIGHT)
                .rotY(LOOP_ROT / 2)
            )
        );
        unfocusAnimation = new Animation(
            new Transition(ElmStyle.D_TIME, Easings.sineOut)
            .targetTransform(style.getTransform())
        );


        // Setup loop animation
        loopAnimation = new Animation(
            new Transition(LOOP_TIME, Easings.linear)
            .additiveTransform(new Transform().rotY(LOOP_ROT))
        );


        // Setup edit animiations
        enterEditAnimation = new Animation(
            new Transition(Shop.CANVAS_ANIMATION_DELAY, Easings.sineOut)
            .additiveTransform(
                new Transform()
                .scale(EDIT_SCALE)
                .move(EDIT_MOVE)
                .rotY(LOOP_ROT / 2)
            )
        );
    }




    /**
     * Creates a new ShopItemDisplay.
     * @param _targetShop The target shop.
     * @param _rawDisplay A vanilla ItemDisplayEntity to use to display the item.
     */
    public ShopItemDisplay(@NotNull Shop _targetShop, @NotNull ItemDisplayEntity _rawDisplay) {
        this(_targetShop, new CustomItemDisplay(_rawDisplay));
    }

    /**
     * Creates a new ShopItemDisplay.
     * @param _targetShop The target shop.
     */
    public ShopItemDisplay(@NotNull Shop _targetShop) {
        this(_targetShop, new CustomItemDisplay(_targetShop.getWorld()));
    }




    /**
     * Updates the displayed item reading data from the target shop.
     */
    public void updateDisplay(){
        ItemStack _item = shop.getItem();


        // If the shop is unconfigured (item is AIR), display a barrier and EMPTY_SHOP_NAME as name
        if(_item.getItem() == Items.AIR) {
            ItemStack noItem = Items.BARRIER.getDefaultStack();
            noItem.setCustomName(Shop.EMPTY_SHOP_NAME);
            ((ItemElmStyle)style).setItem(noItem);
            entity.setCustomName(MinecraftUtils.getItemName(noItem));
        }


        // If the shop is configured, display the current item and its name
        else {
            ((ItemElmStyle)style).setItem(_item);
            entity.setCustomName(MinecraftUtils.getItemName(((ItemElmStyle)style).getItem()));
        }


        // Turn on custom name and update the entity
        if(!shop.isFocused()) entity.setCustomNameVisible(true);
        flushStyle();
    }




    @Override
    protected Transform __calcTransform() {
        return super.__calcTransform()
            .rotY(shop.getDefaultRotation())
        ;
    }




    /**
     * Enters the focus state, making the item more visible and starting the loop animation
     */
    public void enterFocusState(){

        // Hide custom name
        if(nameToggleHandler != null) nameToggleHandler.cancel();
        entity.setCustomNameVisible(false);

        // Start animations
        applyAnimation(focusAnimation);
        startLoopAnimation();
    }
    /**
     * Starts the loop animation.
     */
    public void startLoopAnimation() {
        loopHandler = Scheduler.loop(0, loopAnimation.getTotalDuration(), () -> applyAnimation(loopAnimation));
    }




    /**
     * Leaves the focus state.
     */
    public void leaveFocusState(){

        // Stop loop animation and start unfocus animation
        stopLoopAnimation();
        applyAnimation(unfocusAnimation);

        // Show custom name after animations end
        nameToggleHandler = Scheduler.schedule(unfocusAnimation.getTotalDuration(), () -> entity.setCustomNameVisible(true));
    }
    /**
     * Stops the loop animation.
     */
    public void stopLoopAnimation() {
        loopHandler.cancel();
        futureDataQueue.clear();
    }




    /**
     * Enters the edit state
     */
    public void enterEditState(){
        applyAnimation(enterEditAnimation);
    }




    /**
     * Leaves the edit state
     */
    public void leaveEditState(){
        // Empty
        //! leaveEditAnimation not needed as the unfocus animation uses a target transform
    }



    @Override
    public void spawn(Vector3d pos) {
        super.spawn(new Vector3d(pos).add(0, 0.3f, 0));

        // Force display update to remove tracking custom name
        updateDisplay();
    }
}
