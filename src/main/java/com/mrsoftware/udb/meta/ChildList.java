package com.mrsoftware.udb.meta;

import java.util.ArrayList;

import com.mrsoftware.udb.Entity;
import com.mrsoftware.udb.exceptions.EntityLoadException;

public class ChildList extends DbMetaData {

    private ArrayList<ChildData> childList = new ArrayList<>();

    public ChildList(String viewName) {
        super(DbMetaData.MetaType.ChildList, viewName);
    }

    public void add(ChildData cd) {
        childList.add(cd);
    }

    public void load(Entity instance) throws EntityLoadException {
        for (ChildData cd : childList) {
            // Get the entity represented by the ChildData
            cd.load(instance);
        }
    }
    
    public void clear()
    {
        childList.clear();
    }
}
