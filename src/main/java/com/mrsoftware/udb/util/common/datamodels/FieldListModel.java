package com.mrsoftware.udb.util.common.datamodels;

import com.mrsoftware.udb.Entity;
import com.mrsoftware.udb.meta.ColumnData;
import com.mrsoftware.udb.meta.FormMetaData;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

public class FieldListModel extends DefaultTableModel {

    FormMetaData fmd = null;
    ArrayList<Boolean> selectedFields = new ArrayList<Boolean>();

    public FieldListModel() {

    }

    Class[] columnTypes = new Class[]{
        Boolean.class, String.class
    };

    public Class getColumnClass(int columnIndex) {
        return columnTypes[columnIndex];
    }

    boolean[] columnEditables = new boolean[]{
        true, false
    };

    public boolean isCellEditable(int row, int column) {
        return columnEditables[column];
    }

    public void load(String name) {
        Entity entity = Entity.createEntityOrView(name);

        fmd = entity.getFormMetaData();

        // Add a boolean for each field
        for (ColumnData fd : fmd.getColumns()) {
            selectedFields.add(false);
        }
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    public int getRowCount() {

        return (fmd == null) ? 0 : fmd.getColumns().getList().size();
    }

    @Override
    public Object getValueAt(int row, int column) {
        if (column == 0) {
            return selectedFields.get(row);
        } else {
            return fmd.getColumns().getList().get(row).getName();
        }
    }

    public FormMetaData getFormMetaData() {
        return fmd;
    }
    
    public ColumnData getSelectedColumnData(String selectedField)
    {
        ColumnData cd = fmd.getColumns().get(selectedField);

        return cd;
    }

}
