package com.mrsoftware.udb;

import com.mrsoftware.udb.keyhandlers.ForeignKey;
import com.mrsoftware.udb.meta.ChildData;
import java.io.IOException;
import java.util.Date;
import org.junit.Test;


/* 
    The tests in this file depend on specific data being in the database.

    In order to run them successfully, you will need to have MySQL installed, 
    including the Sakila video rental demo database. 
*/



//@TestSuite
public class EntityTest {

    static Integer address_id = 0;
    static Integer customer_id = 0;

    @Test
    public void testEntityLoad() {
        Entity e = Entity.createEntityOrView("sakila.customer");

        e.setValue("customer_id", 6);

        e.load();

        assert (e.getValue("first_name").equals("JENNIFER"));
    }

    @Test
    public void testEntityInsert() throws Exception {
        Entity e = Entity.createEntityOrView("sakila.customer");

        e.setValue("first_name", "Rodney");
        e.setValue("last_name", "Barbati");

        e.setValue("store_id", 1);
        e.setValue("address_id", 1);
        e.setValue("active", false);

        e.setValue("create_date", new Date());

        assert (!e.isPersisted());

        e.save();

        assert (e.isPersisted());
    }

    @Test
    public void testNestedInsert() throws Exception {
        // Write a new address and customer records

        // Address must be written first, so it is parent
        Entity e = Entity.createEntityOrView("sakila.address");

        e.setValue("address", "7201 Spring Morning Lane");
        e.setValue("city_id", 111);
        e.setValue("postal_code", 28227);
        e.setValue("district", "TEST");
        e.setValue("phone", "8018033638");
        e.setValue("create_date", new Date());

        // Add a customer record as child to address
        Entity c = Entity.createEntityOrView("sakila.customer");

        c.setValue("first_name", "Rodney");
        c.setValue("last_name", "Barbati");

        c.setValue("store_id", 1);
        c.setValue("active", false);

        // Set the foreign key handler in customer (so it will get its value from the address record after it is inserted)
        c.getFormMetaData().addKeyColumn("address_id", new ForeignKey());

        e.add(c);

        e.save();

        assert (e.isPersisted());

        address_id = e.getValue("address_id");

        // Ensure the customer record was written by reading its key
        Entity customer = e.getChild("sakila.customer");

        assert (customer.getValue("customer_id") != null);

        customer_id = customer.getValue("customer_id");
    }

    @Test
    public void testNestedLoad() throws Exception {
        
        testNestedInsert();
        
        // Add a customer record as child to address
        Entity c = Entity.createEntityOrView("sakila.customer");

        c.setValue("customer_id", customer_id);

        c.getChildList().add(new ChildData("sakila.address", "address_id", false, false));

        c.setDeep(true);

        c.load();

        // Ensure the customer record was written by reading its key
        Entity address = c.getChild("sakila.address");

        Integer pId = address.getValue("address_id");

        assert (address_id.intValue() == pId.intValue());
    }

    @Test
    public void testNestedUpdate() throws Exception {
        Entity c = Entity.createEntityOrView("sakila.customer");

        c.setValue("customer_id", customer_id);

        c.getChildList().add(new ChildData("sakila.address", "address_id", false, false));

        c.setDeep(true);

        c.load();

        // Now update the address2 field in the child entity address
        Entity address = c.getChild("sakila.address");

        address.setValue("Address2", "PO BOX 671164");

        address.setDirty(true);

        // Save the customer (and address update)
        c.save();

        // Load just the address to check if updated
        Entity a = Entity.createEntityOrView("sakila.address");

        a.setValue("address_id", address_id);

        a.load();

        String address2 = a.getValue("address2");

        assert (address2.equals("PO BOX 671164"));

    }

    @Test
    public void testSetDelete() throws Exception {
        
//        String sql = "SELECT * FROM address JOIN customer ON customer.address_id = address.address_id WHERE first_name = 'Rodney' AND last_name = 'Barbati'";
//        
//        SQLEntity se = new SQLEntity("RodneyAddresses", sql);
        
        Entity c = Entity.createEntityOrView("sakila.customer", false);

        c.setFilter("first_name = 'Rodney' AND last_name = 'Barbati'");

        
        
        c.getChildList().clear();
                
        c.getChildList().add(new ChildData("sakila.address", "address_id", false, false));

        c.setDeep(true);

        c.load();       // Load customer rows with embedded addresses
        
        c.setDeleted(true); // Prepare to delete entire set and children (all customer records in this set and any related address records)
        
        c.save();       // Do the delete
        
        // Verify
        
        c.load();
        
        // Nothing should have loaded
        
        assert(!c.isPersisted());
        
        Entity address = Entity.createEntityOrView("sakila.address");

        address.setValue("address_id", address_id);

        address.load();

        assert (!address.isPersisted());
    }
    
    @Test
    public void testSQLView() throws IOException
    {
        String sql = "SELECT * FROM sakila.film f WHERE f.film_id < 10";
        
        SQLView sv = new SQLView("filmInventory", sql);
        
        sv.initialize(false);
        
        sv.load();
        
        assert(sv.getChildCount() > 2);   
        
        String s = sv.toJSON();
    }
}
