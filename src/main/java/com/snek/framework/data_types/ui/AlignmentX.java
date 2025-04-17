package com.snek.framework.data_types.ui;




/**
 * An enum that defines the horizontal alignment of a UI element.
 *     NONE:    The element is not horizontally aligned and can move freely.
 *     LEFT:    The left edge  of the element is always aligned with the left edge  of its parent.
 *     CENTER:  The center     of the element is always aligned with the center     of its parent.
 *     RIGHT:   The right edge of the element is always aligned with the right edge of its parent.
 */
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
