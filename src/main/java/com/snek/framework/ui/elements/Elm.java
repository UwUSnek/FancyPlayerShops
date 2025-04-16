package com.snek.framework.ui.elements;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector4i;

import com.snek.fancyplayershops.FancyPlayerShops;
import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.InterpolatedData;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.TransitionStep;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.data_types.containers.Flagged;
import com.snek.framework.data_types.containers.IndexedArrayDeque;
import com.snek.framework.data_types.displays.CustomDisplay;
import com.snek.framework.data_types.ui.AlignmentX;
import com.snek.framework.data_types.ui.AlignmentY;
import com.snek.framework.ui.Div;
import com.snek.framework.ui.interfaces.Hoverable;
import com.snek.framework.ui.styles.ElmStyle;
import com.snek.framework.utils.Easing;
import com.snek.framework.utils.SpaceUtils;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.scheduler.Scheduler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.decoration.DisplayEntity.BillboardMode;
import net.minecraft.entity.decoration.DisplayEntity.TextDisplayEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.AffineTransformation;
import net.minecraft.world.World;
















/**
 * An abstract class that represents a visible UI Element.
 */
public abstract class Elm extends Div {
    public static final String ENTITY_CUSTOM_NAME = FancyPlayerShops.MOD_ID + ".ui.displayentity";
    public static final int QUEUE_LINGER_TICKS = 4;
    // ^ Additional update ticks the element stays in the update queue for after all of its steps have been processed.


    // Animation handling
    public    static final int TRANSITION_REFRESH_TIME = 2;                         // The time between transition updates. Measured in ticks
    private   static final @NotNull List<Elm> elmUpdateQueue = new ArrayList<>();   // The list of instances with pending transition steps
    protected        final @NotNull IndexedArrayDeque<InterpolatedData> futureDataQueue = new IndexedArrayDeque<>(); // The list of transition steps to apply to this instance in the next ticks. 1 for each update tick
    private boolean isQueued = false;                                               // Whether this instance is queued for updates. Updated manually
    private int queueLingerTicks = 0;


    // In-world data
    protected @NotNull ServerWorld   world;     // The world this Elm will be spawned in
    protected @NotNull CustomDisplay entity;    // The display entity held by this element
    public    @NotNull ElmStyle      style;     // The style of the element
    protected boolean isSpawned = false;        // Whether the element has been spawned into the world
    private   boolean isHovered = false;        // Whether the element is being hovered on by a player's crosshair. //! Only valid in Hoverable instances
    public CustomDisplay getEntity() { return entity; }




    // @Override public void setSize (@NotNull Vector2f   _size) { super.setSize (_size); style.editTransform(); }
    // @Override public void setSizeX(         float      x    ) { super.setSizeX(x    ); style.editTransform(); }
    // @Override public void setSizeY(         float      y    ) { super.setSizeY(y    ); style.editTransform(); }
    // @Override public void scale   (@NotNull Vector2f   _size) { super.scale   (_size); style.editTransform(); }
    // @Override public void scaleX  (         float      x    ) { super.scaleX  (x    ); style.editTransform(); }
    // @Override public void scaleY  (         float      y    ) { super.scaleY  (y    ); style.editTransform(); }

    // @Override public void setPos  (@NotNull Vector2f   _pos ) { super.setPos  (_pos ); style.editTransform(); }
    // @Override public void setPosX (         float      x    ) { super.setPosX (x    ); style.editTransform(); }
    // @Override public void setPosY (         float      y    ) { super.setPosY (y    ); style.editTransform(); }
    // @Override public void move    (@NotNull Vector2f   _pos ) { super.move    (_pos ); style.editTransform(); }
    // @Override public void moveX   (         float      x    ) { super.moveX   (x    ); style.editTransform(); }
    // @Override public void moveY   (         float      y    ) { super.moveY   (y    ); style.editTransform(); }








    protected Elm(@NotNull ServerWorld _world, CustomDisplay _entity, @NotNull ElmStyle _style) {
        super();
        world  = _world;
        entity = _entity;
        style  = _style;
        style.resetAll();
    }




