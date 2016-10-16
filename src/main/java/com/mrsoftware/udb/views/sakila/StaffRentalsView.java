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

        // Add the rental table as a child set (using a condition)
        childList.add(new ChildData("sakila.rental", "staff_id", false, true, "customer_id = 77"));
    }
}
