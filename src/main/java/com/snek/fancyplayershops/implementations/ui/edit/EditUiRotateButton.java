package com.snek.fancyplayershops.implementations.ui.edit;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.Shop;
import com.snek.fancyplayershops.implementations.ui.ShopButton;
import com.snek.fancyplayershops.implementations.ui.styles.ShopButtonStyle;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;








/**
 * A button that allows the owner of the shop to change the default rotation of the displayed object.
 */
public class EditUiRotateButton extends ShopButton {
    private final float rotation;
    private final Text buttonText;


    /**
     * Creates a new RotateButton.
     * @param _shop The target shop.
     * @param _rotateAngle The angle to add to the default rotation each time this button is pressed.
     * @param _buttonText The text to display on the button.
     */
    public EditUiRotateButton(Shop _shop, float _rotation, Text _buttonText){
        super(_shop, EditUi.SQUARE_BUTTON_SIZE, EditUi.SQUARE_BUTTON_SIZE);
        rotation = _rotation;
        buttonText = _buttonText;
        updateDisplay(null);
    }


    @Override
    public void updateDisplay(@Nullable Text textOverride) {
        ((ShopButtonStyle)style).setText(textOverride != null ? textOverride : buttonText);
        flushStyle();
    }


    @Override
    public boolean onClick(@NotNull PlayerEntity player, @NotNull ClickType click) {
        boolean r = super.onClick(player, click);
        if(r) shop.addDefaultRotation(rotation);
        return r;
    }
}