    /**
     * Flushes changeable style values to the entity.
     * This does not start an interpolation.
     */
    public void flushStyle() {
        // //FIXME use flagged values for basic alignment, position and size
        { Flagged<Transform>     f = style.getFlaggedTransform();     if(f.isFlagged()) { entity.setTransformation(__calcTransform().toMinecraftTransform()); f.unflag(); }}
        { Flagged<Float>         f = style.getFlaggedViewRange();     if(f.isFlagged()) { entity.setViewRange     (f.get()                                 ); f.unflag(); }}
        { Flagged<BillboardMode> f = style.getFlaggedBillboardMode(); if(f.isFlagged()) { entity.setBillboardMode (f.get()                                 ); f.unflag(); }}
    }


    @Override
    protected void updateAbsPos() {
        Vector2f oldPos = new Vector2f(getAbsPos());
        super.updateAbsPos();
        if(!getAbsPos().equals(oldPos)) style.editTransform();
        //! This check's sole purpose is to prevent unneeded transform updates and comparisons
    }


    @Override
    protected void updateZIndex() {
        int oldZIndex = getZIndex();
        super.updateZIndex();
        if(getZIndex() != oldZIndex) style.editTransform();
        //! This check's sole purpose is to prevent unneeded transform updates and comparisons
    }


    @Override
    public int getLayerCount() {
        return 1;
    }


    /**
     * Calculates the final transform to apply to the entity.
     * This takes into account the element's position, alignment options and visual transform.
     * @return The transform.
     */
    protected Transform __calcTransform() {
        return style.getTransform().copy()
            .move(getAbsPos().x, getAbsPos().y, getZIndex() * 0.001f) //TODO move Z layer spacing to config file
        ;
    }






    /**
     * Instantly calculates animation steps and adds this element to the update queue.
     * ! Partial steps at the end of the animation are expanded to cover the entire step.
     * @param animation The animation to apply.
     */
    @Override
    public void applyAnimation(@NotNull Animation animation) {
        super.applyAnimation(animation);

        // Add element to the update queue and update the queued state
        if(!isQueued) {
            elmUpdateQueue.add(this);
            isQueued = true;
            queueLingerTicks = QUEUE_LINGER_TICKS;
        }

        // Apply each transition one at a time
        int shift = 0;
        for (Transition transition : animation.getTransitions()) {
            shift += __applyAnimationTransition(transition, shift);
        }
    }




    /**
     * Instantly applies an animation, ignoring transition times and easings. This does not wait for the tick cycle. Entities are updated instantly.
     * @param animation The animation to apply.
     */
    @Override
    public void applyAnimationNow(@NotNull Animation animation) {
        super.applyAnimationNow(animation);

        // Apply each transition one at a time
        for (Transition transition : animation.getTransitions()) {
            __applyAnimationTransitionNow(transition);
        }
        flushStyle();
        entity.setInterpolationDuration(0);
        entity.setStartInterpolation();
    }


    protected void __applyAnimationTransitionNow(@NotNull Transition t) {
        if(t.d.hasTransform()) {
            if(t.isAdditive()) style.editTransform().apply(t.d.getTransform());
            else               style.editTransform().set  (t.d.getTransform());
        }
    }




