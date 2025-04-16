package com.snek.framework.data_types.displays;

import java.lang.reflect.Method;

import org.jetbrains.annotations.NotNull;

import com.snek.framework.utils.Utils;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.DisplayEntity.ItemDisplayEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;








/**
 * A wrapper for Minecraft's ItemDisplayEntity.
 * This class allows for better customization and more readable code.
 */
public class CustomItemDisplay extends CustomDisplay {
    public @NotNull ItemDisplayEntity getRawDisplay() { return (ItemDisplayEntity)heldEntity; }


    // Private methods
    private static Method method_setItemStack;
    static {
        try {
            method_setItemStack = ItemDisplayEntity.class.getDeclaredMethod("setItemStack", ItemStack.class);
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        method_setItemStack.setAccessible(true);
    }




    /**
     * Creates a new CustomItemDisplay using an existing ItemDisplayEntity.
     * @param _rawDisplay The display entity.
     */
    public CustomItemDisplay(@NotNull ItemDisplayEntity _rawDisplay) {
        super(_rawDisplay);
    }

    /**
     * Creates a new CustomItemDisplay in the specified world.
     * @param _world The world.
     */
    public CustomItemDisplay(@NotNull World _world) {
        super(new ItemDisplayEntity(EntityType.ITEM_DISPLAY, _world));
    }




    /**
     *
     * Sets a new item stack value to the entity.
     * This is equivalent to changing the entity's "item" NBT.
     * @param itemStack The new value.
     */
    public void setItemStack(@NotNull ItemStack itemStack) {
        Utils.invokeSafe(method_setItemStack, getRawDisplay(), itemStack);
    }
}
