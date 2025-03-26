package com.snek.framework.data_types;




public class Flagged<T> {
    private T value;
    private boolean flag = true;

    private Flagged(T _value) {
        this.value = _value;
    }
    public static <T> Flagged<T> from(T value) {
        return new Flagged<T>(value);
    }


    public T get() {
        return value;
    }

    public void set(T _value) {
        value = _value;
        flag = true;
    }


    public boolean isFlagged() {
        return flag;
    }

    public void unflag() {
        flag = false;
    }
}
