// package com.snek.fancyplayershops.mixin;

// import org.spongepowered.asm.mixin.Mixin;
// import org.spongepowered.asm.mixin.injection.At;
// import org.spongepowered.asm.mixin.injection.Inject;
// import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// import net.minecraft.entity.player.PlayerEntity;
// import net.minecraft.inventory.CraftingInventory;
// import net.minecraft.inventory.CraftingResultInventory;
// import net.minecraft.item.ItemStack;
// import net.minecraft.item.Items;
// import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
// import net.minecraft.screen.CraftingScreenHandler;
// import net.minecraft.server.network.ServerPlayerEntity;
// import net.minecraft.text.Text;
// import net.minecraft.world.World;




// @Mixin(CraftingScreenHandler.class)
// public class RecipeModifier {
//     @Inject(method = "updateResult", at = @At("RETURN"))
//     private static void modifyCraftingResult(int syncId, World world, PlayerEntity player, CraftingInventory inventory, CraftingResultInventory result, CallbackInfo ci) {
//         ItemStack originalResult = result.getStack(0);

//         if (originalResult.getItem() == Items.DIAMOND_SWORD) {
//             ItemStack modifiedResult = originalResult.copy();
//             modifiedResult.setCustomName(Text.of("Custom Sword"));
//             result.setStack(0, modifiedResult);

//             // ((ServerPlayerEntity) player).networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(syncId, 0, modifiedResult));
//         }
//     }
// }