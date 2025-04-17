package com.snek.framework.data_types.animations;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4i;

import com.snek.framework.utils.Utils;








/**
 * A collection of data that can be interpolated by Minecraft's client.
 * This is used to pre-calculate animations.
 */
public class InterpolatedData {
    private @Nullable Transform transform;
    private @Nullable Transform transformFg;
    private @Nullable Transform transformBg;
    private @Nullable Vector4i background;
    private @Nullable Integer opacity;




    /**
     * Creates a new InterpolatedData.
     * @param _transform The transform.
     * @param _background The background color.
     * @param _opacity The foreground text opacity.
     * @param _transformFg The transform applied to the foreground of FancyTextElm.
     * @param _transformBg The transform applied to the background of FancyTextElm.
     */
    public InterpolatedData(@Nullable Transform _transform, @Nullable Vector4i _background, @Nullable Integer _opacity, Transform _transformFg, Transform _transformBg) {
        transform = _transform;
        background = _background;
        opacity = _opacity;
        transformFg = _transformFg;
        transformBg = _transformBg;
    }


    /**
     * Creates a new InterpolatedData.
     * @param _transform The transform.
     * @param _background The background color.
     * @param _opacity The foreground text opacity.
     */
    public InterpolatedData(@Nullable Transform _transform, @Nullable Vector4i _background, @Nullable Integer _opacity) {
        this(_transform, _background, _opacity, null, null);
    }




    /**
     * Applies a transition step to this data collection.
     * @param s The step to apply.
     */
    public void apply(@NotNull TransitionStep s) {
        if(s.d.hasTransform() && hasTransform()) {
            if(s.isAdditive()) transform.interpolate(transform.copy().apply(s.d.getTransform()), s.getFactor());
            else               transform.interpolate(                       s.d.getTransform(),  s.getFactor());
        }
        if(s.d.hasTransformFg() && hasTransformFg()) {
            if(s.isAdditive()) transformFg.interpolate(transformFg.copy().apply(s.d.getTransformFg()), s.getFactor());
            else               transformFg.interpolate(                         s.d.getTransformFg(),  s.getFactor());
        }
        if(s.d.hasTransformBg() && hasTransformBg()) {
            if(s.isAdditive()) transformBg.interpolate(transformBg.copy().apply(s.d.getTransformBg()), s.getFactor());
            else               transformBg.interpolate(                         s.d.getTransformBg(),  s.getFactor());
        }
        if(s.d.hasBackground() && hasBackground()) {
            background.set(Utils.interpolateARGB(background, s.d.getBackground(), s.getFactor()));
        }
        if(s.d.hasOpacity() && hasOpacity()) {
            //! Integer doesnt need initialization
            opacity = Utils.interpolateI(opacity, s.d.getOpacity(), s.getFactor());
        }
    }



    // Checks
    public boolean hasTransformFg() { return transformFg != null; }
    public boolean hasTransformBg() { return transformBg != null; }
    public boolean hasTransform  () { return transform   != null; }
    public boolean hasBackground () { return background  != null; }
    public boolean hasOpacity    () { return opacity     != null; }

    // Setters
    public void setTransformFg(@Nullable Transform _transformFg) { transformFg = _transformFg; }
    public void setTransformBg(@Nullable Transform _transformBg) { transformBg = _transformBg; }
    public void setTransform  (@Nullable Transform _transform  ) { transform   = _transform;   }
    public void setBackground (@Nullable Vector4i  _background ) { background  = _background;  }
    public void setOpacity    (@Nullable Integer   _opacity    ) { opacity     = _opacity;     }

    // Getters
    public @Nullable Transform getTransformFg() { return transformFg; }
    public @Nullable Transform getTransformBg() { return transformBg; }
    public @Nullable Transform getTransform  () { return transform;   }
    public @Nullable Vector4i  getBackground () { return background;  }
    public @Nullable Integer   getOpacity    () { return opacity;     }
}
