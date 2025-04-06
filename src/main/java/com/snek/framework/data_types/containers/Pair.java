package com.snek.framework.data_types.containers;




public class Pair<F, S> {
    public F first;
    public S second;


    public Pair() {
        first  = null;
        second = null;
    }
    public Pair(F _first, S _second) {
        first  = _first;
        second = _second;
    }


    public static <K, V> Pair<K, V> from(K first, V second) {
        return new Pair<>(first, second);
    }
}
