package com.snek.framework.ui;

import static com.snek.framework.ui.Elm.ENTITY_CUSTOM_NAME;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector4i;

import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.steps.AnimationStep;
import com.snek.framework.data_types.animations.steps.TextAnimationStep;
import com.snek.framework.data_types.animations.transitions.TextAdditiveTransition;
import com.snek.framework.data_types.animations.transitions.TextTargetTransition;
import com.snek.framework.data_types.animations.transitions.Transition;
import com.snek.framework.data_types.containers.Flagged;
import com.snek.framework.data_types.containers.IndexedArrayDeque;
import com.snek.framework.data_types.displays.CustomDisplay;
import com.snek.framework.data_types.displays.CustomTextDisplay;
import com.snek.framework.ui.styles.ElmStyle;
import com.snek.framework.ui.styles.FancyTextElmStyle;
import com.snek.framework.ui.styles.PanelElmStyle;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.Utils;

import net.minecraft.entity.decoration.DisplayEntity.BillboardMode;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;








/**
 * A TextElm that also has a configurable, animatable background color.
 */
public class FancyTextElm extends Elm {

    // This value identifies the amount of rendered text pixels that fit in a minecraft block
    public static final int TEXT_PIXEL_BLOCK_RATIO = 40;


    // Animations
    protected final @NotNull IndexedArrayDeque<Vector4i> backgroundQueue = new IndexedArrayDeque<>(); // The list of backgrounds to apply to this instance in the next ticks. 1 for each update tick
    protected final @NotNull IndexedArrayDeque<Integer>  alphaQueue      = new IndexedArrayDeque<>(); // The list of opacities   to apply to this instance in the next ticks. 1 for each update tick


    // In-world data
    protected @NotNull CustomDisplay text;
    public CustomTextDisplay getBgEntity() { return (CustomTextDisplay)getEntity(); }
    public CustomTextDisplay getFgEntity() { return (CustomTextDisplay)text; }
    // private TextElmStyle getStyle() { return (TextElmStyle)style; }


    private FancyTextElmStyle getStyle() { return (FancyTextElmStyle)style; }







    // protected FancyTextElm(@NotNull ServerWorld _world, @NotNull CustomDisplay _entity, @NotNull ElmStyle _style) {

    //     // Create element and background element
    //     super(_world, _entity, _style);
    //     text = (TextElm)addChild(new TextElm(_world));

    //     // // Copy background color to background element, then make the background transparent
    //     // ((PanelElmStyle)style).setColor(((TextElmStyle)text.style).getBackground());
    //     // ((TextElmStyle)text.style).setBackground(new Vector4i(0, 0, 0, 0));
    // }


    protected FancyTextElm(@NotNull ServerWorld _world, @NotNull ElmStyle _style) {

        // Create element and background element
        super(_world, new CustomTextDisplay(_world), _style);
        text = new CustomTextDisplay(_world);

        // Initialize permanent entity values
        getBgEntity().setText(new Txt("").get());
        getFgEntity().setBackground(new Vector4i(0, 0, 0, 0));
    }


    public FancyTextElm(@NotNull ServerWorld _world){
        this(_world, new FancyTextElmStyle());
    }








    @Override
    public void flushStyle() {
        //! super.flushStyle() not called as it unflags style data but only applies it to the background element.

        CustomTextDisplay fg = getFgEntity();
        CustomTextDisplay bg = getBgEntity();

        {Flagged<Transform> f = style.getFlaggedTransform();
        if(f.isFlagged()) {
            fg.setTransformation(__calcTransform().clone().moveZ((getZIndex() + 1) * 0.001f).toMinecraftTransform()); //TODO move Z layer spacing to config file
            bg.setTransformation(__calcTransform()                                          .toMinecraftTransform());
            f.unflag();
        }}
        {Flagged<Float> f = style.getFlaggedViewRange();
        if(f.isFlagged()) {
            fg.setViewRange(f.get());
            bg.setViewRange(f.get());
            f.unflag();
        }}
        {Flagged<BillboardMode> f = style.getFlaggedBillboardMode();
        if(f.isFlagged()) {
            fg.setBillboardMode(f.get());
            bg.setBillboardMode(f.get());
            f.unflag();
        }}

        { Flagged<Text>     f = getStyle().getFlaggedText();        if(f.isFlagged()) { fg.setText       (f.get()); f.unflag(); }}
        { Flagged<Integer>  f = getStyle().getFlaggedTextOpacity(); if(f.isFlagged()) { fg.setTextOpacity(f.get()); f.unflag(); }}
        { Flagged<Vector4i> f = getStyle().getFlaggedBackground();  if(f.isFlagged()) { bg.setBackground (f.get()); f.unflag(); }}
    }

