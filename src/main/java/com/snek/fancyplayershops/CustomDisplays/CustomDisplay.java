package com.snek.fancyplayershops.CustomDisplays;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.joml.Vector3f;
import org.joml.Vector4i;

import com.snek.fancyplayershops.utils.Scheduler;
import com.snek.fancyplayershops.utils.TaskHandler;

import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.AffineTransformation;
import net.minecraft.world.World;








public abstract class CustomDisplay {
    DisplayEntity heldEntity;
    AffineTransformation defaultTransformation;
    DisplayAnimation animation;


    private int TMP_interpolationDuration = 0;
    TaskHandler interpolationDispatcherHandler;


    static Method method_setTransformation;
    static Method method_setInterpolationDuration;
    static Method method_setStartInterpolation;
    static {
        try {
            method_setTransformation        = DisplayEntity.class.getDeclaredMethod("setTransformation", AffineTransformation.class);
            method_setInterpolationDuration = DisplayEntity.class.getDeclaredMethod("setInterpolationDuration",           int.class);
            method_setStartInterpolation    = DisplayEntity.class.getDeclaredMethod("setStartInterpolation",              int.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        method_setTransformation.setAccessible(true);
        method_setInterpolationDuration.setAccessible(true);
        method_setStartInterpolation.setAccessible(true);
    }




    public CustomDisplay(DisplayEntity _heldEntity, float scale, DisplayAnimation _animation) {
        defaultTransformation = new AffineTransformation(
            null,                              // Translation
            null,                              // Rotation
            new Vector3f(scale, scale, scale), // Scale
            null                               // Left rotation
        );
        heldEntity = _heldEntity;
        animation = _animation;
        setTransformation(defaultTransformation);
        apply(0);
    }




    public void spawn(World world) {
        world.spawnEntity(heldEntity);

        // Schedule transitions if present
        if(animation != null && animation.spawn != null) {
            int totScheduledDuration = 0;
            for (TransformTransition t : animation.spawn) {
                if(totScheduledDuration == 0) {
                    setTransformation(t.transform);
                    apply(t.duration);
                }
                else Scheduler.schedule(totScheduledDuration, () -> {
                    setTransformation(t.transform);
                    apply(t.duration);
                });
                totScheduledDuration += t.duration;
            }
        }
    }




    public void despawn() {
        // Schedule transitions if present
        int totScheduledDuration = 0;
        if(animation != null && animation.despawn != null) {
            for (TransformTransition t : animation.despawn) {
                if(totScheduledDuration == 0) {
                    setTransformation(t.transform);
                    apply(t.duration);
                }
                else Scheduler.schedule(totScheduledDuration, () -> {
                    setTransformation(t.transform);
                    apply(t.duration);
                });
                totScheduledDuration += t.duration;
            }
        }

        Scheduler.schedule(totScheduledDuration, () -> { heldEntity.remove(RemovalReason.KILLED); });
    }




    public void setTransformation(AffineTransformation transformation) {
        try {
            method_setTransformation.invoke(heldEntity, transformation);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
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
            System.out.println("started interpolation with delay " + saved_TMP_interpolationDuration);
        });
        System.out.println("queued interpolation with delay " + saved_TMP_interpolationDuration);
    }




    /**
     * @param duration The duration in ticks
     */
    private void setInterpolationDuration(int duration) {
        try {
            method_setInterpolationDuration.invoke(heldEntity, duration);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }




    private void setStartInterpolation() {
        try {
            method_setStartInterpolation.invoke(heldEntity, 0);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
