package com.snek.framework.ui.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector4i;

import com.snek.framework.data_types.animations.InterpolatedData;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.data_types.containers.Flagged;
import com.snek.framework.data_types.containers.Pair;
import com.snek.framework.data_types.displays.CustomDisplay;
import com.snek.framework.data_types.displays.CustomTextDisplay;
import com.snek.framework.generated.FontSize;
import com.snek.framework.ui.styles.ElmStyle;
import com.snek.framework.ui.styles.TextElmStyle;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;








/**
 * An element that can display text.
 * This class has transparent background. For a text element with background color, use FancyTextElm.
 */
public class TextElm extends Elm {
    private TextElmStyle getStyle() { return (TextElmStyle)style; }

    // This value identifies the amount of rendered text pixels that fit in a minecraft block
    public static final int TEXT_PIXEL_BLOCK_RATIO = 40;




    /**
     * Creates a new TextElm using an existing entity and a custom style.
     * @param _world The world in which to place the element.
     * @param _entity The display entity.
     * @param _style The custom style.
     */
    protected TextElm(@NotNull ServerWorld _world, @NotNull CustomDisplay _entity, @NotNull ElmStyle _style) {
        super(_world, _entity, _style);
        ((CustomTextDisplay)entity).setBackground(new Vector4i(0, 0, 0, 0));
    }


    /**
     * Creates a new TextElm using a custom style.
     * @param _world The world in which to place the element.
     * @param _style The custom style.
     */
    protected TextElm(@NotNull ServerWorld _world, @NotNull ElmStyle _style) {
        this(_world, new CustomTextDisplay(_world), _style);
    }


    /**
     * Creates a new TextElm using the default style.
     * @param _world The world in which to place the element.
     */
    public TextElm(@NotNull ServerWorld _world){
        this(_world, new CustomTextDisplay(_world), new TextElmStyle());
    }




    @Override
    public void flushStyle() {
        super.flushStyle();
        CustomTextDisplay e2 = (CustomTextDisplay)entity;
        { Flagged<Text>     f = getStyle().getFlaggedText();        if(f.isFlagged()) { e2.setText       (f.get()); f.unflag(); }}
        { Flagged<Integer>  f = getStyle().getFlaggedTextOpacity(); if(f.isFlagged()) { e2.setTextOpacity(f.get()); f.unflag(); }}
    }




    @Override
    protected void __applyAnimationTransitionNow(@NotNull Transition t) {
        super.__applyAnimationTransitionNow(t);
        if(t.d.hasOpacity()) getStyle().setTextOpacity(t.d.getOpacity());
    }




    @Override
    protected void __applyTransitionStep(@NotNull InterpolatedData d){
        super.__applyTransitionStep(d);
        if(d.hasOpacity()) getStyle().setTextOpacity(d.getOpacity());
    }




    @Override
    protected InterpolatedData __generateInterpolatedData(){
        return new InterpolatedData(
            getStyle().getTransform().copy(),
            null,
            getStyle().getTextOpacity()
        );
    }
    @Override
    protected InterpolatedData __generateInterpolatedData(int index){
        return new InterpolatedData(
            futureDataQueue.get(index).getTransform().copy(),
            null,
            futureDataQueue.get(index).getOpacity()
        );
    }




    @Override
    public void spawn(Vector3d pos) {
        if(style.getDespawnAnimation() != null) applyAnimationNow(style.getDespawnAnimation());
        super.spawn(pos);
    }


    @Override
    public void despawn() {
        super.despawn();
    }


    @Override
    public boolean stepTransition(){
        boolean r = super.stepTransition();
        ((CustomTextDisplay)entity).tick();
        return r;
    }




    /**
     * Calculates the height of the associated TextDisplay entity.
     * NOTICE: The height can be inaccurate as a lot of assumptions are made to calculate it.
     *     The returned value is the best possible approximation.
     * NOTICE: This operation is fairly expensive. The result should be cached whenever possible.
     * NOTICE: Wrapped lines are counted as one.
     * @return The height in blocks.
     */
    public float calcHeight(){

        // Retrieve the current text as a string and count the number of lines
        final float lineNum = ((CustomTextDisplay)entity).getText().getString().split("\n").length;
        if(lineNum == 0) return 0;

        // Calculate their height and return it
        return ((lineNum == 1 ? 0 : lineNum - 1) * 2 + lineNum * FontSize.getHeight()) / TEXT_PIXEL_BLOCK_RATIO * style.getTransform().getScale().y;
    }




    /**
     * Calculates the width of the associated TextDisplay entity.
     * NOTICE: The width can be inaccurate as a lot of assumptions are made to calculate it.
     *     The returned value is the best possible approximation.
     * NOTICE: This operation is fairly expensive. The result should be cached whenever possible.
     * NOTICE: Wrapped lines are counted as one.
     * @return The width in blocks.
     */
    public float calcWidth(){

        // Retrieve the current text as a string
        final String[] lines = ((CustomTextDisplay)entity).getText().getString().split("\n");
        if(lines.length == 0) {
            return 0;
        }

        // Find the longest line if necessary
        final Pair<String, Integer> line = Pair.from(lines[0], lines[0].length());
        if(lines.length > 1) for(int i = 1; i < lines.length; ++i) {
            final int len = lines[i].length();
            if(len > line.second) {
                line.first = lines[i];
                line.second = len;
            }
        }

        // Calculate its length and return it
        return (float)FontSize.getWidth(line.first) / TEXT_PIXEL_BLOCK_RATIO * style.getTransform().getScale().x;
    }
}
