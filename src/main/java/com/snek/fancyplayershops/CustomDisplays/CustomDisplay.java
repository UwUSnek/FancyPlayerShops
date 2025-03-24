package com.snek.fancyplayershops.CustomDisplays;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.utils.Scheduler;
import com.snek.fancyplayershops.utils.TaskHandler;
import com.snek.fancyplayershops.utils.Utils;

import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.decoration.Brightness;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.entity.decoration.DisplayEntity.BillboardMode;
import net.minecraft.util.math.AffineTransformation;
import net.minecraft.world.World;








public abstract class CustomDisplay {
    protected @NotNull DisplayEntity heldEntity;
    protected @NotNull Transform defaultTransform;
    protected @NotNull AnimationData animation;
    protected @NotNull List<TaskHandler> currentHandlers = new ArrayList<>(); // The handlers of the transitions that are currently scheduled. Used to cancel animations without waiting for them to finish


    private int TMP_interpolationDuration = 0;
    private @Nullable TaskHandler interpolationDispatcherHandler;


    static private Method method_setTransformation;
    static private Method method_setInterpolationDuration;
    static private Method method_setStartInterpolation;
    static private Method method_setBillboardMode;
    static private Method method_setViewRange;
    static private Method method_setBrightness;
    static {
        try {
            method_setTransformation        = DisplayEntity.class.getDeclaredMethod("setTransformation", AffineTransformation.class);
            method_setInterpolationDuration = DisplayEntity.class.getDeclaredMethod("setInterpolationDuration",           int.class);
            method_setStartInterpolation    = DisplayEntity.class.getDeclaredMethod("setStartInterpolation",              int.class);
            method_setBillboardMode         = DisplayEntity.class.getDeclaredMethod("setBillboardMode",         BillboardMode.class);
            method_setViewRange             = DisplayEntity.class.getDeclaredMethod("setViewRange",                     float.class);
            method_setBrightness            = DisplayEntity.class.getDeclaredMethod("setBrightness",               Brightness.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        method_setTransformation.setAccessible(true);
        method_setInterpolationDuration.setAccessible(true);
        method_setStartInterpolation.setAccessible(true);
        method_setBillboardMode.setAccessible(true);
        method_setViewRange.setAccessible(true);
        method_setBrightness.setAccessible(true);
    }




    public CustomDisplay(@NotNull DisplayEntity _heldEntity, @NotNull Transform _defaultTransform, @NotNull AnimationData _animation) {
        defaultTransform = _defaultTransform;
        heldEntity = _heldEntity;
        animation = _animation;
        setViewRange(0.3f);
        setBrightness(new Brightness(15, 15));
        setTransformation(defaultTransform.get());
        apply(0);
    }




    /**
     * Schedules an animation.
     * Automatically cancels any remaining animation from the previous call (or previous calls to loopAnimation).
     * @param animtion The animation.
     */
    public void scheduleAnimation(@NotNull Animation animtion) {
        // Cancel previous transitions
        for (TaskHandler handler : currentHandlers) {
            handler.cancel();
        }
        currentHandlers.clear();


        // Schedule the new transitions
        int totScheduledDuration = 0;
        for (Transition t : animtion.transitions) {
            currentHandlers.add(Scheduler.schedule(totScheduledDuration, () -> {
                setTransformation(t.transform.get());
                apply(t.getDuration());
            }));
            totScheduledDuration += t.getDuration();
        }
    }




    /**
     * Loops an animation.
     * Automatically cancels any remaining animation from the previous call (or previous calls to scheduleAnimation).
     * @param transition The animation.
     */
    public void loopAnimation(@NotNull Animation animation, int loopDuration) {
        // Cancel previous transitions
        for (TaskHandler handler : currentHandlers) {
            handler.cancel();
        }
        currentHandlers.clear();


        // Schedule the new transitions
        int totScheduledDuration = 0;
        for (Transition t : animation.transitions) {
            currentHandlers.add(Scheduler.loop(totScheduledDuration, loopDuration, () -> {
                setTransformation(t.transform.get());
                apply(t.getDuration());
            }));
            totScheduledDuration += t.getDuration();
        }
    }




    public void spawn(@NotNull World world) {
        world.spawnEntity(heldEntity);

        // Schedule transitions if present
        if(animation != null && animation.spawn != null) {
            scheduleAnimation(animation.spawn);
        }
    }




    public void despawn() {
        // Schedule transitions if present
        if(animation != null && animation.despawn != null) {
            scheduleAnimation(animation.despawn);
        }

        // Schedule entity removal
        int totScheduledDuration = 0;
        for (Transition t : animation.despawn.transitions) totScheduledDuration += t.getDuration(); //TODO this should prob be a method of a custom transition list class
        Scheduler.schedule(totScheduledDuration, () -> { heldEntity.remove(RemovalReason.KILLED); });
    }




    public void setTransformation(@NotNull AffineTransformation transformation) {
        Utils.invokeSafe(method_setTransformation, heldEntity, transformation);
    }




    /**
     * Sets the duration of the interpolation and starts it at the end of the current tick.
     * Multiple calls only update the duration. The interpolation is started exactly 1 time.
     * @param duration The duration of the interpolation.
     */
    public void apply(int duration) {

        // Update duration
        TMP_interpolationDuration = duration;

        // Cancel previous tasks and schedule a new one on the current tick
        if(interpolationDispatcherHandler != null) interpolationDispatcherHandler.cancel();
        final int saved_TMP_interpolationDuration = TMP_interpolationDuration;
        interpolationDispatcherHandler = Scheduler.run(() -> {
            setInterpolationDuration(saved_TMP_interpolationDuration);
            setStartInterpolation();
        });
    }




    /**
     * @param duration The duration in ticks
     */
    private void setInterpolationDuration(int duration) {
        Utils.invokeSafe(method_setInterpolationDuration, heldEntity, duration);
    }




    private void setStartInterpolation() {
        Utils.invokeSafe(method_setStartInterpolation, heldEntity, 0);
    }




    public void setBillboardMode(@NotNull BillboardMode billboardMode) {
        Utils.invokeSafe(method_setBillboardMode, heldEntity, billboardMode);
    }




    public void setViewRange(float viewRange) {
        Utils.invokeSafe(method_setViewRange, heldEntity, viewRange);
    }




    public void setBrightness(@NotNull Brightness brightness) {
        Utils.invokeSafe(method_setBrightness, heldEntity, brightness);
    }
}
