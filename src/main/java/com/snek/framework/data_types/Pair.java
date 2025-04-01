package com.snek.framework.data_types;




public class Pair<F, S> {
    public final F first;
    public final S second;


    public Pair(F _first, S _second) {
        first  = _first;
        second = _second;
    }


    public static <K, V> Pair<K, V> from(K first, V second) {
        return new Pair<>(first, second);
    }
}
