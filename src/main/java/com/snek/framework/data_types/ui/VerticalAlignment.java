package com.snek.framework.data_types.ui;




public enum VerticalAlignment {
    NONE  (-1),
    TOP   (0),
    CENTER(1),
    BOTTOM(2);


    public final int value;
    VerticalAlignment(int _value) {
        value = _value;
    }
}
