package com.snek.framework.ui.elements;

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
import com.snek.framework.ui.styles.PanelElmStyle;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.Utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;















/**
 * A simple UI element with a background color and animations.
 */
public class PanelElm extends Elm {


    private PanelElmStyle getStyle() { return (PanelElmStyle)style; }
    public CustomTextDisplay getPanelEntity() { return (CustomTextDisplay)entity; }


    // Animations
    // protected final @NotNull IndexedArrayDeque<Vector4i> colorQueue = new IndexedArrayDeque<>(); // The list of backgrounds to apply to this instance in the next ticks. 1 for each update tick





    protected PanelElm(@NotNull ServerWorld _world, @NotNull CustomDisplay _entity, @NotNull ElmStyle _style) {
        super(_world, _entity, _style);
        getPanelEntity().setText(new Txt("").get());
    }

    protected PanelElm(@NotNull ServerWorld _world, @NotNull ElmStyle _style) {
        this(_world, new CustomTextDisplay(_world), _style);
    }

    public PanelElm(@NotNull ServerWorld _world){
        this(_world, new CustomTextDisplay(_world), new PanelElmStyle());
    }




    /**
     * Flushes changeable style values to the entity.
     * This does not start an interpolation.
     */
    @Override
    public void flushStyle() {
        super.flushStyle();
        CustomTextDisplay e = getPanelEntity();
        { Flagged<Vector4i> f = getStyle().getFlaggedColor(); if(f.isFlagged()) { e.setBackground(f.get()); f.unflag(); }}
    }




    @Override
    protected void __applyAnimationTransitionNow(@NotNull Transition t) {
        super.__applyAnimationTransitionNow(t);
        if(t.d.hasBackground()) getStyle().setColor(t.d.getBackground());
    }




    @Override
    protected void __applyTransitionStep(@NotNull InterpolatedData d){
        super.__applyTransitionStep(d);
        if(d.hasBackground()) getStyle().setColor(d.getBackground());
    }




    @Override
    protected InterpolatedData __generateInterpolatedData(){
        return new InterpolatedData(
            getStyle().getTransform().clone(),
            new Vector4i(getStyle().getColor()),
            null
        );
    }
    @Override
    protected InterpolatedData __generateInterpolatedData(int index){
        return new InterpolatedData(
            futureDataQueue.get(index).getTransform().clone(),
            new Vector4i(futureDataQueue.get(index).getBackground()),
            null
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


    //     if(step instanceof TextAnimationStep s) {
    //         // Calculate subclass step and get queued data
    //         Vector4i bg = colorQueue.getOrAdd(index, () -> new Vector4i(getStyle().getColor()));

    //         // Interpolate background and alpha
    //         bg.set(Utils.interpolateARGB(bg, s.background, step.factor));
    //     }


    //     // Call superclass function
    //     return super.applyTransitionStep(index, step);
    // }




    @Override
    public boolean tick(){
        // //TODO remove print
        // if(!colorQueue.isEmpty()) getStyle().setColor(colorQueue.removeFirst());
        // //! Update queue not checked as it depends exclusively on transform changes.

        return super.tick();
    }






    /**
     * Calculates the final transform to apply to the entity.
     * This takes into account the element's position, alignment options and visual transform.
     * @return The transform.
     */
    @Override
    protected Transform __calcTransform() {
        return super.__calcTransform()
            .scaleX(getAbsSize().x)
            .scaleY(getAbsSize().y)
        ;
    }



    // /**
    //  * Calculates the height of this text element.
    //  * NOTICE: The height can be inaccurate as a lot of assumptions are made to calculate it.
    //  *     The returned value is the best possible approximation.
    //  * NOTICE: This operation is fairly expensive. The result should be cached whenever possible.
    //  * NOTICE: Wrapped lines are counted as one.
    //  * @return The height in blocks.
    //  */
    // public float calcHeight(){

    //     // Retrieve the current text as a string and count the number of lines
    //     final float lineNum = ((CustomTextDisplay)entity).getText().getString().split("\n").length;
    //     if(lineNum == 0) return 0;

    //     // Calculate their height and return it
    //     return ((lineNum == 1 ? 0 : lineNum - 1) * 2 + lineNum * FontSize.getHeight()) / TEXT_PIXEL_BLOCK_RATIO * transform.get().getScale().y;
    // }




    // /**
    //  * Calculates the width of this text element.
    //  * NOTICE: The width can be inaccurate as a lot of assumptions are made to calculate it.
    //  *     The returned value is the best possible approximation.
    //  * NOTICE: This operation is fairly expensive. The result should be cached whenever possible.
    //  * NOTICE: Wrapped lines are counted as one.
    //  * @return The width in blocks.
    //  */
    // public float calcWidth(){

    //     // Retrieve the current text as a string
    //     final String[] lines = ((CustomTextDisplay)entity).getText().getString().split("\n");
    //     if(lines.length == 0) {
    //         return 0;
    //     }

    //     // Find the longest line if necessary
    //     final Pair<String, Integer> line = Pair.from(lines[0], lines[0].length());
    //     if(lines.length > 1) for(int i = 1; i < lines.length; ++i) {
    //         final int len = lines[i].length();
    //         if(len > line.second) {
    //             line.first = lines[i];
    //             line.second = len;
    //         }
    //     }

    //     // Calculate its length and return it
    //     return (float)FontSize.getWidth(line.first) / TEXT_PIXEL_BLOCK_RATIO * transform.get().getScale().x;
    // }




    // @Override
    // public boolean checkIntersection(PlayerEntity player) {
    //     return false;
        // if(!isSpawned || billboardMode.get() != BillboardMode.FIXED) return false;


        // // Calculate the world coordinates of the display's origin. //! Left rotation and scale are ignored as they doesn't affect this
        // Vector3f origin =
        //     entity.getPosCopy()
        //     .add   (transform.get().getPos())
        //     .rotate(transform.get().getRrot())
        // ;


        // // Calculate display size
        // Vector2f size = new Vector2f(
        //     calcWidth(),
        //     calcHeight()
        // );


        // // Calculate corner X position relative to the origin using the entity's local coordinate system
        // Vector3f shiftX = new Vector3f(size.x / 2, 0,0 );
        // shiftX.rotate(transform.get().getLrot()).rotate(transform.get().getRrot());


        // // Check view intersection with the display's box
        // Vector3f corner1 = new Vector3f(origin).sub(shiftX);
        // Vector3f corner2 = new Vector3f(origin).add(shiftX).add(0, size.y, 0);
        // return SpaceUtils.checkLineRectangleIntersection(
        //     player.getEyePos().toVector3f(),
        //     player.getRotationVec(1f).toVector3f(),
        //     corner1,
        //     corner2
        // );
    // }
}
