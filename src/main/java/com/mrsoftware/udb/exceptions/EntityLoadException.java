package com.mrsoftware.udb.exceptions;

public class EntityLoadException extends TomatoException {

    public EntityLoadException(Exception e, String name) {
        super(e);

        infoMap.put("name", name);
    }

}
