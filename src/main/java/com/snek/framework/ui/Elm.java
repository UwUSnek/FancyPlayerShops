package com.snek.framework.ui;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import org.joml.Vector3f;

import com.snek.framework.custom_displays.CustomDisplay;
import com.snek.framework.data_types.AdditiveTransition;
import com.snek.framework.data_types.Animation;
import com.snek.framework.data_types.Flagged;
import com.snek.framework.data_types.TargetTransition;
import com.snek.framework.data_types.Transform;
import com.snek.framework.data_types.Transition;
import com.snek.framework.data_types.Triplet;
import com.snek.framework.data_types.Pair;
import com.snek.framework.ui.styles.ElmStyle;
import com.snek.framework.utils.Easings;
import com.snek.framework.utils.Scheduler;

import net.minecraft.entity.decoration.DisplayEntity.BillboardMode;
import net.minecraft.server.world.ServerWorld;







/**
 * An abstract class that represents a UI Element.
 */
public abstract class Elm {

    // Animations
    public static int TRANSITION_REFRESH_TIME = 1;                                      // The time between transition updates. Measured in ticks
    private static final @NotNull List<Elm> elmUpdateQueue = new ArrayList<>();         // The list of instances with pending transform updates
    private final @NotNull ArrayDeque<Transform>  transformQueue = new ArrayDeque<>();  // The list of transforms to apply to this instance in the next ticks. 1 for each update tick
    private boolean isQueued = false;                                                   // Whether this instance is queued for updates. Updated manually

    // Element data
    protected @NotNull  Flagged<Transform>     transform;
    protected @NotNull  Flagged<Float>         viewRange;
    protected @NotNull  Flagged<BillboardMode> billboardMode;

    // Tree data
    private @Nullable Elm parent = null;                                    // The parent element
    private @NotNull  List<@NotNull Elm> children = new ArrayList<>();      // A list of child elements

    // In-world data
    protected @NotNull ServerWorld   world;                                 // The world this Elm will be spawned in
    protected @NotNull CustomDisplay entity;                                // The display entity held by this element
    protected @NotNull ElmStyle      style;                                 // The style of the element




    protected Elm(@NotNull ServerWorld _world, CustomDisplay _entity, @NotNull ElmStyle _style) {
        world  = _world;
        entity = _entity;
        style  = _style;

        transform     = Flagged.from(style.getTransform());
        viewRange     = Flagged.from(style.getViewRange());
        billboardMode = Flagged.from(style.getBillboardMode());
    }




    /**
     * Flushes changeable style values to the entity.
     * This does not start an interpolation.
     */
    public void flushStyle() {
        if(transform    .isFlagged()) { entity.setTransformation(transform    .get().get()); transform    .unflag(); }
        if(viewRange    .isFlagged()) { entity.setViewRange     (viewRange    .get()      ); viewRange    .unflag(); }
        if(billboardMode.isFlagged()) { entity.setBillboardMode (billboardMode.get()      ); billboardMode.unflag(); }
    }




