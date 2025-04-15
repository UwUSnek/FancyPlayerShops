package com.snek.framework.ui.elements;

import static com.snek.framework.ui.elements.Elm.ENTITY_CUSTOM_NAME;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector4i;

import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.InterpolatedData;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.TransitionStep;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.data_types.containers.Flagged;
import com.snek.framework.data_types.containers.IndexedArrayDeque;
import com.snek.framework.data_types.displays.CustomDisplay;
import com.snek.framework.data_types.displays.CustomTextDisplay;
import com.snek.framework.ui.styles.ElmStyle;
import com.snek.framework.ui.styles.FancyTextElmStyle;
import com.snek.framework.ui.styles.PanelElmStyle;
import com.snek.framework.ui.styles.TextElmStyle;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.Utils;

import net.minecraft.entity.decoration.DisplayEntity.BillboardMode;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;








/**
 * A TextElm that also has a configurable, animatable background color.
 */
public class FancyTextElm extends Elm {

    // // Animations
    // protected final @NotNull IndexedArrayDeque<Vector4i> backgroundQueue = new IndexedArrayDeque<>(); // The list of backgrounds to apply to this instance in the next ticks. 1 for each update tick
    // protected final @NotNull IndexedArrayDeque<Integer>  alphaQueue      = new IndexedArrayDeque<>(); // The list of opacities   to apply to this instance in the next ticks. 1 for each update tick


    // In-world data
    private @NotNull CustomDisplay text;
    public CustomTextDisplay getFgEntity() { return (CustomTextDisplay)text; }
    public CustomTextDisplay getBgEntity() { return (CustomTextDisplay)getEntity(); }
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
        //! The transform also needs to be applied differently because of the Z-Layer size of fancy text elements.

        CustomTextDisplay fg = getFgEntity();
        CustomTextDisplay bg = getBgEntity();

        {Flagged<Transform> f = style.getFlaggedTransform();
        if(f.isFlagged()) {
            fg.setTransformation(
                __calcTransform()
                .moveZ((getZIndex() + 1) * 0.001f) //TODO move Z layer spacing to config file
                .scale(TextElmStyle.DEFAULT_TEXT_SCALE)
                .toMinecraftTransform()
            );
            bg.setTransformation(
                __calcTransform()
                .scaleX(PanelElmStyle.ENTITY_BLOCK_RATIO_X * getAbsSize().x)
                .scaleY(PanelElmStyle.ENTITY_BLOCK_RATIO_Y * getAbsSize().y)
                .moveX(PanelElmStyle.ENTITY_SHIFT_X)
                .toMinecraftTransform()
            );
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
    protected void updateAbsPos() {
        super.updateAbsPos();
    }


    @Override
    public int getLayerCount() {
        return 2;
    }




    @Override
    public void applyAnimationNow(@NotNull Animation animation) {
        super.applyAnimationNow(animation);

        getFgEntity().setInterpolationDuration(0);
        getFgEntity().setStartInterpolation();
    }




    @Override
    protected void __applyAnimationTransitionNow(@NotNull Transition t) {
        super.__applyAnimationTransitionNow(t);
        if(t.d.hasOpacity   ()) getStyle().setTextOpacity(t.d.getOpacity());
        if(t.d.hasBackground()) getStyle().setBackground(t.d.getBackground());
    }




    @Override
    protected void __applyTransitionStep(@NotNull InterpolatedData d){
        super.__applyTransitionStep(d);
        if(d.hasOpacity   ()) getStyle().setTextOpacity(d.getOpacity   ());
        if(d.hasBackground()) getStyle().setBackground (d.getBackground());
        // Transform ft = transitionStepQueue.get(
    }




    @Override
    protected InterpolatedData __generateInterpolatedData(){
        return new InterpolatedData(
            getStyle().getTransform().clone(),
            new Vector4i(getStyle().getBackground()),
            getStyle().getTextOpacity()
        );
    }
    @Override
    protected InterpolatedData __generateInterpolatedData(int index){
        return new InterpolatedData(
            transitionStepQueue.get(index).getTransform().clone(),
            new Vector4i(transitionStepQueue.get(index).getBackground()),
            transitionStepQueue.get(index).getOpacity()
        );
    }
    // /**
    //  * Applies a single animation step.
    //  * @param index The index of the future background and alpha to apply the step to.
    //  * @param step The animation step.
    //  * @return The modified transform.
    //  */
    // @Override
    // protected @NotNull Transform applyTransitionStep(int index, @NotNull TransitionStep step){


    //     if(step) {
    //         // Calculate subclass step and get queued data
    //         Vector4i bg = backgroundQueue.getOrAdd(index, () -> new Vector4i(getStyle().getBackground()));
    //         int       a =      alphaQueue.getOrAdd(index, () ->              getStyle().getTextOpacity());

    //         // Interpolate background and alpha
    //         bg.set(Utils.interpolateARGB(bg, s.background, step.factor));
    //         alphaQueue.set(index, Utils.interpolateI(a, s.alpha, step.factor));
    //     }


    //     // Call superclass function
    //     return super.applyTransitionStep(index, step);
    // }




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
        // if(!backgroundQueue.isEmpty()) getStyle().setBackground (backgroundQueue.removeFirst());
        // if(     !alphaQueue.isEmpty()) getStyle().setTextOpacity(     alphaQueue.removeFirst());
        // //! Update queue not checked as it depends exclusively on transform changes.

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