    /**
     * Instantly calculates the steps of a single transition and adds them to this element's future transforms.
     * @param transition The transition to calculate.
     * @param shift the amount of future transforms to skip before applying this transition.
     * @return The amount of future transforms this transition affected.
     */
    private int __applyAnimationTransition(@NotNull Transition transition, int shift) {


        // Calculate animation as a list of steps
        List<TransitionStep> animationSteps = new ArrayList<>();
        // Transform totTransform = new Transform();       // The sum of all the changes applied by the current and previous steps of the animation
        int time = transition.getDuration();            // The duration of this transition
        // int i = TRANSITION_REFRESH_TIME;
        // System.out.println("{"); //TODO REMOVE
        Easing e = transition.getEasing();
        for(int i = 0; i < time; i += TRANSITION_REFRESH_TIME) {

            // Calculate interpolation factor and add the new animation step to the list
            // float factor = (float)transition.getEasing().compute(Math.min(1d, (double)i / (double)time));
            // float factor = (float)(
            //     e.compute(Math.min(1d, (double)(i + TRANSITION_REFRESH_TIME) / time)) -
            //     e.compute(Math.min(1d, (double) i                            / time))
            // );
            float factor = (float)e.compute(Math.min(1d, (double)(i + TRANSITION_REFRESH_TIME) / time));
            animationSteps.add(transition.createStep(factor));
            // System.out.println("    " + (animationSteps.get(animationSteps.size() - 1).d.hasTransform() ? animationSteps.get(animationSteps.size() - 1).d.getTransform().getRot().toString() : "null")); //TODO REMOVE
        }
        // System.out.println("}"); //TODO REMOVE

        // // Add padding step //! This makes the actual duration match the duration specified in the transition (or be greater than it, which is not an issue)
        // animationSteps.add(transition.createStep(totTransform, 1));

        // Create the necessary amount of future data before applying the steps
        futureDataQueue.getOrAdd(
            shift + animationSteps.size() - 1,
            () -> {
                return futureDataQueue.isEmpty() ?
                __generateInterpolatedData() :
                __generateInterpolatedData(futureDataQueue.size() - 1);
            }
        );


        // Update existing future steps
        int j = 0;
        // if(!transitionStepQueue.isEmpty()) {
        TransitionStep step = null;

        // Update existing future steps and add new ones if needed
        // for(; j + shift < transitionStepQueue.size() && j < animationSteps.size(); ++j) {
        // System.out.println("{"); //TODO REMOVE
        for(; j < animationSteps.size(); ++j) {
            step = animationSteps.get(j);
            int k = j + shift; //FIXME remove K if not needed
            // Transform t00 = futureDataQueue.getOrAdd(k, () -> k == 0 ? __generateInterpolatedData() : __generateInterpolatedData(k - 1)).getTransform(); //TODO REMOVE
            // Vector3f v00 = new Vector3f(); //TODO REMOVE
            // if(t00 != null) t00.getRot().getEulerAnglesXYZ(v00); //TODO REMOVE
            // futureDataQueue.getOrAdd(k, () -> k == 0 ? __generateInterpolatedData() : __generateInterpolatedData(k - 1)).apply(step);
            futureDataQueue.get(k).apply(step);
            // System.out.println("factor: " + step.getFactor()); //TODO REMOVE
            // Quaternionf q = (futureDataQueue.get(j + shift).hasTransform() ? futureDataQueue.get(j + shift).getTransform().getRot() : new Quaternionf()); //TODO REMOVE
            // Quaternionf q0 = step.d.hasTransform() ? step.d.getTransform().getRot() : new Quaternionf(); //TODO REMOVE
            // Vector3f v = new Vector3f(); //TODO REMOVE
            // Vector3f v0 = new Vector3f(); //TODO REMOVE
            // q.getEulerAnglesXYZ(v); //TODO REMOVE
            // q0.getEulerAnglesXYZ(v0); //TODO REMOVE
            // System.out.println("    @" + Integer.toHexString(System.identityHashCode(futureDataQueue.get(j + shift))) + " :" + v.y + "        from step rotation " + v0.y * step.getFactor() + "        before: " + v00.y); //TODO REMOVE
            // System.out.println("    " + (step.d.hasTransform() ? step.d.getTransform().getRot().toString() : "null")); //TODO REMOVE
        }

        // If the amount of future steps is larger than the amount of steps, apply the last step to the remaining steps
        // if(j >= animationSteps.size()) {
        // if(j >= animationSteps.size()) {
        for(; j + shift < futureDataQueue.size(); ++j) {
            // Transform t00 = futureDataQueue.get(j + shift).getTransform(); //TODO REMOVE
            // Vector3f v00 = new Vector3f(); //TODO REMOVE
            // if(t00 != null) t00.getRot().getEulerAnglesXYZ(v00); //TODO REMOVE
            futureDataQueue.get(j + shift).apply(step);
            // System.out.println("factor: " + step.getFactor()); //TODO REMOVE
            // Quaternionf q = (futureDataQueue.get(j + shift).hasTransform() ? futureDataQueue.get(j + shift).getTransform().getRot() : new Quaternionf()); //TODO REMOVE
            // Quaternionf q0 = step.d.hasTransform() ? step.d.getTransform().getRot() : new Quaternionf(); //TODO REMOVE
            // Vector3f v = new Vector3f(); //TODO REMOVE
            // Vector3f v0 = new Vector3f(); //TODO REMOVE
            // q.getEulerAnglesXYZ(v); //TODO REMOVE
            // q0.getEulerAnglesXYZ(v0); //TODO REMOVE
            // System.out.println("    @" + Integer.toHexString(System.identityHashCode(futureDataQueue.get(j + shift))) + " :" + v.y + "        from step rotation " + v0.y * step.getFactor() + "        before: " + v00.y); //TODO REMOVE
            // System.out.println("    " + (step.d.hasTransform() ? step.d.getTransform().getRot().toString() : "null")); //TODO REMOVE
        }
        // System.out.println("}"); //TODO REMOVE
        // }
        // }


        // // Add remaining future steps
        // // Transform lastTransform = transitionStepQueue.isEmpty() ? style.getTransform() : transitionStepQueue.getLast();
        // // InterpolatedData lastData = transitionStepQueue.isEmpty() ? new InterpolatedData(null, null, null) : transitionStepQueue.getLast();
        // for(; j < animationSteps.size(); ++j) {
        //     // transitionStepQueue.add(new InterpolatedData(lastData.getTransform(), lastData.getBackground(), lastData.getOpacity()));
        //     transitionStepQueue.add(new InterpolatedData(null, null, null));
        //     var step = animationSteps.get(j);
        //     transitionStepQueue(j + shift).apply(step);
        // }


        // Return transition width
        return animationSteps.size();
    }




