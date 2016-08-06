package com.mrsoftware.udb.exceptions;

public class EntityLoadException extends UltraDbException {

    public EntityLoadException(Exception e, String name) {
        super(e);

        infoMap.put("name", name);
    }

}
