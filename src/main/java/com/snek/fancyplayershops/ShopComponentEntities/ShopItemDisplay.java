package com.snek.fancyplayershops.ShopComponentEntities;

import java.util.List;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.snek.fancyplayershops.CustomDisplays.CustomItemDisplay;
import com.snek.fancyplayershops.CustomDisplays.DisplayAnimation;
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
    public static int TRANSITION_DURATION_SPAWN   = FocusDisplay.TRANSITION_DURATION_SPAWN + 2;
    public static int TRANSITION_DURATION_LOOP    = 32 * 3; //! Must be a multiple of 3
    public static int TRANSITION_DURATION_DESPAWN = FocusDisplay.TRANSITION_DURATION_DESPAWN + 2;

    private static float DEFAULT_ROTATION  = (float) Math.toRadians(45);
    private static float DEFAULT_SCALE     = 1.00f / 2;
    private static float TRANSITION_SCALE  = 1.02f / 2;
    private static float TRANSITION_HEIGHT = 0.05f;

    public static float LOOP_ROTATION_WIDTH   = (float) Math.toRadians(120);
    public static float LOOP_ROTATION_WIDTH_A = LOOP_ROTATION_WIDTH * (float)((double)TRANSITION_DURATION_SPAWN / ((double)TRANSITION_DURATION_LOOP / 3) * 3d);


    private static final DisplayAnimation focusAnimation = new DisplayAnimation(
        List.of(new TransformTransition(
            new AffineTransformation(
                new Vector3f(0, TRANSITION_HEIGHT, 0),
                new Quaternionf().rotateY(DEFAULT_ROTATION + LOOP_ROTATION_WIDTH_A),
                new Vector3f(TRANSITION_SCALE),
                new Quaternionf()
            ),
            TRANSITION_DURATION_SPAWN
        )),
        List.of(
            new TransformTransition(
                new AffineTransformation(
                    new Vector3f(0, TRANSITION_HEIGHT, 0),
                    new Quaternionf().rotateY(DEFAULT_ROTATION + LOOP_ROTATION_WIDTH_A + LOOP_ROTATION_WIDTH),
                    new Vector3f(TRANSITION_SCALE),
                    new Quaternionf()
                ),
                TRANSITION_DURATION_LOOP / 3
            ),
            new TransformTransition(
                new AffineTransformation(
                    new Vector3f(0, TRANSITION_HEIGHT, 0),
                    new Quaternionf().rotateY(DEFAULT_ROTATION + LOOP_ROTATION_WIDTH_A + LOOP_ROTATION_WIDTH * 2),
                    new Vector3f(TRANSITION_SCALE),
                    new Quaternionf()
                ),
                TRANSITION_DURATION_LOOP / 3
            ),
            new TransformTransition(
                new AffineTransformation(
                    new Vector3f(0, TRANSITION_HEIGHT, 0),
                    new Quaternionf().rotateY(DEFAULT_ROTATION + LOOP_ROTATION_WIDTH_A + LOOP_ROTATION_WIDTH * 3),
                    new Vector3f(TRANSITION_SCALE),
                    new Quaternionf()
                ),
                TRANSITION_DURATION_LOOP / 3
            )
        ),
        List.of(new TransformTransition(
            new AffineTransformation(
                new Vector3f(0, 0, 0),
                new Quaternionf().rotateY(DEFAULT_ROTATION),
                new Vector3f(DEFAULT_SCALE),
                new Quaternionf()
            ),
            TRANSITION_DURATION_DESPAWN
        ))
    );




    public ShopItemDisplay(ItemDisplayEntity _rawDisplay, DisplayAnimation _animation) {
        super(_rawDisplay, _animation);
    }

    public ShopItemDisplay(World world, BlockPos pos, ItemStack item) {
        super(
            world,
            new Vec3d(pos.getX() + 0.5, pos.getY() + 0.3, pos.getZ() + 0.5),
            item,
            true,
            false,
            null
        );
        rawDisplay.setCustomName(Utils.getItemName(item));
        setTransformation(new AffineTransformation(
            new Vector3f(0, 0, 0),
            new Quaternionf().rotateY(DEFAULT_ROTATION),
            new Vector3f(DEFAULT_SCALE),
            new Quaternionf()
        ));
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

        Scheduler.schedule(TRANSITION_DURATION_SPAWN, () -> {
            loopTransitions(focusAnimation.loop, TRANSITION_DURATION_LOOP);
        });
    }




    public void leaveFocusState(){
        scheduleTransitions(focusAnimation.despawn);
        Scheduler.schedule(TRANSITION_DURATION_DESPAWN, () -> { rawDisplay.setCustomNameVisible(true); });
    }
}
