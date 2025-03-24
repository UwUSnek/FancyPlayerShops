package com.snek.fancyplayershops.CustomDisplays;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.joml.Vector4i;

import com.snek.fancyplayershops.utils.Scheduler;
import com.snek.fancyplayershops.utils.TaskHandler;

import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.decoration.Brightness;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.entity.decoration.DisplayEntity.BillboardMode;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.AffineTransformation;
import net.minecraft.world.World;








public abstract class CustomDisplay {
    protected DisplayEntity heldEntity;
    protected Transform defaultTransform;
    protected DisplayAnimation animation;
    protected List<TaskHandler> currentHandlers = new ArrayList<>(); // The handlers of the transitions that are currently scheduled. Used to cancel animations without waiting for them to finish


    private int TMP_interpolationDuration = 0;
    private TaskHandler interpolationDispatcherHandler;


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




    public CustomDisplay(DisplayEntity _heldEntity, Transform _defaultTransform, DisplayAnimation _animation) {
        defaultTransform = _defaultTransform;
        heldEntity = _heldEntity;
        animation = _animation;
        setViewRange(0.4f);
        setBrightness(new Brightness(15, 15));
        setTransformation(defaultTransform.get());
        apply(0);
    }




    /**
     * Schedules a list of transitions.
     * Automatically cancels any remaining transitions from the previous call (or previous calls to loopTransition).
     * @param transitions The transitions. Can be empty.
     */
    public void scheduleTransitions(List<TransformTransition> transitions) {
        // Cancel previous transitions
        for (TaskHandler handler : currentHandlers) {
            handler.cancel();
        }
        currentHandlers.clear();


        // Schedule the new transitions
        int totScheduledDuration = 0;
        for (TransformTransition t : transitions) {
            if(totScheduledDuration == 0) {
                setTransformation(t.transform.get());
                apply(t.duration);
            }
            else currentHandlers.add(Scheduler.schedule(totScheduledDuration, () -> {
                setTransformation(t.transform.get());
                apply(t.duration);
            }));
            totScheduledDuration += t.duration;
        }
    }




    /**
     * Schedules a list of transitions.
     * Automatically cancels any remaining transitions from the previous call (or previous calls to scheduleTransition).
     * @param transition The transitions. Can be empty.
     */
    public void loopTransitions(List<TransformTransition> transitions, int loopDuration) {
        // Cancel previous transitions
        for (TaskHandler handler : currentHandlers) {
            handler.cancel();
        }
        currentHandlers.clear();


        // Schedule the new transitions
        int totScheduledDuration = 0;
        for (TransformTransition t : transitions) {
            currentHandlers.add(Scheduler.loop(totScheduledDuration, loopDuration, () -> {
                setTransformation(t.transform.get());
                apply(t.duration);
            }));
            totScheduledDuration += t.duration;
        }
    }




    public void spawn(World world) {
        world.spawnEntity(heldEntity);

        // Schedule transitions if present
        if(animation != null && animation.spawn != null) {
            scheduleTransitions(animation.spawn);
        }
    }




    public void despawn() {
        // Schedule transitions if present
        if(animation != null && animation.despawn != null) {
            scheduleTransitions(animation.despawn);
        }

        // Schedule entity removal
        int totScheduledDuration = 0;
        for (TransformTransition t : animation.despawn) totScheduledDuration += t.duration; //TODO this should prob be a method of a custom transition list class
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
        });
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




    public void setBillboardMode(BillboardMode billboardMode) {
        try {
            method_setBillboardMode.invoke(heldEntity, billboardMode);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }




    public void setViewRange(float viewRange) {
        try {
            method_setViewRange.invoke(heldEntity, viewRange);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }




    public void setBrightness(Brightness brightness) {
        try {
            method_setBrightness.invoke(heldEntity, brightness);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
