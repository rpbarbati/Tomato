//package com.mrsoftware.udb.util.common;
//
//public class CommaSeparatedValues {
//
//    StringBuilder sb = new StringBuilder();
//    boolean hasData = false;
//
//    /**
//     * A helper class for constructing comma separated value strings (like JSON
//     * or string lists)
//     */
//    public CommaSeparatedValues() {
//    }
//
//    // Each call (except the first will prefix with a comma)
//    public CommaSeparatedValues append(String value) {
//        if (hasData) {
//            sb.append(", ");
//        }
//
//        sb.append(value);
//
//        hasData = true;
//
//        return this;
//    }
//
//    public String toString() {
//        return sb.toString();
//    }
//
//}
