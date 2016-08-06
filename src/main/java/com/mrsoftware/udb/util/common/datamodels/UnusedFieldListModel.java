//package com.mrsoftware.udb.util.common.datamodels;
//
//import com.mrsoftware.udb.Entity;
//import com.mrsoftware.udb.meta.ColumnData;
//import com.mrsoftware.udb.meta.FormMetaData;
//import java.util.ArrayList;
//
//import javax.swing.table.DefaultTableModel;
//
//public class FieldListModel extends DefaultTableModel {
//
//    FormMetaData dfs = null;
//
//    Object[] columnIdentifiers = new Object[]{
//        "Selected", "Field"
//    };
//
//    Class[] columnTypes = new Class[]{
//        Boolean.class, String.class
//    };
//
//    private ArrayList<Boolean> select = new ArrayList<Boolean>();
//
//    /**
//     * Construct for a new view
//     *
//     * @param dfs
//     */
//    public FieldListModel(FormMetaData dfs) {
//        this.dfs = dfs;
//
//        setColumnCount(2);
//
//        int row = 0;
//
//        for (ColumnData fd : dfs.getColumns()) {
//            Object[] values = new Object[]{new Boolean(true), fd.getName()};
//
//            insertRow(row++, values);
//
//            select.add(new Boolean(true));
//        }
//    }
//
//    /**
//     * Construct for an existing view
//     *
//     * @param v
//     */
//    public FieldListModel(Entity v) {
//        setColumnCount(2);
//
//        int row = 0;
//
//        FormMetaData dfs = v.getFormMetaData();
//
//        String selectedColumns = v.getColumnList().toString();
//
//        for (ColumnData fd : dfs.getColumns()) {
//            String name = fd.getName();
//            Boolean selected = selectedColumns.contains(name);
//
//            Object[] values = new Object[]{selected, name};
//
//            insertRow(row++, values);
//        }
//    }
//
//    @Override
//    public boolean isCellEditable(int row, int column) {
//        String fieldName = (String) getValueAt(row, 1);
//
//        ColumnData fd = dfs.getColumn(fieldName);
//
//        return (!fd.getBoolean("isKey") & column == 0);
//    }
//
//    public Class getColumnClass(int columnIndex) {
//        return columnTypes[columnIndex];
//    }
//}
