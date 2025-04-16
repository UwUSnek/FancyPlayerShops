package com.snek.framework.data_types.containers;








/**
 * A simple collection of three objects of different types.
 */
public class Triplet<F, S, T> {
    public F first;
    public S second;
    public T third;


    /**
     * Creates a new Triplet.
     * Both elements are set to null.
     */
    public Triplet() {
        first  = null;
        second = null;
        third  = null;
    }


    /**
     * Creates a new Triplet using the specified values.
     * @param _first The first value.
     * @param _second The second value.
     * @param _third The third value.
     */
    public Triplet(F _first, S _second, T _third) {
        first  = _first;
        second = _second;
        third  = _third;
    }


    /**
     * Creates a new Triplet using the specified values.
     * @param _first The first value.
     * @param _second The second value.
     * @param _third The third value.
     * @return The newly created Triplet.
     */
    public static <F, S, T> Triplet<F, S, T> from(F _first, S _second, T _third) {
        return new Triplet<>(_first, _second, _third);
    }
}
