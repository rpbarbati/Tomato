package com.mrsoftware.udb.exceptions;

public class EntityInitializationException extends TomatoException {

    public EntityInitializationException(Exception e, String name) {
        super(e);

        infoMap.put("name", name);
    }

}
