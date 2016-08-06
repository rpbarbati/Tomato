/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mrsoftware.udb;

/**
 *
 * @author NewRodney
 */
public class SQLView extends View {
    
    private String sql;
    
    protected SQLView(String viewName, String sql) {
        super("SQLView", viewName);

        this.sql = sql;
    }
    
    public String getLoadSQL()
    {
         return sql;
    }
}

 
