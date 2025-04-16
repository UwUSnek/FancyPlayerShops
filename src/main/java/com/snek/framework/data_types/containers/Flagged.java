package com.snek.framework.data_types.containers;

import java.util.Objects;








/**
 * A wrapper class that can track value changes of the contained object.
 */
public class Flagged<T> {
    private T value;
    private boolean flag = true;


    /**
     * Creates a new Flagged value.
     * @param _value The initial value.
     */
    private Flagged(T _value) {
        this.value = _value;
    }

    /**
     * Creates a new Flagged value.
     * @param value The initial value.
     * @return The newly created Flagged object.
     */
    public static <T> Flagged<T> from(T value) {
        return new Flagged<>(value);
    }




    /**
     * Returns the current value without flagging the object.
     * @return The value.
     */
    public T get() {
        return value;
    }


    /**
     * Sets a new value.
     * Flags the object if !this.get().equals(_value).
     * @param _value The new value.
     */
    public void set(T _value) {
        if(!Objects.equals(value, _value)) flag = true;
        value = _value;
    }


    /**
     * Flags the object and returns a reference to its value.
     * In case of immutable types, a copy is returned.
     * This method always flags the object without checking for changes.
     * @return The object.
     */
    public T edit() {
        flag = true;
        return value;
    }




    /**
     * Returns the current value of the flag.
     * @return The flag.
     */
    public boolean isFlagged() {
        return flag;
    }


    /**
     * Unflags the Flagged object.
     */
    public void unflag() {
        flag = false;
    }
}
