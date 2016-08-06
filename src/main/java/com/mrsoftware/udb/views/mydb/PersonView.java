package com.mrsoftware.udb.views.mydb;

import com.mrsoftware.udb.View;
import com.mrsoftware.udb.meta.ChildData;

public class PersonView extends View {

    public PersonView() {
        super("mydb.person", "mydb.PersonView");
    }

    public void initializeChildList() {
        super.initializeChildList();

        childList.add(new ChildData("mydb.address", "", false, true, null));
    }
}
