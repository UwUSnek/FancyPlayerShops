package com.snek.framework.data_types.displays;

import java.lang.reflect.Method;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector4i;

import com.snek.framework.utils.Txt;
import com.snek.framework.utils.Utils;
import com.snek.framework.utils.scheduler.Scheduler;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.DisplayEntity.TextDisplayEntity;
import net.minecraft.entity.decoration.DisplayEntity.TextDisplayEntity.TextAlignment;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.world.World;








/**
 * A wrapper for Minecraft's TextDisplayEntity.
 * This class allows for better customization and more readable code.
 */
public class CustomTextDisplay extends CustomDisplay {
    public @NotNull TextDisplayEntity getRawDisplay() { return (TextDisplayEntity)heldEntity; }


    // Text cache and flag used to remove the text when the opacity value is lower than 26
    public static final @NotNull Text EMPTY_TEXT = new Txt("").get();
    private             @NotNull Text textCache = EMPTY_TEXT;
    private final boolean noTextUnderA26;


    // Opacity cache. This is used to understand if the entity is currently interpolating from less than 26 to 26 or more
    // or vice versa, avoiding making changes until the interpolation is complete.
    private int[] lastAlpha = new int[3];
    private long lastAlphaUpdate = 0;
    private boolean lastAlphaInitialized = false;
    private void shiftLastAlpha(int _new) {
        if(lastAlphaUpdate >= Scheduler.getTickNum()) return;

        if(!lastAlphaInitialized) {
            lastAlphaInitialized = true;
            lastAlpha[2] = _new;
            lastAlpha[1] = _new;
            lastAlpha[0] = _new;
        }
        else {
            lastAlpha[2] = lastAlpha[1];
            lastAlpha[1] = lastAlpha[0];
            lastAlpha[0] = _new;
        }
    }


    /**
     * This method flushes the opacity cache and ensures the displayed text doesn't remain
     * in an incorrect state after safety delays performed during short animations.
     * Must be called at the end of each animation tick. //FIXME this can cause issues if the transition ticks are not aligned wit the step size.
     */
    public void tick(){
        shiftLastAlpha(getTextOpacity());

        if(noTextUnderA26) {
            boolean a0 = lastAlpha[0] < 26; //! Always equal to the current (or target) opacity
            boolean a1 = lastAlpha[1] < 26;
            boolean a2 = lastAlpha[2] < 26;
            if(a0 == a1 && a1 != a2) setText(textCache);
        }
    }




