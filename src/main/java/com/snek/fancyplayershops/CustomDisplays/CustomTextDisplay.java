package com.snek.fancyplayershops.CustomDisplays;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.entity.decoration.DisplayEntity.BillboardMode;
import net.minecraft.entity.decoration.DisplayEntity.TextDisplayEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;




public class CustomTextDisplay extends CustomDisplay {
    TextDisplayEntity rawDisplay;
    public TextDisplayEntity getRawDisplay() { return rawDisplay; }
    static private Method method_setText;
    static private Method method_setTextOpacity;
    static private Method method_setBackground;
    static private Method method_setBillboardMode;
    static Method method_setTransformation;
    static {
        try {
            method_setText          = TextDisplayEntity.class.getDeclaredMethod("setText",                   Text.class);
            method_setTextOpacity   = TextDisplayEntity.class.getDeclaredMethod("setTextOpacity",            byte.class);
            method_setBackground   = TextDisplayEntity.class.getDeclaredMethod("setBackground",               int.class);
            method_setBillboardMode =     DisplayEntity.class.getDeclaredMethod("setBillboardMode", BillboardMode.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        method_setText.setAccessible(true);
        method_setTextOpacity.setAccessible(true);
        method_setBackground.setAccessible(true);
        method_setBillboardMode.setAccessible(true);
    }


    public CustomTextDisplay(TextDisplayEntity _rawDisplay) {
        super(_rawDisplay, 1);
        rawDisplay = _rawDisplay;
    }
    public CustomTextDisplay(World world, Text text, Vec3d pos, BillboardMode billboardMode, Boolean glowing) {
        super(new TextDisplayEntity(EntityType.TEXT_DISPLAY, world), 1);
        rawDisplay = (TextDisplayEntity)heldEntity;
        rawDisplay.setGlowing(glowing);
        rawDisplay.setPosition(pos);
        setText(text);
        setBillboardMode(billboardMode);
        world.spawnEntity(rawDisplay);
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


    public void setTextOpacity(byte opacity) {
        try {
            method_setTextOpacity.invoke(rawDisplay, opacity);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    public void setBackground(int a, int r, int g, int b) {
        try {
            method_setBackground.invoke(rawDisplay, (a << 24) | (r << 16) | (g << 8) | b);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
