package com.mrsoftware.udb.exceptions;

public class TomatoServicesException extends TomatoException {

    public TomatoServicesException(Exception e, String ... msgs) {
        super(e, msgs);
    }

}
