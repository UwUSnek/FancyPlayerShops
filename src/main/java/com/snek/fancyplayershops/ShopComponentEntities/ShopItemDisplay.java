package com.snek.fancyplayershops.ShopComponentEntities;

import java.util.List;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.snek.fancyplayershops.CustomDisplays.CustomItemDisplay;
import com.snek.fancyplayershops.CustomDisplays.DisplayAnimation;
import com.snek.fancyplayershops.CustomDisplays.Transform;
import com.snek.fancyplayershops.CustomDisplays.TransformTransition;
import com.snek.fancyplayershops.utils.Scheduler;
import com.snek.fancyplayershops.utils.Utils;

import net.minecraft.entity.decoration.DisplayEntity.ItemDisplayEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AffineTransformation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;








public class ShopItemDisplay extends CustomItemDisplay {

    public void setDefaultRotation(float r) { defaultRotation = r; } //FIXME save in shop instance
    private static       float defaultRotation       = (float) Math.toRadians(45);
    private static final Transform DEFAULT_TRANSFORM = new Transform().scale(0.5f).rotY(defaultRotation);

    public static final int S_TIME = DetailsDisplay.DURATION_SPAWN + 2;
    public static final int L_TIME = 32 * 3; //! Must be a multiple of 3
    public static final int D_TIME = DetailsDisplay.DURATION_DESPAWN + 2;

    private static final float S_SCALE  = 1.02f;
    private static final float S_HEIGHT = 0.05f;

    public static final float L_ROT   = (float) Math.toRadians(120);
    public static final float L_ROT_A = L_ROT * (float)((double)S_TIME / ((double)L_TIME / 3) * 3d);




    private static final DisplayAnimation focusAnimation = new DisplayAnimation(
        List.of(
            new TransformTransition(DEFAULT_TRANSFORM.clone().moveY(S_HEIGHT).scale(S_SCALE).rotY(L_ROT_A), S_TIME)
        ),
        List.of(
            new TransformTransition(DEFAULT_TRANSFORM.clone().moveY(S_HEIGHT).scale(S_SCALE).rotY(L_ROT_A + L_ROT * 1), L_TIME / 3),
            new TransformTransition(DEFAULT_TRANSFORM.clone().moveY(S_HEIGHT).scale(S_SCALE).rotY(L_ROT_A + L_ROT * 2), L_TIME / 3),
            new TransformTransition(DEFAULT_TRANSFORM.clone().moveY(S_HEIGHT).scale(S_SCALE).rotY(L_ROT_A + L_ROT * 3), L_TIME / 3)
        ),
        List.of(
            new TransformTransition(DEFAULT_TRANSFORM, D_TIME)
        )
    );




    public ShopItemDisplay(ItemDisplayEntity _rawDisplay, DisplayAnimation _animation) {
        super(_rawDisplay, _animation);
    }

    public ShopItemDisplay(World world, BlockPos pos, ItemStack item) {
        super(
            world,
            new Vec3d(pos.getX() + 0.5, pos.getY() + 0.3, pos.getZ() + 0.5), //FIXME this should prob be part of the default transform
            DEFAULT_TRANSFORM,
            item,
            true,
            false,
            null
        );
        rawDisplay.setCustomName(Utils.getItemName(item));
    }




    public void spawn(World world){
        super.spawn(world);
    }




    public void despawn(){
        super.despawn();
    }




    public void setCustomNameVisible(Boolean customNameVisible) {
        rawDisplay.setCustomNameVisible(customNameVisible);
    }




    public void enterFocusState(){
        rawDisplay.setCustomNameVisible(false);
        scheduleTransitions(focusAnimation.spawn);

        currentHandlers.add(Scheduler.schedule(S_TIME, () -> {
            loopTransitions(focusAnimation.loop, L_TIME);
        }));
    }




    public void leaveFocusState(){
        scheduleTransitions(focusAnimation.despawn);
        currentHandlers.add(Scheduler.schedule(D_TIME, () -> {
            rawDisplay.setCustomNameVisible(true);
        }));
    }
}