    /**
     * Instantly calculates animation ticks and adds this element to the update queue.
     * ! Partial steps at the end of the animation are expanded to cover the entire step. //TODO update this if its incorrect
     * @param animation The animation to apply.
     */
    public void applyAnimation(@NotNull Animation animation) {

        // Add element to the update queue and update the queued state
        if(!isQueued) {
            elmUpdateQueue.add(this);
            isQueued = true;
        }


        // Calculate animation steps as a list of additive transforms
        List<Triplet<Transform, Boolean, Float>> animationSteps = new ArrayList<>();
        Transform totTransform = new Transform(); // The sum of all the changes applied by the current and previous steps of the animation
        for (Transition transition : animation.transitions) {

            // For each step of the transition
            int time = transition.getDuration();                            // The duration of this transition
            // Transform targetTransform = transition.compute(totTransform);   // The target transformation of this transition
            totTransform = transition.compute(totTransform);   // The target transformation of this transition
            boolean isAdditive = transition instanceof AdditiveTransition;
            for(int i = TRANSITION_REFRESH_TIME; i < time; i = Math.min(i + TRANSITION_REFRESH_TIME, time)) {

                // Calculate interpolation factor and add the new animation step to the list
                float factor = (float)transition.getEasing().compute((double)i / (double)time);
                // animationSteps.add(Pair.from(targetTransform.clone().interpolate(targetTransform, factor), isAdditive));
                // animationSteps.add(Triplet.from(oldTransform.clone().interpolate(totTransform, factor), isAdditive));
                animationSteps.add(Triplet.from(totTransform, isAdditive, factor));
                // totTransform.clone().interpolate(targetTransform, factor)
            }
        }


        // Update existing future transforms
        int i = 0;
        if(!transformQueue.isEmpty()) {
            for (Transform ft : transformQueue) {
                // ft.apply(animationSteps.get(i));
                var step = animationSteps.get(i);
                if(step.second) ft.interpolate(ft.apply(step.first), step.third); else ft.interpolate(step.first, step.third);
                // System.out.println("Changing to " + ft.get().getTranslation().toString());
                ++i;
                if(i >= animationSteps.size()) break;
            }
        }


        // Add remaining future transforms
        Transform lastTransform = transformQueue.isEmpty() ? transform.get() : transformQueue.getLast();
        for(; i < animationSteps.size(); ++i) {
            // transformQueue.add(lastTransform.clone().apply(animationSteps.get(i)));
            var step = animationSteps.get(i);
            if(step.second) {
                transformQueue.add(lastTransform.clone().interpolate(lastTransform.clone().apply(step.first), step.third));
            }
            else {
                transformQueue.add(lastTransform.clone().interpolate(step.first, step.third));
            }
        }

            // for(int i = 0; i < totTime; i = Math.min(i + TRANSITION_REFRESH_TIME, totTime)) {
                // // stepTargetTick = Math.min(stepTargetTick + TRANSITION_REFRESH_TIME, transition.getDuration());
                // float factor = stepTargetTick / transition.getDuration();       // Calculate interpolation factor
                // transformQueue.add(lastTransform.clone().interpolate(transition.compute(lastTransform), factor);   // Apply the partial interpolation to the current future transform
            // }
    }




    public CustomDisplay getEntity() {
        return entity;
    }




    /**
     * Adds a child to this Elm, then sets it's parent to this.
     * @param child The new child.
     */
    public void addChild(Elm child) {
        children.add(child);
        child.parent = this;
    }


    /**
     * Returns the list of children of this Elm.
     * @return The list of children.
     */
    public List<Elm> getChildren() {
        return children;
    }




    /**
     * Spawns the element and its associated entities into the world.
     */
    public void spawn(Vector3d pos) {

        // Spawn entity into the world
        entity.spawn(world, pos);

        // Handle animations
        Animation animation = style.getSpawnAnimation();
        if(animation != null) {
            applyAnimation(animation);
        }
    }




    /**
     * Removes the element and its associated entities from the world.
     */
    public void despawn() {

        // Handle animations
        Animation animation = style.getDespawnAnimation();
        if(animation != null) {
            applyAnimation(animation);
        }

        // Remove entity from the world
        Scheduler.schedule(animation.getTotalDuration(), () -> {
            entity.despawn();
        });
    }




    /**
     * Processes transitions and other tick features of this Elm and all of its children, recursively.
     * Must be called at the end of the tick every TRANSITION_REFRESH_TIME ticks.
     * @return true if no action is necessary. false if the element has been removed from the update queue.
     */
    public boolean tick() {
        // if(!transformQueue.isEmpty()) {
            transform.set(transformQueue.removeFirst());
            flushStyle();
            entity.setInterpolationDuration(TRANSITION_REFRESH_TIME);
            entity.setStartInterpolation();

            if(transformQueue.isEmpty()) {
                elmUpdateQueue.remove(this);
                isQueued = false;
                return false;
            }
            return true;
        // }
        // else {
        //     flushStyle();
        //     elmUpdateQueue.remove(this);
        // }
    }




    /**
     * Processes a single tick of all the queued elements
     */
    public static void processUpdateQueueTick(){

        // System.out.println("array: " + (elmUpdateQueue == null ? "null" : elmUpdateQueue.toString()));
        for (int i = 0; i < elmUpdateQueue.size();) {
            // System.out.println("processing tick of " + (elm == null ? "null" : elm.toString()));
            if(elmUpdateQueue.get(i).tick()) ++i;
        }
    }
}
