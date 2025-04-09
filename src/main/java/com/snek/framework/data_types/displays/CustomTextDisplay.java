package com.snek.framework.data_types.displays;

import java.lang.reflect.Method;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector4i;

import com.snek.framework.utils.Utils;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.DisplayEntity.TextDisplayEntity;
import net.minecraft.entity.decoration.DisplayEntity.TextDisplayEntity.TextAlignment;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.Text;
import net.minecraft.world.World;








public class CustomTextDisplay extends CustomDisplay {
    public @NotNull TextDisplayEntity getRawDisplay() { return (TextDisplayEntity)heldEntity; }


    private static Method method_setText;
    private static Method method_getText;
    private static Method method_setTextOpacity;
    private static Method method_getTextOpacity;
    private static Method method_setBackground;
    private static Method method_getBackground;
    static {
        try {
            method_setText          = TextDisplayEntity.class.getDeclaredMethod("setText",                   Text.class);
            method_getText          = TextDisplayEntity.class.getDeclaredMethod("getText");
            method_setTextOpacity   = TextDisplayEntity.class.getDeclaredMethod("setTextOpacity",            byte.class);
            method_getTextOpacity   = TextDisplayEntity.class.getDeclaredMethod("getTextOpacity");
            method_setBackground    = TextDisplayEntity.class.getDeclaredMethod("setBackground",              int.class);
            method_getBackground    = TextDisplayEntity.class.getDeclaredMethod("getBackground");
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        method_setText.setAccessible(true);
        method_getText.setAccessible(true);
        method_setTextOpacity.setAccessible(true);
        method_getTextOpacity.setAccessible(true);
        method_setBackground.setAccessible(true);
        method_getBackground.setAccessible(true);
    }




    public CustomTextDisplay(@NotNull TextDisplayEntity _rawDisplay) {
        super(_rawDisplay);
    }
    public CustomTextDisplay(@NotNull World _world) {
        super(new TextDisplayEntity(EntityType.TEXT_DISPLAY, _world));
    }




    public void setText(@NotNull Text text) {
        Utils.invokeSafe(method_setText, heldEntity, text);
    }


    public @NotNull Text getText() {
        return (Text)Utils.invokeSafe(method_getText, heldEntity);
    }




    /**
     * Sets the alpha value of the rendered text.
     * @param a The alpha value.
     *     Values smaller than 26 are converted to 26.
     *     This is because minecraft ignores these values and usually makes the text
     *     fully opaque instead of fully transparent, rendering animations jittery.
     * NOTICE:
     *     Interpolation is broken. Opacity values are NOT converted back to 0-255 range
     *     before interpolating, but the raw byte value (0 to 127, -128 to -1) is used instead.
     */
    public void setTextOpacity(int a) {
        int a2 = Math.max(26, a);
        Utils.invokeSafe(method_setTextOpacity, getRawDisplay(), (byte)(a2 > 127 ? a2 - 256 : a2));
    }




    public int getTextOpacity() {
        int a = (byte)Utils.invokeSafe(method_getTextOpacity, getRawDisplay());
        return a < 0 ? a + 256 : a;
    }




    public void setBackground(@NotNull Vector4i argb) {
        Utils.invokeSafe(method_setBackground, getRawDisplay(), (argb.x << 24) | (argb.y << 16) | (argb.z << 8) | argb.w);
    }




    public @NotNull Vector4i getBackground() {
        int bg = (int)Utils.invokeSafe(method_getBackground, getRawDisplay());
        return new Vector4i((bg >> 24) & 0xFF, (bg >> 16) & 0xFF, (bg >> 8) & 0xFF, bg & 0xFF);
    }




    public void setTextAlignment(TextAlignment alignment) {
        NbtCompound nbt = new NbtCompound();
        heldEntity.writeNbt(nbt);
        nbt.putString("alignment", alignment.asString());
        heldEntity.readNbt(nbt);
    }
}
