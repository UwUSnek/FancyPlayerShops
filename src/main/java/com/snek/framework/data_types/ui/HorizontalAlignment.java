package com.snek.framework.data_types.ui;




public enum HorizontalAlignment {
    NONE  (-1),
    LEFT  (0),
    CENTER(1),
    RIGHT (2);


    public final int value;
    HorizontalAlignment(int _value) {
        value = _value;
    }
}
