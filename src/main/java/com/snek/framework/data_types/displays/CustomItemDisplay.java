package com.snek.framework.data_types.displays;

import java.lang.reflect.Method;

import org.jetbrains.annotations.NotNull;

import com.snek.framework.utils.Utils;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.DisplayEntity.ItemDisplayEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;








public class CustomItemDisplay extends CustomDisplay {
    public @NotNull ItemDisplayEntity getRawDisplay() { return (ItemDisplayEntity)heldEntity; }
    static private Method method_setItemStack;
    static {
        try {
            method_setItemStack = ItemDisplayEntity.class.getDeclaredMethod("setItemStack", ItemStack.class);
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        method_setItemStack.setAccessible(true);
    }




    public CustomItemDisplay(@NotNull ItemDisplayEntity _rawDisplay) {
        super(_rawDisplay);
    }
    public CustomItemDisplay(@NotNull World _world) {
        super(new ItemDisplayEntity(EntityType.ITEM_DISPLAY, _world));
    }




    public void setItemStack(@NotNull ItemStack itemStack) {
        Utils.invokeSafe(method_setItemStack, getRawDisplay(), itemStack);
    }
}
