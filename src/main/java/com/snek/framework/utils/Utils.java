package com.snek.framework.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;








public class Utils {
    public Utils() { throw new UnsupportedOperationException("Utility class \"Utils\" cannot be instantiated"); }


    /**
     * Checks if a double value is within a certain threshold from a target value.
     * This is used to avoid precision related problems when comparing double values.
     * @param n The value to check
     * @param target The target value
     * @param threshold The threshold to use
     * @return True if the value is withing the threshold, false otherwise
     */
    public static boolean doubleEquals(final double n, final double target, final double threshold) {
        return !(n < target - threshold || n > target + threshold);
    }


    /**
     * Invokes a Method on the object target using the specified arguments.
     * @param method The method to invoke.
     * @param target The target Object.
     * @param args The arguments to use. Can be empty.
     * @return The return value of the method.
     */
    public static @Nullable Object invokeSafe(@NotNull Method method, @NotNull Object target, Object... args) {
        try {
            return method.invoke(target, args);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }




    //FIXME TextRenderer cannot be used by the server
    //FIXME find a way to calculate text width
    // public static int getTextWidth(String text) {
    //     MinecraftClient client = MinecraftClient.getInstance();
    //     if (client == null || client.textRenderer == null) {
    //         return 0;
    //     }

    //     TextRenderer renderer = client.textRenderer;
    //     return renderer.getWidth(Text.literal(text));
    // }




    /**
     * Runs a task on a secondary thread after a specified delay.
     * @param delay The delay expressed in milliseconds.
     * @param task The task to run.
     */
    public static void runAsync(int delay, @NotNull Runnable task) {
        new Thread(() -> {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            task.run();
        }).start();
    }




    /**
     * Returns the custom name of an item. If the item has no custom name, the default name is returned.
     * @param item The item.
     * @return The name of the item.
     */
    public static @NotNull Text getItemName(@NotNull ItemStack item) {
        if (item.hasCustomName()) return item.getName();
        return item.getItem().getName();
    }




    /**
     * Converts entity coordinates (double) to block coordinates (int).
     * Minecraft's block grid is weird and simply truncating the decimal part is not enough to convert coordinates.
     * @param pos
     * @return
     */
    public static @NotNull Vec3i doubleToBlockCoords(@NotNull Vec3d pos) {
        int x = pos.x < 0 ? (int)(Math.floor(pos.x) - 0.1) : (int) pos.x;
        int y = pos.y < 0 ? (int)(Math.floor(pos.y) - 0.1) : (int) pos.y;
        int z = pos.z < 0 ? (int)(Math.floor(pos.z) - 0.1) : (int) pos.z;
        return new Vec3i(x, y, z);
    }




    private static final DecimalFormat formatterPrice = new DecimalFormat("#,##0.##");

    public static @NotNull String formatPrice(double price){
        return formatPrice(price, "$", true);
    }

    /**
     * Returns the value <price> expressed as a string and formatted as specified.
     * @param price The price to format.
     * @param currency The currency symbol to use as prefix. [default: "$"]
     * @param thousandsSeparator Whether to use a separator between thousands. [default: true]
     * @return The formatted price.
     */
    public static @NotNull String formatPrice(double price, @NotNull String currency, boolean thousandsSeparator){
        String r;

        // No separator
        if(thousandsSeparator) {
            r = currency + formatterPrice.format(price);
        }

        // Separator
        else {
            r = String.format(currency + "%.2f", price);
        }

        // Add trailing 0 if there is only one decimal digit
        return r.charAt(r.length() - 2) == '.' ? r + "0" : r;
    }




    private static final DecimalFormat formatterAmount = new DecimalFormat("#,###");

    public static @NotNull String formatAmount(double amount){
        return formatAmount(amount, false, true);
    }

    /**
     * Returns the value <amount> expressed as a string and formatted as specified.
     * @param amount The amount to format.
     * @param x Whether the amount should be prefixed with a lowercase "x". [default: false]
     * @param thousandsSeparator Whether to use a separator between thousands. [default: true]
     * @return The formatted price.
     */
    public static @NotNull String formatAmount(double amount, boolean x, boolean thousandsSeparator){
        String r;
        if(thousandsSeparator) {
            r = formatterAmount.format(amount);
        }
        else {
            r = String.valueOf(amount);
        }
        return x ? "x" + r : r;
    }





    public static @NotNull Vector3f RGBtoHSV(@NotNull Vector3f rgb) {
        float r = rgb.x / 255.0f;
        float g = rgb.y / 255.0f;
        float b = rgb.z / 255.0f;

        float max = Math.max(r, Math.max(g, b));
        float min = Math.min(r, Math.min(g, b));
        float h = 0, s, v = max;

        float delta = max - min;

        if (max != 0) {
            s = delta / max;
        } else {
            s = 0;
            h = -1;
            return new Vector3f(h, s, v);
        }

        if (r == max) {
            h = (g - b) / delta;
        } else if (g == max) {
            h = 2 + (b - r) / delta;
        } else {
            h = 4 + (r - g) / delta;
        }

        h *= 60;
        if (h < 0) h += 360;

        return new Vector3f(h, s, v);
    }




    public static Vector3f HSVtoRGB(Vector3f hsv) {
        float h = hsv.x;
        float s = hsv.y;
        float v = hsv.z;

        float c = v * s;
        float x = c * (1 - Math.abs(((h / 60) % 2) - 1));
        float m = v - c;

        float r = 0, g = 0, b = 0;

        if (0 <= h && h < 60) {
            r = c; g = x; b = 0;
        } else if (60 <= h && h < 120) {
            r = x; g = c; b = 0;
        } else if (120 <= h && h < 180) {
            r = 0; g = c; b = x;
        } else if (180 <= h && h < 240) {
            r = 0; g = x; b = c;
        } else if (240 <= h && h < 300) {
            r = x; g = 0; b = c;
        } else if (300 <= h && h < 360) {
            r = c; g = 0; b = x;
        }

        r += m; g += m; b += m;

        return new Vector3f(r * 255, g * 255, b * 255);
    }
}
