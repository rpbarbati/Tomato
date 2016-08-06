package com.mrsoftware.udb.keyhandlers;

import com.mrsoftware.udb.KeyColumn;
import com.mrsoftware.udb.Entity;

public class DbAutoIncremented extends KeyColumn {

    public DbAutoIncremented() {
    }

    public void preInsert(Entity instance) {
        // Clear value before inserts
        instance.setValue(columnData.getName(), null);
    }

}
