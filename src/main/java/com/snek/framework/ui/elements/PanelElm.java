package com.snek.framework.ui.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector4i;

import com.snek.framework.data_types.animations.InterpolatedData;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.data_types.containers.Flagged;
import com.snek.framework.data_types.displays.CustomDisplay;
import com.snek.framework.data_types.displays.CustomTextDisplay;
import com.snek.framework.ui.styles.ElmStyle;
import com.snek.framework.ui.styles.PanelElmStyle;
import com.snek.framework.utils.Txt;

import net.minecraft.server.world.ServerWorld;








/**
 * A simple UI element with a background color and animations.
 * Panels default to a 1x1 blocks size.
 */
public class PanelElm extends Elm {
    private PanelElmStyle getStyle() { return (PanelElmStyle)style; }
    public CustomTextDisplay getPanelEntity() { return (CustomTextDisplay)entity; }





    /**
     * Creates a new PanelElm using an existing entity and a custom style.
     * @param _world The world in which to place the element.
     * @param _entity The display entity.
     * @param _style The custom style.
     */
    protected PanelElm(@NotNull ServerWorld _world, @NotNull CustomDisplay _entity, @NotNull ElmStyle _style) {
        super(_world, _entity, _style);
        getPanelEntity().setText(new Txt("").get());
    }


    /**
     * Creates a new PanelElm using a custom style.
     * @param _world The world in which to place the element.
     * @param _style The custom style.
     */
    protected PanelElm(@NotNull ServerWorld _world, @NotNull ElmStyle _style) {
        this(_world, new CustomTextDisplay(_world), _style);
    }


    /**
     * Creates a new PanelElm using the default style.
     * @param _world The world in which to place the element.
     */
    public PanelElm(@NotNull ServerWorld _world){
        this(_world, new CustomTextDisplay(_world), new PanelElmStyle());
    }




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
            getStyle().getTransform().copy(),
            new Vector4i(getStyle().getColor()),
            null
        );
    }
    @Override
    protected InterpolatedData __generateInterpolatedData(int index){
        return new InterpolatedData(
            futureDataQueue.get(index).getTransform().copy(),
            new Vector4i(futureDataQueue.get(index).getBackground()),
            null
        );
    }




    @Override
    public boolean stepTransition(){
        return super.stepTransition();
    }




    @Override
    protected Transform __calcTransform() {
        return super.__calcTransform()
            .scaleX(getAbsSize().x)
            .scaleY(getAbsSize().y)
        ;
    }
}
