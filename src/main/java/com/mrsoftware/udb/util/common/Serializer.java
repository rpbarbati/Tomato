//package com.mrsoftware.udb.util.common;
//
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.io.Serializable;
//
//public class Serializer<T extends Serializable> {
//
//    String serializeDirectory = "C:/Users/U403495/workspace/DataDrivenPOC/resource/views/";
//
//    public Serializer(String serializeDirectory) {
//        this.serializeDirectory = serializeDirectory;
//    }
//
//    public void saveToFile(T object, String fileName) {
//        try (
//                FileOutputStream fout = new FileOutputStream(serializeDirectory + fileName + ".ser");
//                ObjectOutputStream oos = new ObjectOutputStream(fout);) {
//            oos.writeObject(object);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public T loadFromFile(String fileName) {
//        T retval = null;
//
//        try (
//                FileInputStream fin = new FileInputStream(serializeDirectory + fileName + ".ser");
//                ObjectInputStream ois = new ObjectInputStream(fin);) {
//            retval = (T) ois.readObject();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return retval;
//    }
//}
