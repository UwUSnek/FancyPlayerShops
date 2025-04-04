package com.snek.framework.ui;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector4i;

import com.snek.framework.custom_displays.CustomDisplay;
import com.snek.framework.custom_displays.CustomTextDisplay;
import com.snek.framework.data_types.Flagged;
import com.snek.framework.data_types.IndexedArrayDeque;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.steps.AnimationStep;
import com.snek.framework.data_types.animations.steps.TextAnimationStep;
import com.snek.framework.ui.styles.ElmStyle;
import com.snek.framework.ui.styles.TextElmStyle;
import com.snek.framework.utils.Utils;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;








public class TextElm extends Elm {

    // Animations
    protected final @NotNull IndexedArrayDeque<Vector4i> backgroundQueue = new IndexedArrayDeque<>(); // The list of backgrounds to apply to this instance in the next ticks. 1 for each update tick
    protected final @NotNull IndexedArrayDeque<Integer>  alphaQueue      = new IndexedArrayDeque<>(); // The list of alpha values to set to this instance's text in the next ticks. 1 for each update tick

    // Element data
    protected @NotNull Flagged<Text>          text;
    protected @NotNull Flagged<Integer>       textOpacity;
    protected @NotNull Flagged<Vector4i>      background;




    protected TextElm(@NotNull ServerWorld _world, @NotNull CustomDisplay _entity, @NotNull ElmStyle _style) {
        super(_world, _entity, _style);

        text        = Flagged.from(((TextElmStyle)style).getText());
        textOpacity = Flagged.from(((TextElmStyle)style).getTextOpacity());
        background  = Flagged.from(((TextElmStyle)style).getBackground());
        // textOpacity = Flagged.from(128);              // Changed on spawn
        // background  = Flagged.from(new Vector4i(0));  // Changed on spawn
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
            Vector4i bg = backgroundQueue.getOrAdd(index, () -> { return new Vector4i(background.get()); });
            int       a =      alphaQueue.getOrAdd(index, () -> { return textOpacity.get(); });

            // Interpolate background and alpha
            bg.set(Utils.interpolateARGB(bg, s.background, step.factor));
            alphaQueue.set(index, Utils.interpolateI(a, s.alpha, step.factor));
        }


        // Call superclass function
        return super.__applyTransitionStep(index, step);
    }




    @Override
    public void spawn(Vector3d pos) {
        super.spawn(pos);
    }


    @Override
    public void despawn() {
        super.despawn();
    }


    @Override
    public boolean tick(){
        background .set(backgroundQueue.removeFirst());
        textOpacity.set(     alphaQueue.removeFirst());
        //! Update queue not checked as it depends exclusively on transform changes.
        //! Each transform change always corresponds to one background and one alpha change.

        return super.tick();
    }
}
