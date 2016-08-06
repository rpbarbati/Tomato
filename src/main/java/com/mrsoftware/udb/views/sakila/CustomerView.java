package com.mrsoftware.udb.views.sakila;

import com.mrsoftware.udb.View;
import com.mrsoftware.udb.meta.ChildData;

public class CustomerView extends View {

    public CustomerView() {
        super("sakila.customer", "sakila.CustomerView");

        setDeep(true);
    }

    public void initializeChildList() {
        super.initializeChildList();

        // Add an address table record using the address_id as its foreign key
        childList.add(new ChildData("sakila.AddressView", "address_id", false, false, null));
    }
}
