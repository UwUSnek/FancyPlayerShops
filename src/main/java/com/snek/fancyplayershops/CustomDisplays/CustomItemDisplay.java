package com.snek.fancyplayershops.CustomDisplays;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.DisplayEntity.ItemDisplayEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;




public class CustomItemDisplay extends CustomDisplay {

    ItemDisplayEntity rawDisplay;
    public ItemDisplayEntity getRawDisplay() { return rawDisplay; }
    static Method method_setItemStack;
    static {
        try {
            method_setItemStack = ItemDisplayEntity.class.getDeclaredMethod("setItemStack", ItemStack.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        method_setItemStack.setAccessible(true);
    }


    public CustomItemDisplay(World world) {
        super(new ItemDisplayEntity(EntityType.ITEM_DISPLAY, world));
        rawDisplay = (ItemDisplayEntity)heldEntity;
    }


    public void setItemStack(ItemStack itemStack) {
        try {
            method_setItemStack.invoke(rawDisplay, itemStack);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
