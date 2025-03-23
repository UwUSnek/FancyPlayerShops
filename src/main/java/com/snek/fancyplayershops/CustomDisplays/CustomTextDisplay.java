package com.snek.fancyplayershops.CustomDisplays;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector4i;

import com.snek.fancyplayershops.utils.Scheduler;
import com.snek.fancyplayershops.utils.TaskHandler;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.entity.decoration.DisplayEntity.BillboardMode;
import net.minecraft.entity.decoration.DisplayEntity.TextDisplayEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;








public class CustomTextDisplay extends CustomDisplay {
    private List<TaskHandler> textOpacityHandlers = new ArrayList<>();
    private List<TaskHandler> backgroundHandlers = new ArrayList<>();
    protected TextDisplayEntity rawDisplay;
    public TextDisplayEntity getRawDisplay() { return rawDisplay; }


    static private Method method_setText;
    static private Method method_setTextOpacity;
    static private Method method_getTextOpacity;
    static private Method method_setBackground;
    static private Method method_getBackground;
    static private Method method_setBillboardMode;
    static {
        try {
            method_setText          = TextDisplayEntity.class.getDeclaredMethod("setText",                   Text.class);
            method_setTextOpacity   = TextDisplayEntity.class.getDeclaredMethod("setTextOpacity",            byte.class);
            method_getTextOpacity   = TextDisplayEntity.class.getDeclaredMethod("getTextOpacity");
            method_setBackground    = TextDisplayEntity.class.getDeclaredMethod("setBackground",              int.class);
            method_getBackground    = TextDisplayEntity.class.getDeclaredMethod("getBackground");
            method_setBillboardMode =     DisplayEntity.class.getDeclaredMethod("setBillboardMode", BillboardMode.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        method_setText.setAccessible(true);
        method_setTextOpacity.setAccessible(true);
        method_getTextOpacity.setAccessible(true);
        method_setBackground.setAccessible(true);
        method_getBackground.setAccessible(true);
        method_setBillboardMode.setAccessible(true);
    }




    public CustomTextDisplay(TextDisplayEntity _rawDisplay, DisplayAnimation _animation) {
        super(_rawDisplay, 1, _animation);
        rawDisplay = _rawDisplay;
    }
    public CustomTextDisplay(World world, Text text, Vec3d pos, BillboardMode billboardMode, Boolean glowing, DisplayAnimation _animation) {
        super(new TextDisplayEntity(EntityType.TEXT_DISPLAY, world), 0.5f, _animation);
        rawDisplay = (TextDisplayEntity)heldEntity;
        rawDisplay.setGlowing(glowing);
        rawDisplay.setPosition(pos);
        setText(text);
        setBillboardMode(billboardMode);
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




    /**
     * Sets the alpha value of the rendered text.
     * @param a The alpha value.
     *     Values smaller than 26 are converted to 26.
     *     This is because minecraft ignores these values and usually makes the text
     *     fully opaque instead of fully transparent, rendering animations jittery.
     */
    public void setTextOpacity(int a) {
        try {
            int a2 = Math.max(26, a);
            method_setTextOpacity.invoke(rawDisplay, (byte)(a2 > 127 ? a2 - 256 : a2));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }




    public int getTextOpacity() {
        try {
            int a = (int)(byte)method_getTextOpacity.invoke(rawDisplay);
            return a < 0 ? a + 256 : a;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return 0;
    }




    public void setBackground(Vector4i argb) {
        try {
            method_setBackground.invoke(rawDisplay, (argb.x << 24) | (argb.y << 16) | (argb.z << 8) | argb.w);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }




    public Vector4i getBackground() {
        try {
            int bg = (int)method_getBackground.invoke(rawDisplay);
            return new Vector4i((bg >> 24) & 0xFF, (bg >> 16) & 0xFF, (bg >> 8) & 0xFF, bg & 0xFF);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return new Vector4i(0);
    }
}
