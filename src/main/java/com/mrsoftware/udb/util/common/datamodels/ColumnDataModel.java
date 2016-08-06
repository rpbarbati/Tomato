package com.mrsoftware.udb.util.common.datamodels;

import com.mrsoftware.udb.Entity;
import com.mrsoftware.udb.config.EntityDataSource;
import com.mrsoftware.udb.meta.ColumnData;
import com.mrsoftware.udb.util.common.MetaDataEditorPanel;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.util.Set;

public class ColumnDataModel extends DefaultTableModel {

    ColumnData fd = null;

    static private ArrayList<String> labels = new ArrayList<String>();
    static private ArrayList<Object> values = new ArrayList<Object>();

    MetaDataEditorPanel panel;
    JTable table = null;

    public ColumnDataModel(MetaDataEditorPanel panel, JTable table) {
        this.panel = panel;
        this.table = table;
    }

    public void setFieldData(ColumnData fd) {
        this.fd = fd;

        labels.clear();
        values.clear();

        Set<String> c = fd.getValues().keySet();

        for (String key : c) {
            labels.add(key);
            values.add(fd.getValue(key));
        }
        
        table.setModel(this);
        
        table.invalidate();

        table.repaint();

        panel.invalidate();
        panel.repaint();
        
        Dimension d = panel.getSize();
        
//        panel.setSize(d.width - 2, d.height - 2);
//        panel.setSize(d.width + 2, d.height + 2);
        
        panel.getParent().invalidate();
    }

    Class[] columnTypes = new Class[]{
        String.class, String.class
    };

    public Class getColumnClass(int columnIndex) {
        return columnTypes[columnIndex];
    }

    boolean[] columnEditables = new boolean[]{
        false, true
    };

    public boolean isCellEditable(int row, int column) {
        return columnEditables[column];
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    public int getRowCount() {

        return labels.size();
    }

    public ColumnData getFieldData() {
        return fd;
    }

    @Override
    public Object getValueAt(int row, int column) {
        if (column == 0) {
            return labels.get(row);
        } else {
            return values.get(row);
        }
    }

//	public DefaultFormSchema getFormSchema()
//	{
//		return dfs;
//	}
//	@Override
    public void setValueAt(Object aValue, int row, int column) {
//		super.setValueAt(aValue, row, column);

        if (column == 0) {
            labels.set(row, (String) aValue);
        } else {
            values.set(row, aValue);
        }

        int[] rows = table.getSelectedRows();

        fd.addValue((String) getValueAt(rows[0], 0), getValueAt(rows[0], 1));

        // Write the row change to fieldData
        Entity fieldData = Entity.createEntityOrView(EntityDataSource.getInstance().getMetaSchema() + ".FormMetaData");

        Entity modifiedEntity = Entity.createEntityOrView(panel.getViewName());

        // Set the values
        fieldData.setValue("schematable", modifiedEntity.getName());        // The base table name 
        fieldData.setValue("columnName", panel.getFieldName());
        fieldData.setValue("viewName", panel.getViewName());                // May be the same as base table or it will be view name

        fieldData.setValue("label", getValueAt(row, 0));

        // Look for a row for updating,  Loads the row (if any) using all of the row values - except id and value
        // This will load the id if the row exists already
        // Were doing this because we don't know the id of the row starting off
        fieldData.qbe();

        try {
            // Delete the row if it exists and the new value is empty
            if (fieldData.isPersisted() && aValue.equals(""))
                fieldData.setDeleted(true);

            else
                // Set the new value
                fieldData.setValue("value", (String) getValueAt(row, 1));

            // This will either delete, insert or update based on dirty, deleted and persisted
            fieldData.save();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
