package com.mrsoftware.udb.json;

public class SuperStringBuilder {

    StringBuilder sb = new StringBuilder();
    boolean isData = false;

    public SuperStringBuilder() {
    }

    public SuperStringBuilder append(String... values) {
        for (String value : values) {
            sb.append(value);
        }

        isData = true;

        return this;
    }

    public String toString() {
        return sb.toString();
    }

    public boolean isData() {
        return isData;
    }

    public void reset() {
        sb = new StringBuilder();
        isData = false;
    }
}
