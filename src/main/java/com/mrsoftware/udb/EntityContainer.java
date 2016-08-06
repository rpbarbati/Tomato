package com.mrsoftware.udb;

import java.io.IOException;

public class EntityContainer extends Entity {

    public EntityContainer() {
        super("EntityContainer");
    }

    public EntityContainer(String name) {
        super(name);
    }

    protected void toJSONInternal() throws IOException {
        if (getChildren().size() > 0) {
            ((Entity) getChild(0)).toJSONInternal();
        }
    }
}
