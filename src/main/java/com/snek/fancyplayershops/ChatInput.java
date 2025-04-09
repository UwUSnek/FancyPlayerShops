package com.snek.fancyplayershops;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.message.SignedMessage;








public abstract class ChatInput {
    private ChatInput(){}


    private static @NotNull Map<UUID, Predicate<String>> callbacks = new HashMap<>();


    public static void removeCallback(@NotNull PlayerEntity player) {
        callbacks.remove(player.getUuid());
    }


    /**
     * Sets a callback that is fired the next time the player sends a message in chat chat.
     * Commands are ignored.
     * @param player The player.
     * @param callback The callback function. It must take a String and return a boolean.
     *     The return value controls whether the message is blocked.
     *     Returning true will let the server broadcast the message in chat.
     */
    public static void setCallback(@NotNull PlayerEntity player, @NotNull Predicate<String> callback) {
        callbacks.put(player.getUuid(), callback);
    }


    /**
     * Runs the callback associated with the player and either blocks or broadcasts the message based on the callback's return value.
     * @param message The message.
     * @param player The player that sent the message.
     * @return Whether the message should be blocked.
     */
    public static boolean onMessage(SignedMessage message, PlayerEntity player) {
        Predicate<String> callback = callbacks.get(player.getUuid());
        if(callback != null) {
            if(callback.test(message.getContent().getString())) {
                callbacks.remove(player.getUuid());
                return true;
            }
            return false;
        }
        return false;
    }
}
