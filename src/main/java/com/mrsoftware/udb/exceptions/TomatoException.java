package com.mrsoftware.udb.exceptions;

import java.util.HashMap;
import java.util.Map;

public class TomatoException extends RuntimeException {

    protected Map<String, String> infoMap = new HashMap<>();

    public TomatoException(Exception e) {
        super(e);
    }
    
    public TomatoException(Exception cause, String ... msgs)
    {
        this(cause);
        
        for (String msg : msgs)
            infoMap.put("Information", msg);
    }

    public TomatoException addInfo(String name, String value) {
        infoMap.put(name, value);

        return this;
    }

}
