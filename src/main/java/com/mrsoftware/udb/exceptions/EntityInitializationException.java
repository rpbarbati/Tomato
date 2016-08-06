package com.mrsoftware.udb.exceptions;

public class EntityInitializationException extends UltraDbException {

    public EntityInitializationException(Exception e, String name) {
        super(e);

        infoMap.put("name", name);
    }

}
