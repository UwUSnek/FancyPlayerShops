package com.snek.fancyplayershops.CustomDisplays;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.joml.Vector3f;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.entity.decoration.DisplayEntity.ItemDisplayEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AffineTransformation;
import net.minecraft.world.World;




public class CustomItemDisplay {
    AffineTransformation defaultTransformation = new AffineTransformation(
        null,  // Translation (null = no change)
        null,  // Rotation (null = no change)
        new Vector3f(0.5f, 0.5f, 0.5f), // Scale (0.5x size)
        null   // Left rotation (null = no change)
    );

    ItemDisplayEntity rawDisplay;
    public ItemDisplayEntity getRawDisplay() { return rawDisplay; }
    static Method method_setItemStack;
    static Method method_setTransformation;
    static {
        try {
            method_setItemStack = ItemDisplayEntity.class.getDeclaredMethod("setItemStack", ItemStack.class);
            method_setTransformation = DisplayEntity.class.getDeclaredMethod("setTransformation", AffineTransformation.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        method_setItemStack.setAccessible(true);
        method_setTransformation.setAccessible(true);
    }


    public CustomItemDisplay(World world) {
        rawDisplay = new ItemDisplayEntity(EntityType.ITEM_DISPLAY, world);
        this.setTransformation(defaultTransformation);
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


    public void setTransformation(AffineTransformation transformation) {
        try {
            method_setTransformation.invoke(rawDisplay, transformation);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
