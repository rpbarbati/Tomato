package com.mrsoftware.udb.json;

import java.io.OutputStream;
import java.io.PrintStream;

public class FormattedPrintStream extends PrintStream {

    private int indent = 0;

    // Max indent depth is 20
    private String tabs = "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t";

    public FormattedPrintStream(OutputStream out) {
        super(out);
    }

    public void indent() {
        indent++;
    }

    public void outdent() {
        indent--;
    }

    public String getIndent() {
        return tabs.substring(0, indent);
    }

    public void print(Object o) {
        super.print(getIndent() + ((o instanceof String) ? o : (String) o));
    }

    public void println(Object o) {
        super.print(tabs.substring(0, indent) + ((o instanceof String) ? o : (String) o));
        super.print("\n");
    }

    public void printObjectOpen() {
        this.print(getIndent() + "{\n");
    }

    public void printObjectName() {
        this.print(getIndent() + "{\n");
    }

    public void printName(String name) {
        this.print("\"" + name + "\": ");
    }

    public void printValue(String name, String value) {
        this.print("\"" + name + "\": " + value);
    }
}