    @Override
    public int getLayerCount() {
        return 2;
    }




    @Override
    public void applyAnimationNow(@NotNull Animation animation) {
        super.applyAnimationNow(animation);

        // Apply each transition one at a time
        for (Transition transition : animation.getTransitions()) {
            __applyAnimationTransitionNow(transition);
        }
        flushStyle();
        getFgEntity().setInterpolationDuration(0);
        getFgEntity().setStartInterpolation();
        getBgEntity().setInterpolationDuration(0);
        getBgEntity().setStartInterpolation();
    }




    @Override
    protected void __applyAnimationTransitionNow(@NotNull Transition transition) {
        if(transition instanceof TextAdditiveTransition t) {
            getStyle().setBackground(t.getBackground());
            getStyle().setTextOpacity(t.getAlpha());
        }
        if(transition instanceof TextTargetTransition t) {
            getStyle().setBackground(t.getBackground());
            getStyle().setTextOpacity(t.getAlpha());
        }
        super.__applyAnimationTransitionNow(transition);
    }




    /**
     * Applies a single animation step.
     * @param index The index of the future background and alpha to apply the step to.
     * @param step The animation step.
     * @return The modified transform.
     */
    @Override
    protected @NotNull Transform __applyTransitionStep(int index, @NotNull AnimationStep step){


        if(step instanceof TextAnimationStep s) {
            // Calculate subclass step and get queued data
            Vector4i bg = backgroundQueue.getOrAdd(index, () -> new Vector4i(getStyle().getBackground()));
            int       a =      alphaQueue.getOrAdd(index, () ->              getStyle().getTextOpacity());

            // Interpolate background and alpha
            bg.set(Utils.interpolateARGB(bg, s.background, step.factor));
            alphaQueue.set(index, Utils.interpolateI(a, s.alpha, step.factor));
        }


        // Call superclass function
        return super.__applyTransitionStep(index, step);
    }




    @Override
    public void spawn(Vector3d pos) {

        flushStyle();
        getFgEntity().spawn(world, pos);

        // Set tracking custom name
        getFgEntity().setCustomNameVisible(false);
        getFgEntity().setCustomName(new Txt(Elm.ENTITY_CUSTOM_NAME).get());

        // Call superclass spawn
        super.spawn(pos);
    }




    @Override
    public void despawn() {
        super.despawn();
    }


    @Override
    public void despawnNow(){
        super.despawnNow();
        getFgEntity().despawn();
    }




    @Override
    public boolean tick(){
        if(!backgroundQueue.isEmpty()) getStyle().setBackground (backgroundQueue.removeFirst());
        if(     !alphaQueue.isEmpty()) getStyle().setTextOpacity(     alphaQueue.removeFirst());
        //! Update queue not checked as it depends exclusively on transform changes.

        boolean r = super.tick();
        getFgEntity().setInterpolationDuration(TRANSITION_REFRESH_TIME);
        getFgEntity().setStartInterpolation();
        return r;
    }




    // // @Override
    // // public void flushStyle(){
    //     // ((TextElmStyle)text.style).setBackground(new Vector4i(0, 0, 0, 0));
    //     // text.flushStyle();
    //     // super.flushStyle();
    // // }
}