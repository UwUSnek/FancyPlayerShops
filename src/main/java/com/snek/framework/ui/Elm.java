package com.snek.framework.ui;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import com.snek.framework.custom_displays.CustomDisplay;
import com.snek.framework.data_types.AdditiveTransition;
import com.snek.framework.data_types.Animation;
import com.snek.framework.data_types.Flagged;
import com.snek.framework.data_types.IndexedArrayDeque;
import com.snek.framework.data_types.Transform;
import com.snek.framework.data_types.Transition;
import com.snek.framework.data_types.Triplet;
import com.snek.framework.ui.styles.ElmStyle;
import com.snek.framework.utils.Scheduler;

import net.minecraft.entity.decoration.DisplayEntity.BillboardMode;
import net.minecraft.server.world.ServerWorld;








/**
 * An abstract class that represents a UI Element.
 */
public abstract class Elm {

    // Animations
    public static int TRANSITION_REFRESH_TIME = 2;                                      // The time between transition updates. Measured in ticks
    private static final @NotNull List<Elm> elmUpdateQueue = new ArrayList<>();         // The list of instances with pending transform updates
    protected final @NotNull IndexedArrayDeque<Transform> transformQueue = new IndexedArrayDeque<>(); // The list of transforms to apply to this instance in the next ticks. 1 for each update tick
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
        if(transform    .isFlagged()) { entity.setTransformation(transform.get().get()); transform    .unflag(); }
        if(viewRange    .isFlagged()) { entity.setViewRange     (viewRange      .get()); viewRange    .unflag(); }
        if(billboardMode.isFlagged()) { entity.setBillboardMode (billboardMode  .get()); billboardMode.unflag(); }
    }




    /**
     * Instantly calculates animation steps and adds this element to the update queue.
     * ! Partial steps at the end of the animation are expanded to cover the entire step.
     * @param animation The animation to apply.
     */
    public void applyAnimation(@NotNull Animation animation) {

        // Add element to the update queue and update the queued state
        if(!isQueued) {
            elmUpdateQueue.add(this);
            isQueued = true;
        }

        // Apply each transition one at a time
        int shift = 0;
        for (Transition transition : animation.getTransitions()) {
            shift += __applyAnimationTransition(transition, shift);
        }
    }




    /**
     * Instantly calculates the steps of a single transition and adds them to this element's future transforms.
     * @param transition The transition to calculate.
     * @param shift the amount of future transforms to skip before applying this transition.
     * @return The amount of future transforms this transition affected.
     */
    private int __applyAnimationTransition(@NotNull Transition transition, int shift) {


        // Calculate animation steps as a list of transforms
        List<Triplet<Transform, Boolean, Float>> animationSteps = new ArrayList<>();
        Transform totTransform = new Transform(); // The sum of all the changes applied by the current and previous steps of the animation
        int time = transition.getDuration();                            // The duration of this transition
        for(int i = TRANSITION_REFRESH_TIME; i < time; i = Math.min(i + TRANSITION_REFRESH_TIME, time)) {

            // Calculate interpolation factor and add the new animation step to the list
            float factor = (float)transition.getEasing().compute((double)i / (double)time);
            animationSteps.add(Triplet.from(transition.compute(totTransform), transition instanceof AdditiveTransition, factor));
        }

        // Add padding step
        animationSteps.add(Triplet.from(transition.compute(totTransform), transition instanceof AdditiveTransition, 1f));


        // Update existing future transforms
        int i = 0;
        if(!transformQueue.isEmpty()) {
            Triplet<Transform, Boolean, Float> step = null;

            // Update existing future transforms
            for(; i + shift < transformQueue.size() && i >= animationSteps.size(); ++i) {
                Transform ft = transformQueue.get(i + shift);
                step = animationSteps.get(i);
                __applyTransitionStep(ft, step);
            }

            // If the amount of future transforms is larger than the amount of steps, apply the last step to the remaining transforms
            if(i >= animationSteps.size()) {
                for(; i < transformQueue.size(); ++i) {
                    Transform ft = transformQueue.get(i);
                    __applyTransitionStep(ft, step);
                }
            }
        }


        // Add remaining future transforms
        Transform lastTransform = transformQueue.isEmpty() ? transform.get() : transformQueue.getLast();
        for(; i < animationSteps.size(); ++i) {
            var step = animationSteps.get(i);
            transformQueue.add(__applyTransitionStep(lastTransform.clone(), step));
        }


        // Return transition width
        return animationSteps.size();
    }




    /**
     * Applies a single animation step to a transform.
     * @param ft The transform.
     * @param step The animation step.
     * @return The modified transform.
     */
    private Transform __applyTransitionStep(Transform ft, Triplet<Transform, Boolean, Float> step){
        if(step.second) {                                               // If the step is additive
            ft.interpolate(ft.clone().apply(step.first), step.third);       // Interpolate the transform with the result of applying the step's transform to it
        }                                                                   //
        else {                                                          // If not
            ft.interpolate(step.first, step.third);                         // Interpolate the transform with the step's transform
        }                                                                   //
        return ft;                                                      // Return the modified transform
    }




    /**
     * Retrieves the custom display entity held by this element.
     * @return The entity.
     */
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
    }




    /**
     * Processes a single tick of all the queued elements
     */
    public static void processUpdateQueueTick(){

        for (int i = 0; i < elmUpdateQueue.size();) {
            if(elmUpdateQueue.get(i).tick()) ++i;
        }
    }
}
