package com.mrsoftware.udb.servlets;

import com.mrsoftware.udb.Entity;
import com.mrsoftware.udb.meta.FormMetaData;

public class FormSchemaServices {

    // Returns the dynamically generated default form schema (no modifications) 
    static public String getFormSchema(String name, boolean forceCreate) {

        Entity e = Entity.createEntityOrView(name);

        FormMetaData dfs = e.getFormMetaData();

        String result = dfs.toJSON();

        return result;
    }
}
