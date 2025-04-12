package com.snek.fancyplayershops.implementations.ui.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector4i;

import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.transitions.TextAdditiveTransition;
import com.snek.framework.data_types.animations.transitions.Transition;
import com.snek.framework.ui.styles.ElmStyle;
import com.snek.framework.ui.styles.PanelElmStyle;
import com.snek.framework.utils.Easings;




public class ShopButtonStyle extends PanelElmStyle {
    public ShopButtonStyle() {
        super();
    }


    @Override
    public @NotNull Vector4i getDefaultColor () {
        return new Vector4i(255, 150, 200, 180);
    }
}
