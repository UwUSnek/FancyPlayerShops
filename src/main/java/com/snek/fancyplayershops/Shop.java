package com.snek.fancyplayershops;

import com.snek.fancyplayershops.CustomDisplays.CustomItemDisplay;
import com.snek.fancyplayershops.CustomDisplays.CustomTextDisplay;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardCriterion.RenderType;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;




public class Shop {
    private CustomTextDisplay textDisplay;
    private CustomItemDisplay itemDisplay;
    private BlockPos pos;

    private ItemStack item = null;
    private double price = 0;
    private int stock = 0;

    public Shop(World world, BlockPos _pos){
        pos = _pos;

        // // Create and setup the Text Display entity
        // textDisplay = new CustomTextDisplay(world);
        // textDisplay.getRawDisplay().setPosition(pos.getX(), pos.getY() + 0.4, pos.getZ());
        // textDisplay.setText(Text.of("Empty shop\n$0"));
        // textDisplay.setBillboardMode(BillboardMode.CENTER);
        // textDisplay.getRawDisplay().setGlowing(true);
        // world.spawnEntity(textDisplay.getRawDisplay());


        // Create and setup the Item Display entity
        itemDisplay = new CustomItemDisplay(world);
        itemDisplay.getRawDisplay().setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        itemDisplay.setItemStack(new ItemStack(Items.BARRIER, 1));
        itemDisplay.getRawDisplay().setGlowing(true);
        itemDisplay.getRawDisplay().setCustomName(Text.of("Empty shop"));
        itemDisplay.getRawDisplay().setCustomNameVisible(true);
        world.spawnEntity(itemDisplay.getRawDisplay());
    }
}
