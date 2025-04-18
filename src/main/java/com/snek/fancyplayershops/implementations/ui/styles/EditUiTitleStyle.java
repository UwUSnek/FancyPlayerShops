package com.snek.fancyplayershops.implementations.ui.styles;

import org.joml.Vector4i;

import com.snek.framework.ui.styles.FancyTextElmStyle;








/**
 * The stlye of the EditUiTitle element.
 */
public class EditUiTitleStyle extends FancyTextElmStyle {

    /**
     * Creates a new default EditUiTitleStyle.
     */
    public EditUiTitleStyle() {
        super();
    }


    @Override
    public Vector4i getDefaultBackground(){
        return new Vector4i(255, 38, 38, 40);
    }
}
