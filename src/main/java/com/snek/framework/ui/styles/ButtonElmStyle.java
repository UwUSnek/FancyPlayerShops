package com.snek.framework.ui.styles;

import org.joml.Vector3i;

import com.snek.framework.data_types.Flagged;




public class ButtonElmStyle extends TextElmStyle {
    Flagged<Vector3i> hoverColor = Flagged.from(new Vector3i(255, 255, 255));

    
}
