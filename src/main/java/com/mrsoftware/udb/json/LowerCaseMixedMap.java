package com.mrsoftware.udb.json;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

public class LowerCaseMixedMap implements Serializable {

    HashMap<String, Object> values = new HashMap<String, Object>();

    public <T> T get(String key) {
        return (T) values.get(key.toLowerCase());
    }

    public Object put(String key, Object value) {

        values.put(key.toLowerCase(), value);

        return this;
    }

    public Set<String> keySet() {
        return values.keySet();
    }

    public void remove(String key) {
        values.remove(key.toLowerCase());
    }

    public void clear() {
        values.clear();
    }
}
