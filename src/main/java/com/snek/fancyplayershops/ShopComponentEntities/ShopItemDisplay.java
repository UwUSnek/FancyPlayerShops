package com.snek.fancyplayershops.ShopComponentEntities;

import java.util.List;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.snek.fancyplayershops.CustomDisplays.CustomItemDisplay;
import com.snek.fancyplayershops.CustomDisplays.DisplayAnimation;
import com.snek.fancyplayershops.CustomDisplays.TransformTransition;
import com.snek.fancyplayershops.utils.Scheduler;

import net.minecraft.entity.decoration.DisplayEntity.ItemDisplayEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.AffineTransformation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;








public class ShopItemDisplay extends CustomItemDisplay {
    public static int TRANSITION_DURATION_SPAWN   = FocusDisplay.TRANSITION_DURATION_SPAWN + 2;
    public static int TRANSITION_DURATION_LOOP    = 1; //FIXME
    public static int TRANSITION_DURATION_DESPAWN = FocusDisplay.TRANSITION_DURATION_DESPAWN + 2;

    private static float DEFAULT_SCALE     = 1.00f / 2;
    private static float TRANSITION_SCALE  = 1.02f / 2;
    private static float TRANSITION_HEIGHT = 0.05f;

    private static final DisplayAnimation focusAnimation = new DisplayAnimation(
        List.of(new TransformTransition(
            new AffineTransformation(
                new Vector3f(0, TRANSITION_HEIGHT, 0),
                new Quaternionf(),
                new Vector3f(TRANSITION_SCALE, TRANSITION_SCALE, TRANSITION_SCALE),
                new Quaternionf()
            ),
            TRANSITION_DURATION_SPAWN
        )),
        List.of(new TransformTransition(
            new AffineTransformation(
                new Vector3f(0, 0.05f, 0),
                new Quaternionf(),
                new Vector3f(1.02f, 1.02f, 1.02f),
                new Quaternionf()
            ),
            TRANSITION_DURATION_SPAWN
        )),
        List.of(new TransformTransition(
            new AffineTransformation(
                new Vector3f(0, 0, 0),
                new Quaternionf(),
                new Vector3f(DEFAULT_SCALE, DEFAULT_SCALE, DEFAULT_SCALE),
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
        rawDisplay.setCustomName(Text.of("[Empty]"));
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

        // Scheduler.loop(TRANSITION_DURATION_SPAWN, TRANSITION_DURATION_LOOP, () -> {
            // //TODO
        // });
    }




    public void leaveFocusState(){
        scheduleTransitions(focusAnimation.despawn);
        Scheduler.schedule(TRANSITION_DURATION_DESPAWN, () -> { rawDisplay.setCustomNameVisible(true); });
    }
}
