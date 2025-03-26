package com.snek.fancyplayershops.Containers;

import com.snek.framework.utils.Txt;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;




public class ShopConfigUI_Factory implements NamedScreenHandlerFactory {
    @Override
    public Text getDisplayName() {
        return new Txt("test ui by snek").get();
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new ShopConfigUI(syncId, playerInventory);
    }
}
