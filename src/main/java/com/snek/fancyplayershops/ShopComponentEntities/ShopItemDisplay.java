package com.snek.fancyplayershops.ShopComponentEntities;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.Shop;
import com.snek.framework.custom_displays.Animation;
import com.snek.framework.custom_displays.CustomItemDisplay;
import com.snek.framework.custom_displays.ElmStyle;
import com.snek.framework.custom_displays.Transform;
import com.snek.framework.custom_displays.Transition;
import com.snek.framework.ui.DetailsDisplay;
import com.snek.framework.utils.Scheduler;
import com.snek.framework.utils.Utils;

import net.minecraft.entity.decoration.DisplayEntity.ItemDisplayEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;








public class ShopItemDisplay extends CustomItemDisplay {

    public void setDefaultRotation(float r) { defaultRotation = r; } //FIXME save in shop instance
    private static       float defaultRotation       = (float) Math.toRadians(45);
    private static final Transform DEFAULT_TRANSFORM = new Transform().scale(0.5f).rotY(defaultRotation);

    public static final int S_TIME = DetailsDisplay.S_TIME + 2;
    public static final int L_TIME = 32 * 3; //! Must be a multiple of 3
    public static final int D_TIME = DetailsDisplay.D_TIME + 2;

    private static final float S_SCALE  = 1.02f;
    private static final float S_HEIGHT = 0.05f;

    public static final float L_ROT   = (float) Math.toRadians(120);
    public static final float L_ROT_A = L_ROT * (float)((double)S_TIME / ((double)L_TIME / 3) * 3d);




    private static final AnimationData focusAnimation = new AnimationData(
        new Animation(
            new Transition(DEFAULT_TRANSFORM.clone().moveY(S_HEIGHT).scale(S_SCALE).rotY(L_ROT_A), S_TIME)
        ),
        new Animation(
            new Transition(DEFAULT_TRANSFORM.clone().moveY(S_HEIGHT).scale(S_SCALE).rotY(L_ROT_A + L_ROT * 1), L_TIME / 3),
            new Transition(DEFAULT_TRANSFORM.clone().moveY(S_HEIGHT).scale(S_SCALE).rotY(L_ROT_A + L_ROT * 2), L_TIME / 3),
            new Transition(DEFAULT_TRANSFORM.clone().moveY(S_HEIGHT).scale(S_SCALE).rotY(L_ROT_A + L_ROT * 3), L_TIME / 3)
        ),
        new Animation(
            new Transition(DEFAULT_TRANSFORM, D_TIME)
        )
    );




    public ShopItemDisplay(@NotNull ItemDisplayEntity _rawDisplay, @NotNull AnimationData _animation) {
        super(_rawDisplay, _animation);
    }

    public ShopItemDisplay(@NotNull Shop targetShop) {
        super(
            targetShop.getWorld(),
            targetShop.calcDisplayPos(),
            DEFAULT_TRANSFORM,
            targetShop.getItem(),
            true,
            false,
            null
        );
        rawDisplay.setCustomName(Utils.getItemName(targetShop.getItem()));
    }




    @Override
    public void spawn(@NotNull World world){
        super.spawn(world);
    }




    @Override
    public void despawn(){
        super.despawn();
    }




    public void setCustomNameVisible(boolean customNameVisible) {
        rawDisplay.setCustomNameVisible(customNameVisible);
    }




    public void enterFocusState(){
        rawDisplay.setCustomNameVisible(false);
        scheduleAnimation(focusAnimation.spawn);

        currentHandlers.add(Scheduler.schedule(focusAnimation.spawn.getTotalDuration(), () -> {
            loopAnimation(focusAnimation.loop, focusAnimation.loop.getTotalDuration());
        }));
    }




    public void leaveFocusState(){
        scheduleAnimation(focusAnimation.despawn);
        currentHandlers.add(Scheduler.schedule(focusAnimation.despawn.getTotalDuration(), () -> {
            rawDisplay.setCustomNameVisible(true);
        }));
    }
}
