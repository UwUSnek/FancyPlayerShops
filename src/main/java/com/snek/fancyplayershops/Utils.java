package com.snek.fancyplayershops;

import java.text.DecimalFormat;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;








public class Utils {
    public Utils() { throw new UnsupportedOperationException("Utility class \"FocusFeatures\" cannot be instantiated"); }




    /**
     * Runs a task on a secondary thread after a specified delay.
     * @param delay The delay expressed in milliseconds.
     * @param task The task to run.
     */
    public static void runAsync(int delay, Runnable task) {
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
    public static Text getItemName(ItemStack item) {
        if (item.hasCustomName()) return item.getName();
        return item.getItem().getName();
    }




    /**
     * Converts entity coordinates (double) to block coordinates (int).
     * Minecraft's block grid is weird and simply truncating the decimal part is not enough to convert coordinates.
     * @param pos
     * @return
     */
    public static Vec3i doubleToBlockCoords(Vec3d pos) {
        int x = pos.x < 0 ? (int)(Math.floor(pos.x) - 0.1) : (int) pos.x;
        int y = pos.y < 0 ? (int)(Math.floor(pos.y) - 0.1) : (int) pos.y;
        int z = pos.z < 0 ? (int)(Math.floor(pos.z) - 0.1) : (int) pos.z;
        return new Vec3i(x, y, z);
    }




    private static final DecimalFormat formatterPrice = new DecimalFormat("#,###.00");

    public static String formatPrice(double price){
        return formatPrice(price, "$", true);
    }

    /**
     * Returns the value <price> expressed as a string and formatted as specified.
     * @param price The price to format.
     * @param currency The currency symbol to use as prefix. [default: "$"]
     * @param thousandsSeparator Whether to use a separator between thousands. [default: true]
     * @return The formatted price.
     */
    public static String formatPrice(double price, String currency, Boolean thousandsSeparator){
        if(thousandsSeparator) {
            return currency + formatterPrice.format(price);
        }
        else {
            return String.format(currency + "%.2f", price);
        }
    }




    private static final DecimalFormat formatterAmount = new DecimalFormat("#,###");

    public static String formatAmount(double amount){
        return formatAmount(amount, false, true);
    }

    /**
     * Returns the value <amount> expressed as a string and formatted as specified.
     * @param amount The amount to format.
     * @param x Whether the amount should be prefixed with a lowercase "x". [default: false]
     * @param thousandsSeparator Whether to use a separator between thousands. [default: true]
     * @return The formatted price.
     */
    public static String formatAmount(double amount, Boolean x, Boolean thousandsSeparator){
        String r;
        if(thousandsSeparator) {
            r = formatterAmount.format(amount);
        }
        else {
            r = String.valueOf(amount);
        }
        return x ? "x" + r : r;
    }
}
