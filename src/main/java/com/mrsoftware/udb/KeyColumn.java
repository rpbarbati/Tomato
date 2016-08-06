package com.mrsoftware.udb;

import com.mrsoftware.udb.exceptions.FormMetaDataInitializationException;
import com.mrsoftware.udb.meta.ColumnData;

public class KeyColumn {
    
    private static final String package_prefix = "com.mrsoftware.udb.keyhandlers.";

    protected ColumnData columnData;

    protected KeyColumn() {
    }

    public void setColumnData(ColumnData columnData) {
        this.columnData = columnData;
    }

    /**
     * @param cd A ColumnData which represents a key column (isKey should be
     * true in all cases, but is not checked)
     * @return An instance of a KeyColumn or derived class
     * @throws FormMetaDataInitializationException
     */
    static public KeyColumn createKeyColumn(ColumnData cd) throws FormMetaDataInitializationException {
        KeyColumn kc = null;
        String className = null;

        try {
            // Create the handler instance
            className = package_prefix + cd.getValue("KeyHandler");

            Class c = Class.forName(className);

            kc = (KeyColumn) c.newInstance();

            kc.setColumnData(cd);
        } catch (Exception e) {
            throw new FormMetaDataInitializationException(e, className);
        }

        return kc;
    }

    public String getName() {
        return columnData.getName();
    }

    public <T> T getValue(Entity instance) {
        return instance.getValue(getName());
    }

    public void preLoad(Entity instance) {
    }

    public void preInsert(Entity instance) {
    }

    public void preUpdate(Entity instance) {
    }

    public void preDelete() {
    }
}
