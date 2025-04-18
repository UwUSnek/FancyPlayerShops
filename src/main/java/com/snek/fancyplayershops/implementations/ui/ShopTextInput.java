package com.snek.fancyplayershops.implementations.ui;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.ChatInput;
import com.snek.fancyplayershops.Shop;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.scheduler.Scheduler;
import com.snek.framework.utils.scheduler.TaskHandler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;








/**
 * A generic text input class that allows the user to enter a chat input after clicking the element.
 */
public abstract class ShopTextInput extends ShopButton {
    public static final int CURSOR_TOGGLE_DELAY = 10;

    private final @NotNull  Text        clickFeedbackMessage;
    private       @Nullable TaskHandler inputStateHandler = null;
    private boolean cursorToggleState = true;


    /**
     * Creates a new ShopTextInput.
     * @param _shop The target shop.
     * @param w The width of the button, expressed in blocks.
     * @param h The height of the button, expressed in blocks.
     * @param _clickFeedbackMessage The message to show to the player when they click the element.
     */
    protected ShopTextInput(@NotNull Shop _shop, float w, float h, Text _clickFeedbackMessage) {
        super(_shop, w, h, 1);
        clickFeedbackMessage = _clickFeedbackMessage;
    }




    @Override
    public boolean onClick(@NotNull PlayerEntity player, @NotNull ClickType click) {
        boolean r = super.onClick(player, click);
        if(r) {
            enterInputState();
            player.sendMessage(clickFeedbackMessage, true);
            ChatInput.setCallback(player, this::__internal_messageCallback);
        }
        return r;
    }


    protected void enterInputState(){
        inputStateHandler = Scheduler.loop(0, CURSOR_TOGGLE_DELAY, () -> {
            updateDisplay(new Txt(cursorToggleState ? "|" : " ").get());
            cursorToggleState = !cursorToggleState;
        });
    }


    protected void exitInputState(){
        if(inputStateHandler != null) inputStateHandler.cancel();
        updateDisplay(null);
    }


    protected boolean __internal_messageCallback(String s) {
        boolean r = messageCallback(s);
        if(r) exitInputState();
        return r;
    }


    /**
     * The callback function called when a chat input is received.
     * @param s The input string.
     * @return True if the string s is a valid input, false otherwise.
     */
    protected abstract boolean messageCallback(String s);
    public abstract void updateDisplay(@Nullable Text textOverride);
}
