package com.mrsoftware.udb.meta;

import java.util.ArrayList;

import com.mrsoftware.udb.util.CommaSeparatedValues;

public class ColumnList extends DbMetaData {

    private ArrayList<String> columnList = new ArrayList<>();
    private String columnNames = "";

    public ColumnList(String viewName) {
        super(DbMetaData.MetaType.ColumnList, viewName);
    }

    /**
     *
     * @param columnNames Comma separated list of column names to include in the
     * owning Entity or View
     *
     * This list is used in the creation of all SQL statements
     *
     */
    public void add(String... columns) {
        for (String c : columns) {
            if (!columnNames.contains(c)) {
                columnList.add(c);
                columnNames += c + ", ";
            }
        }
    }

    public String getColumnSelectClause() {
        String retval = null;

        if (columnList.isEmpty()) {
            retval = " * ";
        } else {
            CommaSeparatedValues csv = new CommaSeparatedValues();

            csv.append(columnList.toArray(new String[]{}));

            retval = csv.toString();
        }

        return retval;
    }

}
