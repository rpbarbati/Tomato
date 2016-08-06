package com.mrsoftware.udb.util.common;

public class Pair<K, V> {

    K key;
    V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public String toJSON() {
        return "\"" + key + "\": \"" + value + "\"";
    }

}
