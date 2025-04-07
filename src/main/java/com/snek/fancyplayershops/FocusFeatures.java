package com.snek.fancyplayershops;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.snek.framework.ui.interfaces.Hoverable;
import com.snek.framework.utils.MinecraftUtils;

import net.minecraft.entity.decoration.DisplayEntity.ItemDisplayEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.RaycastContext.ShapeType;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;








/**
 * Utility class containing methods to detect shops players are looking at and display additional informations
 */
public abstract class FocusFeatures {
    private static final double MAX_DISTANCE = 5;  // Maximum distance to check
    private static final double STEP_SIZE = 0.2;
    public static List<Shop> targetedShopsOld = new ArrayList<>();






    private static Vec3d getTargetBlockPrecise(PlayerEntity player) {
        World world = player.getEntityWorld();

        // Perform ray cast
        Vec3d eyePos = player.getEyePos();
        Vec3d lookDirection = player.getRotationVec(1.0F);
        BlockHitResult result = world.raycast(new RaycastContext(
            eyePos,
            eyePos.add(lookDirection.multiply(MAX_DISTANCE)),
            ShapeType.OUTLINE,
            RaycastContext.FluidHandling.NONE,
            player
        ));

        if (result != null) return result.getPos();
        else return null;
    }




    /**
     * Calculates the first location in which each block collides with the player's view.
     * @param player The player to cast the ray from.
     * @return A list of coordinates, one for each full block the ray collides with. Not sorted.
     */
    private static List<Vec3d> getViewCollisisons(PlayerEntity player, double maxDistance) {
        List<Vec3d> blockPositions = new ArrayList<>();
        Vec3i lastBlockPosition = new Vec3i(0, 0, 0);

        Vec3d lookDirection = player.getRotationVec(1.0F);
        Vec3d step = lookDirection.normalize().multiply(STEP_SIZE);
        Vec3d currentPos = player.getEyePos();
        double distanceTraveled = 0;


        // Manually perform ray casting
        while (distanceTraveled < maxDistance) {

            // Add block to list if not present
            Vec3i currentPosInt = new Vec3i((int)currentPos.x, (int)currentPos.y, (int)currentPos.z);
            if(lastBlockPosition != currentPosInt) {
                lastBlockPosition = currentPosInt;
                blockPositions.add(currentPos);
            }

            // Update current position and distance
            currentPos = currentPos.add(step);
            distanceTraveled += STEP_SIZE;
        }

        return blockPositions;
    }




    /**
     * Finds the closest shop block that directly collides with the player's view.
     * The ray casting is hitbox-based, so it can go through gaps in non-full blocks and it ignores fluids.
     * @param player The player.
     * @return The Shop instance of the shop block, or null if the player is not looking at one.
     */
    public static Shop getLookedAtShop(PlayerEntity player, ServerWorld world) {

        // Get the list of nearby item display entities
        final Vec3d playerEyePos = player.getEyePos();
        List<ItemDisplayEntity> entitiyList = player.getWorld().getEntitiesByClass(ItemDisplayEntity.class, Box.from(playerEyePos).expand(MAX_DISTANCE), e->true);
        if(!entitiyList.isEmpty()) {

            // Calculate ray casting max distance, then find and sort colliding blocks
            final Vec3d targetBlock = getTargetBlockPrecise(player);
            final double maxDistance = targetBlock != null ? targetBlock.distanceTo(playerEyePos) + STEP_SIZE * 1.2 : MAX_DISTANCE;
            final List<Vec3d> collidingBlocks = getViewCollisisons(player, maxDistance);
            Collections.sort(collidingBlocks, Comparator.comparingDouble(b -> b.squaredDistanceTo(playerEyePos)));

            // Find target shop block
            for (Vec3d pos : collidingBlocks) {
                Vec3i blockPos = MinecraftUtils.doubleToBlockCoords(pos);
                for (ItemDisplayEntity e : entitiyList) {
                    if(e.getBlockPos().equals(blockPos)) {
                        return Shop.findShop(new BlockPos(blockPos), world);
                    }
                }
            }
        }


        // Return null if no item display entity is near the player or no targeted shop block was found
        return null;
    }




    /**
     * Tick operations. This function spawns and removes the focus displays depending on what players are currently looking at.
     * @param serverWorlds The list of world to process. Only shops in these world are updated.
     */
    public static void tick(Iterable<ServerWorld> serverWorlds) {


        // Set all previously focused shops's next focus state to false
        for (Shop shop : targetedShopsOld) {
            shop.focusStatusNext = false;
        }


        // Find currently focused shops
        final List<Shop> targetedShops = new ArrayList<>();
        for (ServerWorld serverWorld : serverWorlds) {
            for (PlayerEntity player : serverWorld.getPlayers()) {
                Shop targetShop = FocusFeatures.getLookedAtShop(player, serverWorld);
                if(targetShop != null) {

                    // Set their next focus state to true
                    targetShop.focusStatusNext = true;
                    targetedShops.add(targetShop);

                    // Tick hoverable elements if this player is the user of the shop
                    if(targetShop.user == player) {
                        //FIXME tick the menu instead of the display
                        if(targetShop.focusDisplay != null) targetShop.focusDisplay.updateHoverStatus(player);
                    }
                }
            }
        }


        // Update the displays of all the previously and currently focused shops to their next state and update the targeted shops list
        targetedShopsOld.removeAll(targetedShops);
        for (Shop shop : targetedShopsOld) {
            if(shop.focusDisplay != null) shop.focusDisplay.updateHoverStatus(null);
            shop.updateFocusState();
        }
        for (Shop shop : targetedShops) {
            shop.updateFocusState();
            // for (ServerWorld serverWorld : serverWorlds) for (PlayerEntity player : serverWorld.getPlayers()) {

            //     //FIXME move to button element class
            //     //FIXME only check players that are currently looking at the shop
            //     if(shop.focusDisplay != null) {
            //         if(shop.focusDisplay.checkIntersection(player)) {
            //             if(!shop.focusDisplay.isHighlighted) {
            //                 shop.focusDisplay.applyAnimation(new Animation(new TextAdditiveTransition(new Transform(), 0, Easings.linear, new Vector4i(255, 200, 200, 200), 255)));
            //                 shop.focusDisplay.isHighlighted = true;
            //             }
            //         }
            //         else {
            //             if(shop.focusDisplay.isHighlighted) {
            //                 shop.focusDisplay.applyAnimation(new Animation(new TextAdditiveTransition(new Transform(), 0, Easings.linear, new Vector4i(255, 0, 0, 0), 255)));
            //                 shop.focusDisplay.isHighlighted = false;
            //             }
            //         }
            //     }
            // }
        }
        targetedShopsOld = targetedShops;
    }
}
