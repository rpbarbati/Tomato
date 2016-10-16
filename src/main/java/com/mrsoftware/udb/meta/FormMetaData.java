package com.mrsoftware.udb.meta;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import com.mrsoftware.udb.Entity;
import com.mrsoftware.udb.config.EntityDataSource;
import com.mrsoftware.udb.exceptions.FormMetaDataInitializationException;
import com.mrsoftware.udb.KeyColumn;
import com.mrsoftware.udb.SQLView;
import com.mrsoftware.udb.util.CommaSeparatedValues;
import com.mrsoftware.udb.util.KeyedArrayList;
import com.mrsoftware.udb.util.LowerCaseMap;
import java.util.ArrayList;

public class FormMetaData extends DbMetaData {

    private KeyedArrayList<ColumnData> columns = new KeyedArrayList();
    private KeyedArrayList<KeyColumn> keyColumns = new KeyedArrayList();

//	private String tableName;
    public FormMetaData(String viewName) {
        super(MetaType.FormMetaData, viewName);
    }

    public void initialize(Entity instance) throws FormMetaDataInitializationException {
        loadDefaultData(instance);

        loadOverrides(instance);

        loadExtendedData(instance);
    }

    private void loadDefaultData(Entity instance) throws FormMetaDataInitializationException {

        // We want structure, not data
        
        String sql = "";
        
        if (instance instanceof SQLView)
            sql = "SELECT * FROM (" + instance.getLoadSQL() + ") ins WHERE 1=0";
        
        else
            sql = "SELECT " + instance.getColumnSelectClause() + " FROM " + instance.getName() + " WHERE 1=0";

        ResultSet rs = null;

        try (
                Statement s = EntityDataSource.getInstance().getConnection().createStatement();) {
            rs = s.executeQuery(sql);

            ResultSetMetaData rsmd = rs.getMetaData();

            initialize(rsmd);
        } catch (Exception e) {
            throw new FormMetaDataInitializationException(e, instance.getName());
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
            }
        }
    }

    private void loadOverrides(Entity instance) throws FormMetaDataInitializationException {
        // Create a select that returns all of the desired columns but no rows
        // The order by clause is crucial - it ensures that view values always override default values
        // Default values are identified by schemaTable and viewName being the same value (always the actual schema.tableName)
        // View values are identified by schemaTable and viewName being different
        // Default values are applied across all views, 
        // View values override default values 

        // Be aware that virtual columns can be added via this mechanism
        if (EntityDataSource.getInstance().enableOverrides())
        {
            String sql
                    = "SELECT "
                    + "     * "
                    + "FROM "
                    + EntityDataSource.getInstance().getMetaSchema() + ".FormMetaData "
                    + "WHERE "
                    + "     LOWER(schemaTable) = '" + instance.getName().toLowerCase() + "' "
                    + "     AND LOWER(viewName) = '" + instance.getViewName().toLowerCase() + "' "
                    + "ORDER BY "
                    + "     CASE WHEN LOWER(viewName) = LOWER(schemaTable) THEN 0 ELSE 1 END, "
                    + "     columnName";

            ArrayList<LowerCaseMap> rows = EntityDataSource.getInstance().executeQuery(sql);

    //		try (
    //			Connection connection = EntityDataSource.getInstance().getConnection();
    //
    //			Statement s = connection.createStatement();
    //
    //			ResultSet rs = s.executeQuery(sql);
    //		)
            try {

                ColumnData cd = null;

    //			while (rowss.next())
                for (LowerCaseMap row : rows) {
                    String columnName = row.getString("columnName");

                    cd = columns.get(columnName);

                    if (cd == null || !cd.getName().equalsIgnoreCase(columnName)) {
                        if (cd == null) {
                            // Add a virtual ColumnData
                            cd = new ColumnData(columnName);

                            add(cd);
                        }
                    }

                    // Add all the key/values to it
                    cd.addValue((String) row.get("label"), row.get("value"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new FormMetaDataInitializationException(e, instance.getViewName());
            }
        }
        
        // Now that we have all the overrides (or none if not enabled), we can create key columns 
        createKeyColumnList();
    }

    /**
     * Extracts the list of Key Columns from the normal Columns KeyColumns are
     * used for all SQL operations
     *
     * @throws FormMetaDataInitializationException
     */
    private void createKeyColumnList() throws FormMetaDataInitializationException {
        for (ColumnData cd : columns) {
//			ColumnData cd = (ColumnData)o;

            boolean isKey = cd.getBoolean("isKey");

            if (isKey) {
                KeyColumn kc = KeyColumn.createKeyColumn(cd);

                keyColumns.add(kc.getName(), kc);
            }
        }
    }

    public void addKeyColumn(String name, KeyColumn handler) {
        ColumnData cd = columns.get(name);

        handler.setColumnData(cd);

        getKeyColumns().add(name, handler);
    }

    /**
     * Loads data referenced by the columns in this FormMetaData
     *
     * This will typically be value lists, help text or other large values
     */
    private void loadExtendedData(Entity instance) {

    }

    /**
     * This is called from EntityDataSource.executeSelect()
     *
     * @param rsmd
     * @throws FormMetaDataInitializationException
     */
    public void initialize(ResultSetMetaData rsmd) throws FormMetaDataInitializationException {
        try {
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                ColumnData cd = new ColumnData(rsmd, i);

                add(cd);
            }
        } catch (Exception e) {
            throw new FormMetaDataInitializationException(e, getName());
        }
    }

    public void add(ColumnData cd) {
        columns.add(cd.getName(), cd);
    }

    public KeyedArrayList<ColumnData> getColumns() {
        return columns;
    }

    public KeyedArrayList<KeyColumn> getKeyColumns() {
        return keyColumns;
    }

    public String toJSON() {
        CommaSeparatedValues csv = new CommaSeparatedValues(",\n\t");

        for (ColumnData cd : columns) {
            csv.append(cd.toJSON());
        }

        String retval = "{\n\t\"formMetaData\": [\n" + csv.toString() + "]\n\t}\n";

        return retval;
    }

    public ColumnData getColumn(String name) {
        return columns.get(name);
    }

}
