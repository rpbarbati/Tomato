package com.mrsoftware.udb.json;

import com.mrsoftware.udb.Entity;
import com.mrsoftware.udb.EntityContainer;

public class EntityParser3 extends JSONParser {

    @Override
    public void addNamedValue(String name, JSONValue value) {
        Entity current = getCurrentContext();

        if (name.startsWith("__")) {
            current.setEntityValue(name, value.getStringValue());
        } else {
            current.setParsedValue(name, value.getStringValue());
        }
    }

    public void onName(String name) {
    }

    @Override
    public void addChildObject(String name, Object object) {
    }

    @Override
    public void addArrayElement(Object object) {

        Entity ec = getCurrentContext();

        ec.add((Entity) object);
    }

    @Override
    public boolean isObject(Object o) {
        return o instanceof Entity;
    }

    @Override
    public boolean isArray(Object o) {

        Entity ec = getCurrentContext();

        return ec.isArray();
    }

    @Override
    public Object onBeginObject(String name) {

        Entity e = getCurrentContext();

        Entity retval = null;

        if ((e == null && name == null) || name == null) {
            retval = new EntityContainer(null);
        } else {
            retval = Entity.createEntityOrView(name);
        }

//		if (e != null)
//			retval.setDeep(e.isDeep());
        return retval;
    }

    public Object onBeginArray(String name) {
        Entity e = getCurrentContext();

        e.isArray(true);

        // Clear the name buffer (which should be __elements)
        super.resetStringBuffer();

        currentName = null;

        return null;
    }

    public boolean onArrayEnd() {
        return false;
    }

    @Override
    public <T> T getResult() {
        Entity e = super.getResult();

        Entity child = e.getChild(0);

        child.setParent(null);

        if (child.isModel()) {
            child.resetFormMetaData();
        }

        return (T) child;
    }

    protected void pushContext(Object o) {
        if (!stack.isEmpty()) {
            Entity current = (Entity) stack.peek();

            ((Entity) o).setParent(current);
        }

        super.pushContext(o);
    }

    public void popContext() {
        if (!stack.isEmpty()) {
            Entity e = (Entity) stack.pop();

            if (!stack.isEmpty()) {
                Entity current = (Entity) stack.peek();

                // Add it to parent
                current.add(e);
            }
        }
    }
}
