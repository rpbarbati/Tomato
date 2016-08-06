package com.mrsoftware.udb.util.common.datamodels;

import com.mrsoftware.udb.KeyColumn;
import com.mrsoftware.udb.View;
import com.mrsoftware.udb.meta.ColumnData;
import com.mrsoftware.udb.meta.FormMetaData;
import javax.swing.table.DefaultTableModel;

public class KeyFieldHandlersModel extends DefaultTableModel {

    FormMetaData dfs = null;

    Object[] columnIdentifiers = new Object[]{
        "Key Field", "Handler"
    };

    Class[] columnTypes = new Class[]{
        String.class, String.class
    };

    public KeyFieldHandlersModel(FormMetaData dfs) {
        this.dfs = dfs;

        setColumnCount(2);

        int row = 0;

        for (ColumnData fd : dfs.getColumns()) {
            if (fd.getBoolean("isKey")) {
                Object[] values = new Object[]{fd.getName(), ""};

                insertRow(row++, values);
            }
        }
    }

    public KeyFieldHandlersModel(View v) {
        setColumnCount(2);

        int row = 0;

        for (KeyColumn kc : v.getFormMetaData().getKeyColumns()) {
            Object[] values = new Object[]{kc.getName(), kc.getClass().getSimpleName()};

            insertRow(row++, values);
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return (column != 0);
    }

}
