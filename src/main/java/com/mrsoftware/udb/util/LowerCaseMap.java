package com.mrsoftware.udb.util;

import java.util.HashMap;

public class LowerCaseMap<T> extends HashMap<String, T> {

    public boolean containsKey(String key) {
        return super.containsKey(key.toLowerCase());
    }

    public T get(String key) {
        return (T) super.get(key.toLowerCase());
    }

    public <R> R getType(String key) {
        return (R) super.get(key.toLowerCase());
    }

    public T put(String key, T value) {
        return (T) super.put(key.toLowerCase(), value);
    }

    public T remove(String key) {
        return super.remove(key.toLowerCase());
    }

    public String getString(String key) {
        Object o = get(key);

        if (o == null) {
            return "null";
        } else if (o instanceof String) {
            return (String) o;
        } //			return "\"" + (String)o + "\"";
        else {
            return o.toString();
        }
    }
}
