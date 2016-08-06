/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mrsoftware.udb.json;

import com.mrsoftware.udb.exceptions.UltraDbException;

/**
 *
 * @author NewRodney
 */
public class JSONParserException extends UltraDbException {
   
    public JSONParserException(Exception cause, String ... msgs)
    {
        super(cause, msgs);
    }
}
