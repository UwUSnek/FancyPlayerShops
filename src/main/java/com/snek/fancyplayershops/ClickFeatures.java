package com.snek.fancyplayershops;

import com.snek.framework.utils.Txt;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;








public class ClickFeatures {
    public ClickFeatures() { throw new UnsupportedOperationException("Utility class \"ClickFeatures\" cannot be instantiated"); }



    //FIXME use a single onClick event and pass the click type
    /**
     * Handles left and right clicks on shop blocks.
     * Must be called on AttackBlockCallback and UseBlockCallback events.
     * @param world The world the player is in.
     * @param player The player.
     * @param hand The hand used.
     * @param clickType The type of click (LEFT click or RIGHT click)
     * @return SUCCESS if the player clicked a shop, PASS if not.
     */
    public static ActionResult onClick(World world, PlayerEntity player, Hand hand, ClickType clickType) {
        if(hand == Hand.MAIN_HAND && world instanceof ServerWorld serverWorld) {
            Shop targetShop = FocusFeatures.getLookedAtShop(player, serverWorld); //FIXME replace with lookup map (UUID, Shop) set by the focus system
            if(targetShop != null) {
                boolean isOwner = targetShop.ownerUUID.equals(player.getUuid());
                if(targetShop.user == null || targetShop.user == player) {
                    // if(targetShop.menuStatus == MenuStatus.DETAILS) {
                    //     if(clickType == ClickType.RIGHT) {
                    //         targetShop.user = player;
                    //         if(isOwner) {
                    //             targetShop.menuStatusNext = MenuStatus.OWNER_EDIT;
                    //             targetShop.updateMenuStatus();
                    //         }
                    //         else {
                    //             targetShop.menuStatusNext = MenuStatus.CLIENT_BUY;
                    //             targetShop.updateMenuStatus();
                    //         }
                    //     }
                    //     else {
                    //         //TODO retrieve or buy 1
                    //     }
                    // }
                    // else if(targetShop.menuStatus == MenuStatus.OWNER_EDIT) {
                    //     if(clickType == ClickType.RIGHT) {
                    //         targetShop.user = null;
                    //         targetShop.menuStatusNext = MenuStatus.DETAILS;
                    //         targetShop.updateMenuStatus();
                    //     }

                    //     //TODO manage UI components click events
                    //     //TODO click the small item display to change. maybe add a background color too
                    //     //TODO click the set price button and enter the price in chat
                    //     //TODO click the set max stock button and enter the max stock in chat
                    //     //TODO click the arrows to change item Y rotation
                    // }
                    // else if(targetShop.menuStatus == MenuStatus.CLIENT_BUY) {
                    //     targetShop.menuStatusNext = MenuStatus.DETAILS;
                    //     targetShop.updateMenuStatus();

                    //     //TODO manage UI components click events
                    //     //TODO buttons to buy set amounts: 1000, 100, 10, 5, 1
                    // }
                }
                else {
                    player.sendMessage(new Txt("Someone else is already using this shop!\nLeft click to " + (
                        isOwner ? "retrieve" : "buy"
                    ) + " one item.").lightGray().italic().get());
                }
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }
}
