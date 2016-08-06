package com.mrsoftware.udb.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class KeyedArrayList<T> implements Iterable<T> {

    private ArrayList<T> objects = new ArrayList<>();
    private LowerCaseMap<Integer> map = new LowerCaseMap();

    private boolean isArray = false;		// If true, this object will add as an array

    /**
     * KeyedArrayList is a combination Map and List collection.
     *
     * It stores all objects in a list, and stores the index of the object in a
     * map under the corresponding key.
     *
     * This allows keyed access directly to the correct element in the list, as
     * well as maintaining original order. Duplicate keys are supported in that
     * it won't cause an exception However, adding an object with a duplicate
     * key will remove the original object from the list
     */
    public KeyedArrayList() {
    }

    public void add(T object) {
        if (isArray) {
            objects.add(object);
        } else {
            add("" + objects.size(), object);
        }
    }

    /**
     * This method will overwrite any existing object using the same key
     *
     * @param key
     * @param object
     */
    public void add(String key, T object) {
        if (isArray) {
            objects.add(object);
        } else {
            // Check to see if we already have that key
            Integer index = map.get(key);

            if (index == null) {
                // Nope, so add it to objects
                objects.add(object);

                // And save the index in the map
                map.put(key, objects.size() - 1);
            } else {
                // Replace the object at index
                objects.set(index, object);

            }
        }
    }

    public T get(String key) {
        if (!isArray) {
            Integer index = map.get(key);

            if (index != null && index < objects.size()) {
                return objects.get(index);
            }
        } else {
            return objects.get(Integer.valueOf(key));
        }

        return null;
    }

    public <R> R getType(String key) {
        return (R) get(key);
    }

    public List<T> getList() {
        return objects;
    }

    @Override
    public Iterator<T> iterator() {
        return objects.iterator();
    }

    public ArrayList<T> getArray() {
        return objects;
    }

    public void isArray(boolean set) {
        this.isArray = set;
    }

    public boolean isArray() {
        return isArray;
    }

    public <R> R remove(String name) {
        // find element
        Integer o = map.get(name);

        if (o != null) {

            // Remove name
            map.remove(name);
        
            // remove and return object
            return (R) objects.remove(o.intValue());
        }

        return null;
    }
}
