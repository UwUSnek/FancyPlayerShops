package com.snek.framework.ui.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector4i;

import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.InterpolatedData;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.data_types.containers.Flagged;
import com.snek.framework.data_types.displays.CustomDisplay;
import com.snek.framework.data_types.displays.CustomTextDisplay;
import com.snek.framework.ui.styles.ElmStyle;
import com.snek.framework.ui.styles.FancyTextElmStyle;
import com.snek.framework.ui.styles.PanelElmStyle;
import com.snek.framework.ui.styles.TextElmStyle;
import com.snek.framework.utils.Txt;

import net.minecraft.entity.decoration.DisplayEntity.BillboardMode;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;








/**
 * A TextElm that also has a configurable, animatable background color.
 */
public class FancyTextElm extends Elm {

    // In-world data
    private @NotNull CustomDisplay text;
    public CustomTextDisplay getFgEntity() { return (CustomTextDisplay)text; }
    public CustomTextDisplay getBgEntity() { return (CustomTextDisplay)getEntity(); }
    private FancyTextElmStyle getStyle() { return (FancyTextElmStyle)style; }








    /**
     * Creates a new FancyTextElm using a custom style.
     * @param _world The world in which to place the element.
     * @param _style The custom style.
     */
    protected FancyTextElm(@NotNull ServerWorld _world, @NotNull ElmStyle _style) {

        // Create element and background element
        super(_world, new CustomTextDisplay(_world), _style);
        text = new CustomTextDisplay(_world);

        // Initialize permanent entity values
        getBgEntity().setText(new Txt("").get());
        getFgEntity().setBackground(new Vector4i(0, 0, 0, 0));
    }


    /**
     * Creates a new FancyTextElm using the default style.
     * @param _world The world in which to place the element.
     */
    public FancyTextElm(@NotNull ServerWorld _world){
        this(_world, new FancyTextElmStyle());
    }








    @Override
    public void flushStyle() {
        //! super.flushStyle() not called as it unflags style data but only applies it to the background element.
        //! The transform also needs to be applied differently because of the Z-Layer size of fancy text elements.

        // Alias entities
        CustomTextDisplay fg = getFgEntity();
        CustomTextDisplay bg = getBgEntity();


        // Handle transforms
        {
            Flagged<Transform> f = style.getFlaggedTransform();
            Flagged<Transform> fFg = getStyle().getFlaggedTransformFg();
            if(f.isFlagged() || fFg.isFlagged()) {
                fg.setTransformation(
                    __calcTransform()
                    .apply(getStyle().getTransformFg())
                    .moveZ((getZIndex() + 1) * 0.001f) //TODO move Z layer spacing to config file
                    .scale(TextElmStyle.DEFAULT_TEXT_SCALE)
                    .toMinecraftTransform()
                );
                f.unflag();
                fFg.unflag();
            }
            Flagged<Transform> fBg = getStyle().getFlaggedTransformBg();
            if(f.isFlagged() || fBg.isFlagged()) {
                bg.setTransformation(
                    __calcTransform()
                    .apply(getStyle().getTransformBg())
                    .scaleX(PanelElmStyle.ENTITY_BLOCK_RATIO_X * getAbsSize().x)
                    .scaleY(PanelElmStyle.ENTITY_BLOCK_RATIO_Y * getAbsSize().y)
                    .moveX(PanelElmStyle.ENTITY_SHIFT_X * getAbsSize().x)
                    .toMinecraftTransform()
                );
                f.unflag();
                fBg.unflag();
            }
        }


        // Handle the other values normally
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

        getFgEntity().setInterpolationDuration(0);
        getFgEntity().setStartInterpolation();
    }




    @Override
    protected void __applyAnimationTransitionNow(@NotNull Transition t) {
        super.__applyAnimationTransitionNow(t);
        if(t.d.hasOpacity    ()) getStyle().setTextOpacity(t.d.getOpacity    ());
        if(t.d.hasBackground ()) getStyle().setBackground (t.d.getBackground ());
        if(t.d.hasTransformFg()) getStyle().setTransformFg(t.d.getTransformFg());
        if(t.d.hasTransformBg()) getStyle().setTransformBg(t.d.getTransformBg());
    }




    @Override
    protected void __applyTransitionStep(@NotNull InterpolatedData d){
        super.__applyTransitionStep(d);
        if(d.hasOpacity    ()) getStyle().setTextOpacity(d.getOpacity    ());
        if(d.hasBackground ()) getStyle().setBackground (d.getBackground ());
        if(d.hasTransformFg()) getStyle().setTransformFg(d.getTransformFg());
        if(d.hasTransformBg()) getStyle().setTransformBg(d.getTransformBg());
    }




    @Override
    protected InterpolatedData __generateInterpolatedData(){
        return new InterpolatedData(
            getStyle().getTransform().copy(),
            new Vector4i(getStyle().getBackground()),
            getStyle().getTextOpacity(),
            getStyle().getTransformFg().copy(),
            getStyle().getTransformBg().copy()
        );
    }
    @Override
    protected InterpolatedData __generateInterpolatedData(int index){
        return new InterpolatedData(
            futureDataQueue.get(index).getTransform().copy(),
            new Vector4i(futureDataQueue.get(index).getBackground()),
            futureDataQueue.get(index).getOpacity(),
            futureDataQueue.get(index).getTransformFg().copy(),
            futureDataQueue.get(index).getTransformBg().copy()
        );
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
    public boolean stepTransition(){
        boolean r = super.stepTransition();
        getFgEntity().setInterpolationDuration(TRANSITION_REFRESH_TIME);
        getFgEntity().setStartInterpolation();
        getFgEntity().tick();
        return r;
    }
}