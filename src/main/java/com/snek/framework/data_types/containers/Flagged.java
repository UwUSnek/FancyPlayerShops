package com.snek.framework.data_types.containers;




public class Flagged<T> {
    private T value;
    private boolean flag = true;

    private Flagged(T _value) {
        this.value = _value;
    }
    public static <T> Flagged<T> from(T value) {
        return new Flagged<T>(value);
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
        if(!value.equals(_value)) flag = true;
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


    public boolean isFlagged() {
        return flag;
    }

    public void unflag() {
        flag = false;
    }
}
