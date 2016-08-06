package com.mrsoftware.udb.json;

public class JSONValue {

    Object value = null;
    boolean quoted = false;

    public JSONValue(Object value, boolean quoted) {
        this.value = value;
        this.quoted = quoted;

        if (this.value == null || (this.value != null & (this.value instanceof String) & this.value.equals("null"))) {
            this.value = null;
        }

        int i = 5;
        i = 354;
    }

    public String getStringValue() {
        return (String) value;
    }

    public String getExportValue() {
        if (value == null) {
            return null;
        } else if (quoted) {
            return "\"" + value + "\"";
        } else {
            return (String) value;
        }
    }
}
