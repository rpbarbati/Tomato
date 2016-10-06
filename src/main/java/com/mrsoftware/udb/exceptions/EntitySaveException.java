package com.mrsoftware.udb.exceptions;

import com.mrsoftware.udb.Entity;

public class EntitySaveException extends TomatoException {

    public EntitySaveException(Exception e, Entity entity) {
        super(e);

        infoMap.put("name", entity.getName());
        infoMap.put("view", entity.getViewName());
    }

    public EntitySaveException(Exception e, String name) {
        super(e);

        infoMap.put("name", name);
    }

}
