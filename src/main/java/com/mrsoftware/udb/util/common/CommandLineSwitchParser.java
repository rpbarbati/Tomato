//package com.mrsoftware.udb.util.common;
//
//import java.util.ArrayList;
//
//public class CommandLineSwitchParser {
//
//    ArrayList<String> fileSpecs = new ArrayList<String>();
//    int priority = 0;
//
//    static public void main(String[] args) {
//        CommandLineSwitchParser parser = new CommandLineSwitchParser();
//
//        parser.parseCommandSwitches(args);
//
//        System.out.println("FileSpecs: " + parser.fileSpecs.toString());
//        System.out.println("priority: " + parser.priority);
//    }
//
//    private void parseCommandSwitches(String[] args) {
//        boolean fileSpecListFlag = false;
//        boolean priorityFlag = false;
//
//        for (String arg : args) {
//            switch (arg.toLowerCase()) {
//                case "filespeclist":
//                    fileSpecListFlag = true;
//                    break;
//
//                case "priority":
//                    priorityFlag = true;
//                    break;
//
//                default: {
//                    if (fileSpecListFlag) {
//                        fileSpecs.add(arg);
//                    } else if (priorityFlag) {
//                        priority = Integer.valueOf(arg);
//                    }
//                }
//            }
//        }
//    }
//}
