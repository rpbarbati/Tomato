package com.mrsoftware.udb.views.sakila;

import com.mrsoftware.udb.View;
import com.mrsoftware.udb.meta.ChildData;

public class StaffRentalsView extends View {

    public StaffRentalsView() {
        super("sakila.staff", "sakila.StaffRentalsView");

        setDeep(true);
    }

    public void initializeChildList() {
        super.initializeChildList();

        // Add an address table record using the address_id as its foreign key
        childList.add(new ChildData("sakila.rental", "staff_id", false, true, "customer_id = 77"));
    }
}
