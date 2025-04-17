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
    private @Nullable Vector4i background;
    private @Nullable Integer opacity;


    /**
     * Creates a new InterpolatedData.
     * @param _transform The transform.
     * @param _background The background color.
     * @param _opacity The foreground text opacity.
     */
    public InterpolatedData(@Nullable Transform _transform, @Nullable Vector4i _background, @Nullable Integer _opacity) {
        transform = _transform;
        background = _background;
        opacity = _opacity;
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
        if(s.d.hasBackground() && hasBackground()) {
            background.set(Utils.interpolateARGB(background, s.d.getBackground(), s.getFactor()));
        }
        if(s.d.hasOpacity() && hasOpacity()) {
            //! Integer doesnt need initialization
            opacity = Utils.interpolateI(opacity, s.d.getOpacity(), s.getFactor());
        }
    }



    // Checks
    public boolean hasTransform () { return transform  != null; }
    public boolean hasBackground() { return background != null; }
    public boolean hasOpacity   () { return opacity    != null; }

    // Setters
    public void setTransform (@Nullable Transform _transform ) { transform  = _transform;  }
    public void setBackground(@Nullable Vector4i  _background) { background = _background; }
    public void setOpacity   (@Nullable Integer   _opacity   ) { opacity    = _opacity;    }

    // Getters
    public @Nullable Transform getTransform () { return transform;  }
    public @Nullable Vector4i  getBackground() { return background; }
    public @Nullable Integer   getOpacity   () { return opacity;    }
}
