package com.snek.framework.ui;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import org.joml.Vector3f;

import com.snek.framework.custom_displays.CustomDisplay;
import com.snek.framework.data_types.Flagged;
import com.snek.framework.data_types.IndexedArrayDeque;
import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.steps.AnimationStep;
import com.snek.framework.data_types.animations.transitions.Transition;
import com.snek.framework.ui.styles.ElmStyle;
import com.snek.framework.utils.SpaceUtils;
import com.snek.framework.utils.scheduler.Scheduler;

import net.minecraft.entity.decoration.DisplayEntity.BillboardMode;
import net.minecraft.entity.player.PlayerEntity;
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
    protected boolean isSpawned = false;                                    // Whether the element has been spawned into the world




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
        List<AnimationStep> animationSteps = new ArrayList<>();
        Transform totTransform = new Transform();       // The sum of all the changes applied by the current and previous steps of the animation
        int time = transition.getDuration();            // The duration of this transition
        for(int i = TRANSITION_REFRESH_TIME; i < time; i = Math.min(i + TRANSITION_REFRESH_TIME, time)) {

            // Calculate interpolation factor and add the new animation step to the list
            float factor = (float)transition.getEasing().compute((double)i / (double)time);
            animationSteps.add(transition.createStep(totTransform, factor));
        }

        // Add padding step //! This makes the actual duration match the duration specified in the transition (or be greater than it, which is not an issue)
        animationSteps.add(transition.createStep(totTransform, 1));


        // Update existing future transforms
        int i = 0;
        if(!transformQueue.isEmpty()) {
            AnimationStep step = null;

            // Update existing future transforms
            for(; i + shift < transformQueue.size() && i < animationSteps.size(); ++i) {
                step = animationSteps.get(i);
                __applyTransitionStep(i + shift, step);
            }

            // If the amount of future transforms is larger than the amount of steps, apply the last step to the remaining transforms
            if(i >= animationSteps.size()) {
                for(; i + shift < transformQueue.size(); ++i) {
                    __applyTransitionStep(i + shift, step);
                }
            }
        }


        // Add remaining future transforms
        Transform lastTransform = transformQueue.isEmpty() ? transform.get() : transformQueue.getLast();
        for(; i < animationSteps.size(); ++i) {
            transformQueue.add(lastTransform.clone());
            var step = animationSteps.get(i);
            __applyTransitionStep(i + shift, step);
        }


        // Return transition width
        return animationSteps.size();
    }




    /**
     * Applies a single animation step.
     * @param index The index of the future transform to apply the step to.
     * @param step The animation step.
     * @return The modified transform.
     */
    protected @NotNull Transform __applyTransitionStep(int index, @NotNull AnimationStep step){
        Transform ft = transformQueue.get(index);
        if(step.isAdditive) ft.interpolate(ft.clone().apply(step.transform), step.factor);
        else                ft.interpolate(step.transform,                   step.factor);
        return ft;
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
        isSpawned = true;

        // Flush previous changes to the entity to avoid bad interpolations and the entity into the world
        flushStyle();
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
        isSpawned = false;

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




    /**
     * Processes a click event using the player's view angle and current children.
     * Reach distance is not accounted for.
     * Calling this method on an element that hasn't been spawned yet is allowed and has no effect.
     *
     * NOTICE: Click detection is only available for elements with Fixed billboard mode.
     * Calling this function on elements with a different billboard mode is allowed and has no effect.
     *
     * @param player The player
     */
    public void onClick(PlayerEntity player) {
        if(isSpawned && billboardMode.get() == BillboardMode.FIXED) {

            // Calculate the world coordinates of the display's origin. //! Left rotation and scale are ignored as they doesn't affect this
            Vector3f origin =
                entity.getPosCopy()
                .add   (transform.get().getPos())
                .rotate(transform.get().getRrot())
            ;

            //TODO calculate direction vector. multiply the two rotaitons
            //TODO process clicks using a 2d plane instead of a sphere

            if(SpaceUtils.checkLineSphereIntersection(player.getEyePos().toVector3f(), player.getRotationVec(1f).toVector3f(), origin, 0.2f)) {
                System.out.println("ELEMENT CLICKED");
            }
            else {
                System.out.println("ELEMENT NOT CLICKED");
                // System.out.println("Center: " + center.toString());
            }
        }
    }



    //TODO Checks whether a line intersects a rectangle in a 3d space instead of using spheres
}




//FIXME SPAWN INTERACTION ENTITY WHEN SHOPS ARE FOCUSED
//FIXME use it to visually stop client-side interactions with blocks and entities behind it
