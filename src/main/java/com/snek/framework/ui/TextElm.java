package com.snek.framework.ui;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector4i;

import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.steps.AnimationStep;
import com.snek.framework.data_types.animations.steps.TextAnimationStep;
import com.snek.framework.data_types.animations.transitions.TextAdditiveTransition;
import com.snek.framework.data_types.animations.transitions.TextTargetTransition;
import com.snek.framework.data_types.animations.transitions.Transition;
import com.snek.framework.data_types.containers.Flagged;
import com.snek.framework.data_types.containers.IndexedArrayDeque;
import com.snek.framework.data_types.containers.Pair;
import com.snek.framework.data_types.displays.CustomDisplay;
import com.snek.framework.data_types.displays.CustomTextDisplay;
import com.snek.framework.generated.FontSize;
import com.snek.framework.ui.styles.ElmStyle;
import com.snek.framework.ui.styles.TextElmStyle;
import com.snek.framework.utils.SpaceUtils;
import com.snek.framework.utils.Utils;

import net.minecraft.entity.decoration.DisplayEntity.BillboardMode;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;








public class TextElm extends Elm {
    // This value identifies the amount of rendered text pixels that fit in a minecraft block
    public static final int TEXT_PIXEL_BLOCK_RATIO = 40;

    // Animations
    protected final @NotNull IndexedArrayDeque<Vector4i> backgroundQueue = new IndexedArrayDeque<>(); // The list of backgrounds to apply to this instance in the next ticks. 1 for each update tick
    protected final @NotNull IndexedArrayDeque<Integer>  alphaQueue      = new IndexedArrayDeque<>(); // The list of alpha values to set to this instance's text in the next ticks. 1 for each update tick

    // Element data
    public @NotNull Flagged<Text>          text;
    public @NotNull Flagged<Integer>       textOpacity;
    public @NotNull Flagged<Vector4i>      background;




    protected TextElm(@NotNull ServerWorld _world, @NotNull CustomDisplay _entity, @NotNull ElmStyle _style) {
        super(_world, _entity, _style);

        text        = Flagged.from(((TextElmStyle)style).getText());
        textOpacity = Flagged.from(((TextElmStyle)style).getTextOpacity());
        background  = Flagged.from(((TextElmStyle)style).getBackground());
    }

    protected TextElm(@NotNull ServerWorld _world, @NotNull ElmStyle _style) {
        this(_world, new CustomTextDisplay(_world), _style);
    }

    public TextElm(@NotNull ServerWorld _world){
        this(_world, new CustomTextDisplay(_world), new TextElmStyle());
    }




    /**
     * Flushes changeable style values to the entity.
     * This does not start an interpolation.
     */
    @Override
    public void flushStyle() {
        super.flushStyle();
        CustomTextDisplay e2 = (CustomTextDisplay)entity;
        if(text       .isFlagged()) { e2.setText       (text       .get()); text       .unflag(); }
        if(textOpacity.isFlagged()) { e2.setTextOpacity(textOpacity.get()); textOpacity.unflag(); }
        if(background .isFlagged()) { e2.setBackground (background .get()); background .unflag(); }
    }




    @Override
    protected void __applyAnimationTransitionNow(@NotNull Transition transition) {
        if(transition instanceof TextAdditiveTransition t) {
            background.set(t.getBackground());
            textOpacity.set(t.getAlpha());
        }
        if(transition instanceof TextTargetTransition t) {
            background.set(t.getBackground());
            textOpacity.set(t.getAlpha());
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
            Vector4i bg = backgroundQueue.getOrAdd(index, () -> new Vector4i(background.get()));
            int       a =      alphaQueue.getOrAdd(index, () -> textOpacity.get());

            // Interpolate background and alpha
            bg.set(Utils.interpolateARGB(bg, s.background, step.factor));
            alphaQueue.set(index, Utils.interpolateI(a, s.alpha, step.factor));
        }


        // Call superclass function
        return super.__applyTransitionStep(index, step);
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
    public boolean tick(){
        if(!backgroundQueue.isEmpty()) background .set(backgroundQueue.removeFirst());
        if(     !alphaQueue.isEmpty()) textOpacity.set(     alphaQueue.removeFirst());
        //! Update queue not checked as it depends exclusively on transform changes.
        //! Each transform change always corresponds to one background and one alpha change.

        return super.tick();
    }




    /**
     * Calculates the height of this text element.
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
        return ((lineNum == 1 ? 0 : lineNum - 1) * 2 + lineNum * FontSize.getHeight()) / TEXT_PIXEL_BLOCK_RATIO * transform.get().getScale().y;
    }




    /**
     * Calculates the width of this text element.
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
        return (float)FontSize.getWidth(line.first) / TEXT_PIXEL_BLOCK_RATIO * transform.get().getScale().x;
    }




    @Override
    public boolean checkIntersection(PlayerEntity player) {
        if(!isSpawned || billboardMode.get() != BillboardMode.FIXED) return false;


        // Calculate the world coordinates of the display's origin. //! Left rotation and scale are ignored as they doesn't affect this
        Vector3f origin =
            entity.getPosCopy()
            .add   (transform.get().getPos())
            .rotate(transform.get().getRrot())
        ;


        // Calculate display size
        Vector2f size = new Vector2f(
            calcWidth(),
            calcHeight()
        );


        // Calculate corner X position relative to the origin using the entity's local coordinate system
        Vector3f shiftX = new Vector3f(size.x / 2, 0,0 );
        shiftX.rotate(transform.get().getLrot()).rotate(transform.get().getRrot());


        // Check view intersection with the display's box
        Vector3f corner1 = new Vector3f(origin).sub(shiftX);
        Vector3f corner2 = new Vector3f(origin).add(shiftX).add(0, size.y, 0);
        return SpaceUtils.checkLineRectangleIntersection(
            player.getEyePos().toVector3f(),
            player.getRotationVec(1f).toVector3f(),
            corner1,
            corner2
        );
    }
}
