package com.snek.fancyplayershops.implementations.ui.edit;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.Shop;
import com.snek.fancyplayershops.implementations.ui.ShopButton;
import com.snek.fancyplayershops.implementations.ui.ShopItemDisplay;
import com.snek.fancyplayershops.implementations.ui.styles.EditUiRotateButtonStyle;
import com.snek.fancyplayershops.implementations.ui.styles.ShopButtonStyle;
import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.utils.Easings;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;








/**
 * A button that allows the owner of the shop to change the default rotation of the displayed object.
 */
public class EditUiRotateButton extends ShopButton {
    public static final int ROTATION_ANIMATION_TIME = 8;

    private final float rotation;
    private final Text buttonText;


    /**
     * Creates a new RotateButton.
     * @param _shop The target shop.
     * @param _rotateAngle The angle to add to the default rotation each time this button is pressed.
     * @param _buttonText The text to display on the button.
     */
    public EditUiRotateButton(Shop _shop, float _rotation, Text _buttonText){
        super(_shop, EditUi.SQUARE_BUTTON_SIZE, EditUi.SQUARE_BUTTON_SIZE, ROTATION_ANIMATION_TIME, new EditUiRotateButtonStyle());
        rotation = _rotation;
        buttonText = _buttonText;
        updateDisplay(null);

        // Adjust arrow size
        applyAnimationNow(new Animation(
            new Transition()
            .additiveTransformFg(new Transform().scale(EditUi.SQUARE_BUTTON_SIZE * 10))
        ));
    }


    @Override
    public void updateDisplay(@Nullable Text textOverride) {
        ((ShopButtonStyle)style).setText(textOverride != null ? textOverride : buttonText);
        flushStyle();
    }


    @Override
    public boolean onClick(@NotNull PlayerEntity player, @NotNull ClickType click) {
        boolean r = super.onClick(player, click);
        if(r) {
            shop.addDefaultRotation(rotation);

            // Animate the item display to show the new rotation
            shop.findItemDisplayEntityIfNeeded().applyAnimation(new Animation(
                new Transition(2, Easings.sineInOut)
                .additiveTransform(new Transform().rotY(-rotation))
            ));
            shop.findItemDisplayEntityIfNeeded().applyAnimation(new Animation(
                new Transition(ROTATION_ANIMATION_TIME, Easings.sineInOut)
                .additiveTransform(new Transform().rotY(rotation))
            ));
        }
        return r;
    }


    @Override
    public void onHoverEnter(PlayerEntity player) {
        super.onHoverEnter(player);

        // Handle item display animations
        ShopItemDisplay itemDisplay = shop.findItemDisplayEntityIfNeeded();
        itemDisplay.stopLoopAnimation();
        itemDisplay.applyAnimation(new Animation(
            new Transition(ROTATION_ANIMATION_TIME, Easings.sineOut)
            .targetTransform(
                itemDisplay.style.getDefaultTransform()
                .move(ShopItemDisplay.EDIT_MOVE)
                .moveY(ShopItemDisplay.FOCUS_HEIGHT)
                .scale(ShopItemDisplay.EDIT_SCALE)
            )
        ));
    }


    @Override
    public void onHoverExit(PlayerEntity player) {
        super.onHoverExit(player);

        // Handle item display animations
        shop.findItemDisplayEntityIfNeeded().startLoopAnimation();
    }
}