    // Private methods
    private static Method method_getText;
    private static Method method_getTextOpacity;
    private static Method method_getBackground;
    private static Method method_setText;
    private static Method method_setTextOpacity;
    private static Method method_setBackground;
    static {
        try {
            method_getText          = TextDisplayEntity.class.getDeclaredMethod("getText");
            method_getTextOpacity   = TextDisplayEntity.class.getDeclaredMethod("getTextOpacity");
            method_getBackground    = TextDisplayEntity.class.getDeclaredMethod("getBackground");
            method_setText          = TextDisplayEntity.class.getDeclaredMethod("setText",        Text.class);
            method_setTextOpacity   = TextDisplayEntity.class.getDeclaredMethod("setTextOpacity", byte.class);
            method_setBackground    = TextDisplayEntity.class.getDeclaredMethod("setBackground",   int.class);
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        method_getText.setAccessible(true);
        method_getTextOpacity.setAccessible(true);
        method_getBackground.setAccessible(true);
        method_setText.setAccessible(true);
        method_setTextOpacity.setAccessible(true);
        method_setBackground.setAccessible(true);
    }




    /**
     * Creates a new CustomTextDisplay using an existing TextDisplayEntity.
     * @param _rawDisplay The display entity.
     * @param _noTextUnderA26 Whether the text should not be rendered when the opacity is lower than 26,
     *     as opposed to forcing a minimum opacity value.
     */
    public CustomTextDisplay(@NotNull TextDisplayEntity _rawDisplay, boolean _noTextUnderA26) {
        super(_rawDisplay);
        noTextUnderA26 = _noTextUnderA26;
    }

    /**
     * Creates a new CustomTextDisplay in the specified world.
     * @param _world The world.
     * @param _noTextUnderA26 Whether the text should not be rendered when the opacity is lower than 26,
     *     as opposed to forcing a minimum opacity value.
     */
    public CustomTextDisplay(@NotNull World _world, boolean _noTextUnderA26) {
        super(new TextDisplayEntity(EntityType.TEXT_DISPLAY, _world));
        noTextUnderA26 = _noTextUnderA26;
    }


    /**
     * Creates a new CustomTextDisplay using an existing TextDisplayEntity.
     * @param _rawDisplay The display entity.
     */
    public CustomTextDisplay(@NotNull TextDisplayEntity _rawDisplay) {
        this(_rawDisplay, true);
    }

    /**
     * Creates a new CustomTextDisplay in the specified world.
     * @param _world The world.
     */
    public CustomTextDisplay(@NotNull World _world) {
        this(_world, true);
    }




    /**
     * Sets a new text value to the entity.
     * This is equivalent to changing the entity's "text" NBT.
     * @param text The new value.
     */
    public void setText(@NotNull Text text) {
        if(noTextUnderA26 && lastAlpha[0] < 26 && lastAlpha[1] < 26) {
            Utils.invokeSafe(method_setText, heldEntity, EMPTY_TEXT);
        }
        else {
            Utils.invokeSafe(method_setText, heldEntity, text);
        }
        textCache = text.copy();
    }


    /**
     * Retrieves the entity's text value.
     * @return The current text.
     */
    public @NotNull Text getText() {
        return textCache;
    }


    /**
     * Returns the actual text the entity is displaying, as opposed to the cached value, meaning that
     * this method returns an empty Text when noTextUnderA26 is set to true and the opacity is less than 26.
     * @return The text value.
     */
    public @NotNull Text getTrueText() {
        return (Text)Utils.invokeSafe(method_getText, heldEntity);
    }




    /**
     * Sets the alpha value of the rendered text.
     * @param a The alpha value.
     *     Values smaller than 26 are converted to 26 unless noTextUnderA26 is set to true, in which case the text not rendered at all.
     *     This is done because minecraft ignores these values and usually makes the text fully opaque instead of fully transparent, rendering animations jittery.
     * NOTICE:
     *     Interpolation is broken. Opacity values are NOT converted back to 0-255 range
     *     before interpolating, but the raw byte value (0 to 127, -128 to -1) is used instead.
     */
    public void setTextOpacity(int a) {
        shiftLastAlpha(a);
        lastAlphaUpdate = Scheduler.getTickNum();

        if(a < 26) {
            if(noTextUnderA26 && lastAlpha[1] < 26) {
                Utils.invokeSafe(method_setText, heldEntity, EMPTY_TEXT);
            }
            else {
                Utils.invokeSafe(method_setText, heldEntity, textCache);
                a = 26;
            }
        }
        else if(lastAlpha[1] >= 26 && lastAlpha[2] < 26) {
            Utils.invokeSafe(method_setText, heldEntity, textCache);
        }
        Utils.invokeSafe(method_setTextOpacity, getRawDisplay(), (byte)(a > 127 ? a - 256 : a));
    }


    /**
     * Retrieves the entity's text opacity value.
     * @return The current text opacity.
     */
    public int getTextOpacity() {
        int a = (byte)Utils.invokeSafe(method_getTextOpacity, getRawDisplay());
        return a < 0 ? a + 256 : a;
    }


    /**
     * Sets a new background color value to the entity.
     * This is equivalent to changing the entity's "background" NBT.
     * @param argb The new value.
     */
    public void setBackground(@NotNull Vector4i argb) {
        Utils.invokeSafe(method_setBackground, getRawDisplay(), (argb.x << 24) | (argb.y << 16) | (argb.z << 8) | argb.w);
    }


    /**
     * Retrieves the entity's background color value.
     * @return The current background color.
     */
    public @NotNull Vector4i getBackground() {
        int bg = (int)Utils.invokeSafe(method_getBackground, getRawDisplay());
        return new Vector4i((bg >> 24) & 0xFF, (bg >> 16) & 0xFF, (bg >> 8) & 0xFF, bg & 0xFF);
    }


    /**
     * Sets a new text alignment value to the entity.
     * This is equivalent to changing the entity's "text alignment" NBT.
     * @param alignment The new value.
     */
    public void setTextAlignment(TextAlignment alignment) {
        NbtCompound nbt = new NbtCompound();
        heldEntity.writeNbt(nbt);
        nbt.putString("alignment", alignment.asString());
        heldEntity.readNbt(nbt);
    }
}
