package com.snek.fancyplayershops.Containers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;




public class ShopConfigUI_Factory implements NamedScreenHandlerFactory {
    @Override
    public Text getDisplayName() {
        return Text.literal("test ui by snek");
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new ShopConfigUI(syncId, playerInventory);
    }
}
