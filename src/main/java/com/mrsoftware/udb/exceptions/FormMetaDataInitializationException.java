package com.mrsoftware.udb.exceptions;

public class FormMetaDataInitializationException extends TomatoException {

    public FormMetaDataInitializationException(Exception e, String name) {
        super(e);

        infoMap.put("name", name);
    }

}
