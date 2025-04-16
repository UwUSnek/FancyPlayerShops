package com.snek.framework.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.joml.Vector4i;








/**
 * A utility class that provides a collection of useful methods.
 */
public abstract class Utils {
    private Utils(){}




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
                Thread.currentThread().interrupt();
            }
            task.run();
        }).start();
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
            r = String.format("%s%.2f", currency, price);
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





    /**
     * Converts an RGB color to HSV.
     *     Hue:         0 to 360.0
     *     Saturation:  0 to 1.0
     *     Value:       0 to 1.0
     * @param rgb The RGB color.
     * @return The color as an HSV value.
     */
    public static @NotNull Vector3f RGBtoHSV(@NotNull Vector3i rgb) {
        float r = rgb.x / 255.0f;
        float g = rgb.y / 255.0f;
        float b = rgb.z / 255.0f;

        float max = Math.max(r, Math.max(g, b));
        float min = Math.min(r, Math.min(g, b));
        float h = 0;
        float s;
        float v = max;

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




    /**
     * Converts an HSV color to RGB.
     *     Red:   0 to 255
     *     Green: 0 to 255
     *     Blue:  0 to 255
     * @param hsv The HSV color.
     * @return The color as an HSV value.
     */
    public static Vector3i HSVtoRGB(Vector3f hsv) {
        float h = hsv.x;
        float s = hsv.y;
        float v = hsv.z;

        float c = v * s;
        float x = c * (1 - Math.abs(((h / 60) % 2) - 1));
        float m = v - c;

        float r = 0;
        float g = 0;
        float b = 0;

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

        return new Vector3i(Math.round(r * 255), Math.round(g * 255), Math.round(b * 255));
    }




    /**
     * Interpolates two RGB colors while maintaining luminosity.
     * @param rgb1 The starting color.
     * @param rgb2 The target color
     * @param factor The interpolation factor.
     * @return The resulting color.
     */
    public static Vector3i interpolateRGB(Vector3i rgb1, Vector3i rgb2, float factor) {
        Vector3f hsv1 = RGBtoHSV(rgb1);
        Vector3f hsv2 = RGBtoHSV(rgb2);

        float h1 = hsv1.x;
        float s1 = hsv1.y;
        float v1 = hsv1.z;

        float h2 = hsv2.x;
        float s2 = hsv2.y;
        float v2 = hsv2.z;

        // Adjust hue to allow the interpolation to take the shortest path
        if (Math.abs(h1 - h2) > 180) {
            if (h1 > h2) h2 += 360;
            else h1 += 360;
        }

        // Interpolate values and return color vector
        return HSVtoRGB(new Vector3f(
            interpolateF(h1, h2, factor) % 360,
            interpolateF(s1, s2, factor),
            interpolateF(v1, v2, factor)
        ));
    }




    /**
     * Interpolates two ARGB colors while maintaining luminosity.
     * @param argb1 The starting color.
     * @param argb2 The target color
     * @param factor The interpolation factor.
     * @return The resulting color.
     */
    public static Vector4i interpolateARGB(Vector4i argb1, Vector4i argb2, float factor) {
        Vector3i rgbRet = interpolateRGB(new Vector3i(argb1.y, argb1.z, argb1.w), new Vector3i(argb2.y, argb2.z, argb2.w), factor);
        return new Vector4i(interpolateI(argb1.x, argb2.x, factor), rgbRet.x, rgbRet.y, rgbRet.z);
    }




    /**
     * Interpolates two float values.
     * @param v1 The first value.
     * @param v2 The second value.
     * @param factor The interpolation factor.
     * @return The resulting value.
     */
    public static float interpolateF(float v1, float v2, float factor){
        return v1 + (v2 - v1) * factor;
    }




    /**
     * Interpolates two double values.
     * @param v1 The first value.
     * @param v2 The second value.
     * @param factor The interpolation factor.
     * @return The resulting value.
     */
    public static double interpolateF(double v1, double v2, double factor){
        return v1 + (v2 - v1) * factor;
    }




    /**
     * Interpolates two int values.
     * @param v1 The first value.
     * @param v2 The second value.
     * @param factor The interpolation factor.
     * @return The resulting value.
     */
    public static int interpolateI(int v1, int v2, float factor){
        return Math.round(v1 + (v2 - v1) * factor);
    }
}