    // /**
    //  * Applies a single animation step.
    //  * @param index The index of the future transform to apply the step to.
    //  * @param step The animation step.
    //  * @return The modified transform.
    //  */
    // protected @NotNull Transform applyTransitionStep(int index, @NotNull TransitionStep step){
    //     Transform ft = transitionStepQueue.get(index);
    //     if(step.isAdditive) ft.interpolate(ft.clone().apply(step.transform), step.factor);
    //     else                ft.interpolate(step.transform,                   step.factor);
    //     return ft;
    // }

    /**
     * Applies a single animation step.
     * @param step The animation step.
     * @return The modified transform.
     */
    protected void __applyTransitionStep(@NotNull InterpolatedData d){
        if(d.hasTransform()) style.setTransform(d.getTransform());
        // Transform ft = transitionStepQueue.get(index);
        // if(step.isAdditive) ft.interpolate(ft.clone().apply(step.transform), step.factor);
        // else                ft.interpolate(step.transform,                   step.factor);
        // return ft;
    }


    protected InterpolatedData __generateInterpolatedData(){
        return new InterpolatedData(
            style.getTransform().copy(),
            null,
            null
        );
    }
    protected InterpolatedData __generateInterpolatedData(int index){
        return new InterpolatedData(
            futureDataQueue.get(index).getTransform().copy(),
            null,
            null
        );
    }




    /**
     * Spawns the element and its associated entities into the world.
     */
    @Override
    public void spawn(Vector3d pos) {

        // Flush previous changes to the entity to avoid bad interpolations and spawn the entity into the world
        flushStyle();
        Animation primerAnimation = style.getPrimerAnimation();
        if(primerAnimation != null) {
            applyAnimationNow(primerAnimation);
        }
        entity.spawn(world, pos);


        // Set tracking custom name
        entity.setCustomNameVisible(false);
        entity.setCustomName(new Txt(ENTITY_CUSTOM_NAME).get());


        // Handle animations
        Animation animation = style.getSpawnAnimation();
        if(animation != null) {
            applyAnimation(animation);
        }


        // Call superclass spawn and set spawned flag to true
        super.spawn(pos);
        isSpawned = true;
    }




    /**
     * Removes the element and its associated entities from the world.
     */
    @Override
    public void despawn() {
        super.despawn();
        isSpawned = false;

        // Handle animations
        Animation animation = style.getDespawnAnimation();
        if(animation != null) {
            applyAnimation(animation);

            // Remove entity from the world after a delay
            Scheduler.schedule(animation.getTotalDuration(), this::despawnNow);
        }
        else {
            despawnNow();
        }
    }




    /**
     * Instantly removes the entities associated with this element from the world.
     */
    @Override
    public void despawnNow() {
        super.despawnNow();
        isSpawned = false;
        entity.despawn();
    }




