package com.mrsoftware.udb.servlets;

import com.mrsoftware.udb.Entity;
import com.mrsoftware.udb.json.EntityParser3;
import java.io.IOException;

public class DynamicEntityServices {

    static public String getEntityInstance(String name, boolean deep, boolean asCollection, String filter) throws IOException {

        String retval = null;

        Entity e = Entity.createEntityOrView(name);

        e.setDeep(deep);

        e.isArray(asCollection);

        e.setFilter(filter);

        String s = e.toJSON();

        return s;
    }

    /**
     *
     * @param name - schema.table
     * @param deep
     * @param filter
     * @return
     */
    static public String loadCollection(String name, boolean deep, String filter) throws IOException {

        Entity e = Entity.createEntityOrView(name);

        e.setDeep(deep);

        e.isArray(true);

        e.setFilter(filter);

        e.load();

        String s = e.toJSON();

        return s;
    }

    static public String loadEntity(String entityJSON) throws IOException {

        EntityParser3 ep = new EntityParser3();

        ep.parse(entityJSON);

        Entity e = ep.getResult();

//		e.initialize();
        e.load();

        String s = e.toJSON();

        return s;
    }

    static public String saveEntity(String entityJSON) throws Exception {

        EntityParser3 ep = new EntityParser3();

        ep.parse(entityJSON);

        Entity e = ep.getResult();

        e.save();

        String s = e.toJSON();

        return s;
    }

}
