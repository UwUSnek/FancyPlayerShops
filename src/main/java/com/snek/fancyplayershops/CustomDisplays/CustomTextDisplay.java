package com.snek.fancyplayershops.CustomDisplays;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.joml.Vector3f;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.entity.decoration.DisplayEntity.BillboardMode;
import net.minecraft.entity.decoration.DisplayEntity.TextDisplayEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.AffineTransformation;
import net.minecraft.world.World;




public class CustomTextDisplay {
    AffineTransformation defaultTransformation = new AffineTransformation(
        null,  // Translation (null = no change)
        null,  // Rotation (null = no change)
        new Vector3f(0.5f, 0.5f, 0.5f), // Scale (0.5x size)
        null   // Left rotation (null = no change)
    );

    TextDisplayEntity rawDisplay;
    public TextDisplayEntity getRawDisplay() { return rawDisplay; }
    static private Method method_setText;
    static private Method method_setBillboardMode;
    static Method method_setTransformation;
    static {
        try {
            method_setText          = TextDisplayEntity.class.getDeclaredMethod("setText",                   Text.class);
            method_setBillboardMode =     DisplayEntity.class.getDeclaredMethod("setBillboardMode", BillboardMode.class);
            method_setTransformation = DisplayEntity.class.getDeclaredMethod("setTransformation", AffineTransformation.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        method_setText.setAccessible(true);
        method_setBillboardMode.setAccessible(true);
        method_setTransformation.setAccessible(true);
    }


    public CustomTextDisplay(World world) {
        rawDisplay = new TextDisplayEntity(EntityType.TEXT_DISPLAY, world);
        this.setTransformation(defaultTransformation);
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


    public void setBillboardMode(BillboardMode billboardMode) {
        try {
            method_setBillboardMode.invoke(rawDisplay, billboardMode);
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
