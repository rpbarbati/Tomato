package com.mrsoftware.udb.util;

public class DelimitedValueBuilder {

    StringBuilder sb = new StringBuilder();

    String delimiter = null;

    public DelimitedValueBuilder(String delimiter) {
        this.delimiter = delimiter;
    }

    boolean beenHere = false;

    public void append(String... values) {
        for (String v : values) {
            if (beenHere) {
                sb.append(delimiter);
            }

            sb.append(v);

            beenHere = true;
        }
    }

    public String toString() {
        return sb.toString();
    }
}
