package com.mrsoftware.udb.meta;

import com.mrsoftware.udb.util.CommaSeparatedValues;
import com.mrsoftware.udb.util.LowerCaseMap;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ColumnData {

    LowerCaseMap<Object> values = new LowerCaseMap();

    static private DateFormat dateTimeFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
    static private DateFormat dateOnlyFormat = new SimpleDateFormat("MM-dd-yyyy");

//	static private DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	static private DateFormat dateOnlyFormat = new SimpleDateFormat("yyyy-MM-dd");
//	static private DateFormat formalDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    private String name;

    public ColumnData(ResultSetMetaData rsmd, int index) throws SQLException {
        name = rsmd.getColumnName(index);

        addValue("isKey", rsmd.isAutoIncrement(index));

        String javaClass = rsmd.getColumnClassName(index);
        javaClass = javaClass.substring(javaClass.lastIndexOf(".") + 1);

        addValue("javaClass", javaClass);

        addTypedValues(javaClass);

        addValue("displaySize", rsmd.getColumnDisplaySize(index));
        addValue("dbType", rsmd.getColumnTypeName(index));

        addValue("name", rsmd.getColumnName(index));

        String label = rsmd.getColumnLabel(index);

        addValue("label", label.replaceAll("_", " "));

        addValue("isNullable", (rsmd.isNullable(index) == ResultSetMetaData.columnNullable));
        addValue("isSigned", rsmd.isSigned(index));
        addValue("decimals", rsmd.getScale(index));
        addValue("width", rsmd.getPrecision(index));

        // Add custom values based on above
        if (getValue("isKey")) {
            addValue("KeyHandler", "DbAutoIncremented");
        }
    }

    public ColumnData(String name) {
        this.name = name;
    }

    public void addValue(String key, Object value) {
        values.put(key, value);
    }

    public <T> T getValue(String key) {
        return (T) values.get(key);
    }

    public String getName() {
        return name;
    }

    public Boolean getBoolean(String name) {
        boolean retval = false;

        Object o = getValue("isKey");

        if (o instanceof Boolean) {
            retval = (Boolean) o;
        } else if (o instanceof String) {
            retval = Boolean.valueOf((String) o);
        }

        return retval;
    }

    public LowerCaseMap getValues() {
        return values;
    }

    public String toJSON() {
        CommaSeparatedValues csv = new CommaSeparatedValues(",\n\t");

        for (String key : values.keySet()) {
            Object o = getValue(key);

            Object value = o;

            if (o instanceof String) {
                value = "\"" + o + "\"";
            }

            csv.append("\"" + key + "\": " + value);
        }

        String retval = "{\n\t" + csv.toString() + "\n}";

        return retval;
    }

    private void addTypedValues(String javaClass) {
        switch (javaClass.toLowerCase()) {
            case "int":
            case "integer":
            case "long":
            case "float":
            case "double":
            case "short":
                addValue("input", "number");
                break;

            case "char":
            case "character":
            case "string":
                addValue("input", "text");
                break;

            case "date":
                addValue("input", "date");
                break;

            case "datetime":
            case "timestamp":
                addValue("input", "datetime-local");
                break;

            case "boolean":
                addValue("input", "checkbox");
                break;

            default:
                addValue("input", "text");
                break;

        }
    }

    /**
     * This is called by the Entity Parser to get the correct type of value from
     * a string
     *
     * @param instance
     * @param name
     * @param value
     */
    public Object getTypedValue(String value) {
        String javaClass = getValue("javaClass");

        try {

            switch (javaClass.toLowerCase()) {
                case "int":
                case "integer":
                    return Integer.valueOf(value);

                case "long":
                    return Long.valueOf(value);

                case "char":
                case "character":
                case "string":
                    return value;

                case "float":
                    return Float.valueOf(value);

                case "double":
                    return Double.valueOf(value);

                case "date":		// Dates do not have time and are always formatted as YYYY-MM-DD
                    return dateOnlyFormat.parse(value);

                case "short":
                    return Short.valueOf(value);

                case "timestamp":  // Both must have date and time and are always formatted as YYYY-MM-DDTHH:MM:SS:nn
                case "datetime":
                    return dateTimeFormat.parse(value);

                case "boolean":
                    return Boolean.valueOf(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /* This is called to get the export string form of a given value
     */
    public String getStringValue(Object value) {
        String javaClass = getValue("javaClass");

        if (value == null) {
            return "null";
        } else {
            switch (javaClass.toLowerCase()) {
                case "int":
                case "integer":
                    return String.valueOf(value);

                case "long":
                    return String.valueOf(value);

                case "char":
                case "character":
                case "string":
                    return "\"" + (String) value + "\"";

                case "float":
                    return String.valueOf(value);

                case "double":
                    return String.valueOf(value);

                case "date":
                    return "\"" + dateOnlyFormat.format(value) + "\"";

                case "timestamp":
                case "datetime":
                    return "\"" + dateTimeFormat.format(value) + "\"";

                case "short":
                    return String.valueOf(value);

                case "boolean":
                    return Boolean.toString((Boolean) value);

                default:
                    return "" + value;
            }
        }
    }

    private String getDateString(Object date) {
        String className = date.getClass().getSimpleName();

        switch (className) {
            case "Date":
            case "Datetime":
            case "Timestamp":

        };

        return null;
    }
}
