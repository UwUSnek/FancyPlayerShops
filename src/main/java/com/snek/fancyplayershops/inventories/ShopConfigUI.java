package com.snek.fancyplayershops.inventories;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;








public class ShopConfigUI extends ScreenHandler {
    private final SimpleInventory inventory;




    public ShopConfigUI(int syncId, PlayerInventory playerInventory) {
        super(ScreenHandlerType.GENERIC_9X6, syncId);
        this.inventory = new SimpleInventory(54);

        // Add 54 storage slots
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(inventory, j + i * 9, 8 + j * 18, 18 + i * 18));
            }
        }

        // Add player inventory (27 slots)
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 140 + i * 18));
            }
        }

        // Add hotbar (9 slots)
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 198));
        }
    }




    //FIXME
    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }




    //FIXME
    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        throw new UnsupportedOperationException("Unimplemented method 'quickMove'");
    }
}