package com.snek.framework.data_types.containers;




public class Triplet<F, S, T> {
    public final F first;
    public final S second;
    public final T third;


    public Triplet(F _first, S _second, T _third) {
        first  = _first;
        second = _second;
        third  = _third;
    }


    public static <F, S, T> Triplet<F, S, T> from(F first, S second, T third) {
        return new Triplet<>(first, second, third);
    }
}
