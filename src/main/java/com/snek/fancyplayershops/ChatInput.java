package com.snek.fancyplayershops;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.message.SignedMessage;








/**
 * A utility class that can detect player messages and execute callbacks based on the sender.
 */
public abstract class ChatInput {
    private ChatInput(){}
    private static @NotNull Map<UUID, Predicate<String>> callbacks = new HashMap<>();


    /**
     * Removes the callback for the specified player.
     * Future messages from this player will not be detected nor blocked.
     * @param player The player.
     */
    public static void removeCallback(@NotNull PlayerEntity player) {
        callbacks.remove(player.getUuid());
    }


    /**
     * Sets a callback that is fired the next time the player sends a message in chat.
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
        Predicate<String> callback = callbacks.get(player.getUuid());   // Find callback for the specified player
        if(callback != null) {                                          // If present
            if(callback.test(message.getContent().getString())) {           // Execute the callback. If it returns true
                callbacks.remove(player.getUuid());                             // Remove the callback
                return true;                                                    // Block the message
            }                                                               // If not
            return false;                                                       // Broadcast the message
        }

        // Broadcast the message if the player has no associated callback
        return false;
    }
}
