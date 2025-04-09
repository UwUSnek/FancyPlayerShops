package com.snek.framework.data_types.ui;




public enum AlignmentY {
    NONE  (-1),
    TOP   (0),
    CENTER(1),
    BOTTOM(2);


    public final int value;
    AlignmentY(int _value) {
        value = _value;
    }
}
