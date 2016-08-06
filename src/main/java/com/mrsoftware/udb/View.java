package com.mrsoftware.udb;

import com.mrsoftware.udb.exceptions.EntityInitializationException;
import com.mrsoftware.udb.exceptions.ViewCreationException;

public class View extends Entity {
    
    private static final String package_prefix = "com.mrsoftware.udb.views.";

    public String viewName = null;
    
    
    /* Remember, you can override all of the following in a derived view...
   
    -- Customize the handling of specific values
    getValue();
    setValue();
   
    // Handle event notifications - be sure to call base class method
    preInsert();
    preUpdate();
    preDelete();
    preLoad();
    
    -- customize the entire save and delete process
    doUpdate();
    doDelete();
    doInsert();
   
    */
    

    protected View(String name, String viewName) {
        super(name);

        this.viewName = viewName;
    }

    static public View create(String name) throws ViewCreationException, EntityInitializationException {
        return create(name, true);
    }

    static public View create(String name, boolean useCache) throws ViewCreationException, EntityInitializationException {
        Object o;

        try {

            Class c = Class.forName(package_prefix + name);

            o = c.newInstance();

            ((View) o).initialize(useCache);

        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new ViewCreationException(e, name);
        }

        return (View) o;
    }

    public String getViewName() {

        return viewName;
    }
}
