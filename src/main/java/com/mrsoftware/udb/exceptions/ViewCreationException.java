package com.mrsoftware.udb.exceptions;

public class ViewCreationException extends TomatoException {

    public ViewCreationException(Exception e, String name) {
        super(e);

        infoMap.put("name", name);
    }

}
