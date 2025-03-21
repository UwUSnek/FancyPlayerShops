package com.snek.fancyplayershops.CustomDisplays;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.joml.Vector3f;

import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.util.math.AffineTransformation;




public abstract class CustomDisplay {
    DisplayEntity heldEntity;
    AffineTransformation defaultTransformation;
    DisplayAnimation animation;


    static Method method_setTransformation;
    static Method method_setInterpolationDuration;
    static Method method_setStartInterpolation;
    static {
        try {
            method_setTransformation        = DisplayEntity.class.getDeclaredMethod("setTransformation", AffineTransformation.class);
            method_setInterpolationDuration = DisplayEntity.class.getDeclaredMethod("setInterpolationDuration",           int.class);
            method_setStartInterpolation    = DisplayEntity.class.getDeclaredMethod("setInterpolationDuration",           int.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        method_setTransformation.setAccessible(true);
        method_setInterpolationDuration.setAccessible(true);
        method_setStartInterpolation.setAccessible(true);
    }


    public CustomDisplay(DisplayEntity _heldEntity, float scale, DisplayAnimation _animation) {
        defaultTransformation = new AffineTransformation(
            null,                              // Translation
            null,                              // Rotation
            new Vector3f(scale, scale, scale), // Scale
            null                               // Left rotation
        );
        heldEntity = _heldEntity;
        animation = _animation;
        this.setTransformation(defaultTransformation);
    }


    /**
     * Applies a new transformation to the display and starts the linear interpolation at the end of the current tick.
     * @param transformation The transformation to apply.
     * @param duration The duration of the interpolation.
     */
    public void applyTransform(AffineTransformation transformation, int duration) {
        setStartInterpolation();
        setInterpolationDuration(duration);
        setTransformation(transformation);
    }


    private void setTransformation(AffineTransformation transformation) {
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


    /**
     * @param duration The duration in ticks
     */
    private void setInterpolationDuration(int duration) {
        try {
            method_setInterpolationDuration.invoke(heldEntity, duration);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    private void setStartInterpolation() {
        try {
            method_setStartInterpolation.invoke(heldEntity, -1);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
