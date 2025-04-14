package com.snek.fancyplayershops.implementations.ui.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector4i;

import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.ui.styles.ElmStyle;
import com.snek.framework.ui.styles.FancyTextElmStyle;
import com.snek.framework.ui.styles.PanelElmStyle;
import com.snek.framework.utils.Easings;




public class ShopButtonStyle extends FancyTextElmStyle {
    public ShopButtonStyle() {
        super();
    }


    @Override
    public @NotNull Vector4i getDefaultBackground() {
        return new Vector4i(255, 130, 180, 160);
    }
}
