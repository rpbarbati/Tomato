/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mrsoftware.udb.json;

import com.mrsoftware.udb.exceptions.TomatoException;

/**
 *
 * @author NewRodney
 */
public class JSONParserException extends TomatoException {
   
    public JSONParserException(Exception cause, String ... msgs)
    {
        super(cause, msgs);
    }
}
