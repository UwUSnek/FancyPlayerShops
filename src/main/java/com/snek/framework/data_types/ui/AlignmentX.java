package com.snek.framework.data_types.ui;




public enum AlignmentX {
    NONE  (-1),
    LEFT  (0),
    CENTER(1),
    RIGHT (2);


    public final int value;
    AlignmentX(int _value) {
        value = _value;
    }
}
