package com.snek.framework.data_types.containers;








/**
 * A simple collection of two values of different types.
 */
public class Pair<F, S> {
    public F first;
    public S second;


    /**
     * Creates a new Pair.
     * Both elements are set to null.
     */
    public Pair() {
        first  = null;
        second = null;
    }


    /**
     * Creates a new Pair using the specified values.
     * @param _first The first value.
     * @param _second The second value.
     */
    public Pair(F _first, S _second) {
        first  = _first;
        second = _second;
    }


    /**
     * Creates a new Pair using the specified values.
     * @param _first The first value.
     * @param _second The second value.
     * @return The newly created Pair.
     */
    public static <K, V> Pair<K, V> from(K first, V second) {
        return new Pair<>(first, second);
    }
}
