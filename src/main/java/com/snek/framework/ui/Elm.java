package com.snek.framework.ui;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.framework.custom_displays.CustomDisplay;
import com.snek.framework.data_types.AdditiveTransition;
import com.snek.framework.data_types.Animation;
import com.snek.framework.data_types.Flagged;
import com.snek.framework.data_types.Transform;
import com.snek.framework.data_types.Transition;
import com.snek.framework.ui.styles.ElmStyle;
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
    private @NotNull  Flagged<Transform>     transform;
    private @NotNull  Flagged<Float>         viewRange;
    private @NotNull  Flagged<BillboardMode> billboardMode;

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
    }




    /**
     * Flushes changeable style values to the entity.
     * This does not start an interpolation.
     */
    public void flushStyle() {
        if(transform    .isFlagged()) entity.setTransformation(transform    .get().get()); transform    .unflag();
        if(viewRange    .isFlagged()) entity.setViewRange     (viewRange    .get()      ); viewRange    .unflag();
        if(billboardMode.isFlagged()) entity.setBillboardMode (billboardMode.get()      ); billboardMode.unflag();
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


        // Fo each transition in the animation
        // for (Transition transition : animation.transitions) {

            // For each future transform calculated by previous calls
            // for (Transform ft : transformQueue) {

            // For each transition in the animation
            // int stepTargetTick = 0;
            // int ftIndex        = 0;
            // for (Transition transition : animation.transitions) {
                //         stepTargetTick += TRANSITION_REFRESH_TIME;                      // Calculate the number of the tick in which this step ends
                //         Transform ft = transformQueue.(ftIndex);                     // Retrieve the future transform calculated by previous calls
                //         float factor = stepTargetTick / animation.getTotalDuration();   // Calculate interpolation factor
                //         ft.interpolate(transition.compute(ft), factor);                 // Apply the partial interpolation to the future transform
                //         ++ftIndex;                                                      // Increment future transform index
                //     }
                // }

                // // For each transform that has yet to be queued
                // Transform lastTransform = ((LinkedList<Transform>)transformQueue).getLast();

                // For each transition of the animation
                // int stepEnd = 0;
        List<Transform> animationSteps = new ArrayList<>();
        Transform totTransform = new Transform(); // The sum of all the changes applied by the current and previous steps of the animation
        for (Transition transition : animation.transitions) {

            // For each step of the transition
            int time = transition.getDuration();                            // The duration of this transition
            Transform targetTransform = transition.compute(totTransform);   // The target transformation of this transition
            for(int i = TRANSITION_REFRESH_TIME; i < time; i = Math.min(i + TRANSITION_REFRESH_TIME, time)) {

                // Calculate interpolation factor and add the new animation step to the list
                float factor = (float)i / (float)time;
                animationSteps.add(totTransform.interpolate(targetTransform, factor));
            }
        }


        // Update existing future transforms
        int i = 0;
        if(!transformQueue.isEmpty()) {
            for (Transform ft : transformQueue) {
                ft.apply(animationSteps.get(i));
                ++i;
            }
        }


        // Add remaining future transforms
        Transform lastTransform = transformQueue.isEmpty() ? transform.get() : transformQueue.getLast();
        for(; i < animationSteps.size(); ++i) {
            transformQueue.add(lastTransform.clone().apply(animationSteps.get(i)));
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
    public void spawn() {
        entity.spawn(world);
    }




    /**
     * Removes the element and its associated entities from the world.
     */
    public void despawn() {
        Scheduler.schedule(style.getDespawnAnimation().getTotalDuration(), () -> {
            entity.despawn();
        });
    }




    /**
     * Processes transitions and other tick features of this Elm and all of its children, recursively.
     * Must be called at the end of the tick every TRANSITION_REFRESH_TIME ticks.
     */
    public void tick() {
        if(!transformQueue.isEmpty()) {
            transform.set(transformQueue.removeFirst());
        }

        flushStyle();
        entity.setInterpolationDuration(TRANSITION_REFRESH_TIME);
        entity.setStartInterpolation();
    }




    /**
     * Processes a single tick of all the queued elements
     */
    public static void processUpdateQueueTick(){
        for (Elm elm : elmUpdateQueue) {
            elm.tick();
        }
    }
}
