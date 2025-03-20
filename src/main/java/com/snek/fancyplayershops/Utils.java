package com.snek.fancyplayershops;

import org.apache.logging.log4j.core.config.builder.api.Component;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;








public class Utils {
    public Utils() { throw new UnsupportedOperationException("Utility class \"FocusFeatures\" cannot be instantiated"); }




    /**
     * Returns the custom name of an item. If the item has no custom name, the default name is returned.
     * @param item The item.
     * @return The name of the item.
     */
    public static Text getItemName(ItemStack item) {
        if (item.hasCustomName()) return item.getName();
        return item.getItem().getName();
    }
}