    /**
     * Processes the first step of the scheduled transitions of this Elm.
     * @return false if the element has been removed from the update queue, true otherwise.
     */
    protected boolean stepTransition() {
        // style.setTransform(transitionStepQueue.removeFirst());
        // System.out.println("Size: " + transitionStepQueue.size());
        __applyTransitionStep(futureDataQueue.removeFirst());
        flushStyle();
        entity.setInterpolationDuration(TRANSITION_REFRESH_TIME);
        entity.setStartInterpolation();

        if(futureDataQueue.isEmpty()) {
            if(queueLingerTicks > 0) {
                elmUpdateQueue.remove(this);
                isQueued = false;
            }
            else {
                --queueLingerTicks;
            }
            return false;
        }
        return true;
    }




    /**
     * Processes the first step of the scheduled transitions of all the queued elements
     * Must be called at the end of the tick every TRANSITION_REFRESH_TIME ticks. //FIXME make unaligned
     */
    public static void processUpdateQueue(){

        for (int i = 0; i < elmUpdateQueue.size();) {
            if(elmUpdateQueue.get(i).stepTransition()) ++i;
        }
    }




    // /**
    //  * Helper method that checks if the player's view intersects with the hitbox of this element.
    //  * @param player The player.
    //  * @return True if the view intersets with the hitbox, false otherwise.
    //  */
    // public abstract boolean checkIntersection(PlayerEntity player);




    /**
     * Updates the new hover status of the element and executes the specified callbacks.
     * @param player The player to check the view of. Can be null.
     */
    public void updateHoverStatus(@Nullable PlayerEntity player){
        if(this instanceof Hoverable h) {
            boolean hoverStatusNext;


            // Calculate next hover status
            if(player == null) {
                hoverStatusNext = false;
            }
            else {
                hoverStatusNext = checkIntersection(player);
            }


            // Update current status and run hover status change callbacks if needed
            if(isHovered != hoverStatusNext) {
                isHovered = hoverStatusNext;
                if(isHovered) {
                    h.onHoverEnter();
                }
                else {
                    h.onHoverExit();
                }
            }
        }
    }








    public boolean checkIntersection(PlayerEntity player) {
        if(!isSpawned || style.getBillboardMode() != BillboardMode.FIXED) return false;
        Transform t = __calcTransform();


        // Calculate the world coordinates of the display's origin. //! Left rotation and scale are ignored as they doesn't affect this
        Vector3f origin =
            entity.getPosCopy()
            .add   (t.getPos())
            .rotate(t.getGlobalRot())
        ;


        // // Calculate display size
        // Vector2f size = new Vector2f(
        //     calcWidth(),
        //     calcHeight()
        // );
        // System.out.println("Size: " + getAbsSize().toString());


        // Calculate corner X position relative to the origin using the entity's local coordinate system
        Vector3f shiftX = new Vector3f(getAbsSize().x / 2, 0, 0);
        shiftX.rotate(t.getRot()).rotate(t.getGlobalRot());


        // Check view intersection with the display's box
        Vector3f corner1 = new Vector3f(origin).sub(shiftX);
        Vector3f corner2 = new Vector3f(origin).add(shiftX).add(0, getAbsSize().y, 0);
        world.spawnParticles(ParticleTypes.BUBBLE_POP, corner1.x, corner1.y, corner1.z, 0, 0, 0, 0, 0);
        world.spawnParticles(ParticleTypes.BUBBLE_POP, corner2.x, corner2.y, corner2.z, 0, 0, 0, 0, 0);
        return SpaceUtils.checkLineRectangleIntersection(
            player.getEyePos().toVector3f(),
            player.getRotationVec(1f).toVector3f(),
            corner1,
            corner2
        );
    }




    /**
     * Checks for stray tracked displays and purges them.
     * Must be called on entity load event.
     * @param entity The entity.
     */
    public static void onEntityLoad(@NotNull Entity entity) {
        if (entity instanceof TextDisplayEntity) {
            World world = entity.getWorld();
            if(
                world != null &&
                entity.getCustomName() != null &&
                entity.getCustomName().getString().equals(ENTITY_CUSTOM_NAME)
            ) {
                entity.remove(RemovalReason.KILLED);
            }
        }
    }
}




//FIXME SPAWN INTERACTION ENTITY WHEN SHOPS ARE FOCUSED
//FIXME use it to visually stop client-side interactions with blocks and entities behind it
