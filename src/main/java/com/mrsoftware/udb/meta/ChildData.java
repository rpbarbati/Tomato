package com.mrsoftware.udb.meta;

import com.mrsoftware.udb.Entity;
import com.mrsoftware.udb.exceptions.EntityLoadException;
import com.mrsoftware.udb.exceptions.TomatoException;
import com.mrsoftware.udb.keyhandlers.ForeignKey;

public class ChildData {

    private String name;
    private boolean deep;
    private boolean set;
    private String foreignKeyColumnName;
    private String filter;

    public ChildData(String name, String foreignKey, boolean deep, boolean makeSet) {
        this.name = name;
        this.deep = deep;
        this.set = makeSet;
        this.foreignKeyColumnName = foreignKey;
    }

    public ChildData(String name, String foreignKey, boolean deep, boolean makeSet, String filter) {
        this(name, foreignKey, deep, makeSet);
        this.filter = filter;
    }

    public void load(Entity entity) throws EntityLoadException {
        try {
            // Create the entity for this ChildData
            Entity child = Entity.createEntityOrView(name);

            child.setDeep(deep);

            child.isArray(set);

            child.setFilter(filter);

            // Add the foreignKey handler to child entity
            child.getFormMetaData().addKeyColumn(foreignKeyColumnName, new ForeignKey());

            // Now attempt to load the entity
            // First set its parent so ForeignKey can update correctly
            // But we do not add it to parent because we do not know if it has any data yet
            child.setParent(entity);

            child.load();

            if (child.isPersisted()) {
                entity.add(child);
            }
        } catch (TomatoException e) {
            throw new EntityLoadException(e, entity.getName());
        }
    }

}
