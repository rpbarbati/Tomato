package com.mrsoftware.udb.exceptions;

import java.util.HashMap;
import java.util.Map;

public class UltraDbException extends RuntimeException {

    protected Map<String, String> infoMap = new HashMap<>();

    public UltraDbException(Exception e) {
        super(e);
    }
    
    public UltraDbException(Exception cause, String ... msgs)
    {
        this(cause);
        
        for (String msg : msgs)
            infoMap.put("Information", msg);
    }

    public UltraDbException addInfo(String name, String value) {
        infoMap.put(name, value);

        return this;
    }

}
