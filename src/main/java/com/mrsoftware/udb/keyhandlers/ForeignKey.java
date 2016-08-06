package com.mrsoftware.udb.keyhandlers;

import com.mrsoftware.udb.KeyColumn;
import com.mrsoftware.udb.Entity;

public class ForeignKey extends KeyColumn {

    /**
     * ForeignKey will look up its proper value just before processing a load or
     * a save resulting in an insert.
     *
     * For updates and deletes, ForeignKey will cause the value of the column to
     * not be set into the be removed from the entity It looks it up by calling
     * getValue(name) on the parent of its owner entity.
     *
     * ForeignKey is what allows a hierarchy of entities to load correctly. If
     * it is not set on at least one column in a child entity, that child entity
     * will never load any data - as a child. It could, however, load data if
     * used by itself.
     */
    /**
     * This constructor can only be used for foreign keys that have the same
     * column name in both tables.
     */
    public ForeignKey() {
        super();
    }

    /**
     * Get the value of the foreign key from the parent table
     */
    public void preLoad(Entity instance) {
        if (instance.getParent() != null) {
            // Get the value
            Object o = instance.getParent().getValue(getName());

            instance.setValue(getName(), o);
        }
    }

    public void preInsert(Entity instance) {
        if (instance.getParent() != null) {
            // Get the value
            Object o = instance.getParent().getValue(getName());

            instance.setValue(getName(), o);
        }
    }

}
