package com.snek.fancyplayershops.CustomDisplays;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.joml.Vector3f;

import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.util.math.AffineTransformation;




public abstract class CustomDisplay {
    DisplayEntity heldEntity;


    AffineTransformation defaultTransformation = new AffineTransformation(
        null,                                            // Translation (null = no change)
        null,                                            // Rotation (null = no change)
        new Vector3f(0.5f, 0.5f, 0.5f),                  // Scale (0.5x size)
        null                                             // Left rotation (null = no change)
    );


    static Method method_setTransformation;
    static {
        try {
            method_setTransformation = DisplayEntity.class.getDeclaredMethod("setTransformation", AffineTransformation.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        method_setTransformation.setAccessible(true);
    }


    public CustomDisplay(DisplayEntity _heldEntity) {
        heldEntity = _heldEntity;
        this.setTransformation(defaultTransformation);
    }


    public void setTransformation(AffineTransformation transformation) {
        try {
            method_setTransformation.invoke(heldEntity, transformation);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
