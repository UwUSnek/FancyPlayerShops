package com.snek.fancyplayershops.CustomDisplays;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.jetbrains.annotations.NotNull;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.DisplayEntity.ItemDisplayEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;








public class CustomItemDisplay extends CustomDisplay {

    protected @NotNull ItemDisplayEntity rawDisplay;
    public @NotNull ItemDisplayEntity getRawDisplay() { return rawDisplay; }
    static private Method method_setItemStack;
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




    public CustomItemDisplay(@NotNull ItemDisplayEntity _rawDisplay, @NotNull AnimationData _animation) {
        super(_rawDisplay, new Transform(), _animation);
        rawDisplay = _rawDisplay;
    }
    public CustomItemDisplay(@NotNull World world, @NotNull Vec3d pos, @NotNull Transform _defaultTransform, @NotNull ItemStack itemStack, boolean customNameVisible, boolean glowing, @NotNull AnimationData _animation) {
        super(new ItemDisplayEntity(EntityType.ITEM_DISPLAY, world), _defaultTransform, _animation);
        rawDisplay = (ItemDisplayEntity)heldEntity;
        setItemStack(itemStack);
        rawDisplay.setCustomNameVisible(customNameVisible);
        rawDisplay.setGlowing(glowing);
        rawDisplay.setPosition(pos);
    }




    public void setItemStack(@NotNull ItemStack itemStack) {
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
