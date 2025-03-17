package com.snek.fancyplayershops.CustomDisplays;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.DisplayEntity.TextDisplayEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;




public class CustomTextDisplay {
    TextDisplayEntity rawDisplay;
    public TextDisplayEntity getRawDisplay() { return rawDisplay; }
    Method method_setText;


    public CustomTextDisplay(World world) {
        rawDisplay = new TextDisplayEntity(EntityType.TEXT_DISPLAY, world);
        try {
            method_setText = rawDisplay.getClass().getDeclaredMethod("setText", Text.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        method_setText.setAccessible(true);
    }


    public void setText(Text text) {
        try {
            method_setText.invoke(rawDisplay, text);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
