package com.mrsoftware.udb.util;

public class CommaSeparatedValues extends DelimitedValueBuilder {

    public CommaSeparatedValues(String separator) {
        super(separator);
    }

    public CommaSeparatedValues() {
        super(", ");
    }
}
