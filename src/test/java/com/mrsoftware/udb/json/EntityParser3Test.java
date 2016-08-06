/*
 * Some basic testing of the JSON parser.
 */
package com.mrsoftware.udb.json;


import com.mrsoftware.udb.Entity;
import java.io.IOException;
import static org.hamcrest.CoreMatchers.containsString;
import org.junit.AfterClass;
import static org.junit.Assert.assertThat;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 *
 * @author rpbarbati
 */

public class EntityParser3Test {

    static Entity e;

    @BeforeClass
    static public void setUp() {

        // code that will be invoked before this test starts
        e = Entity.createEntityOrView("sakila.customer");

        e.setValue("customer_id", 6);

        e.load();

        System.out.println("Entity loaded!");
    }

    @Test
    public void JSONParserTest() throws IOException {

        String s = e.toJSON();

        System.out.println("Original JSON");

        System.out.print(s);

        EntityParser3 parser = new EntityParser3();

        parser.parse(s);

        Entity result = parser.getResult();

        s = result.toJSON();
        
        assert( s.indexOf("\"first_name\": \"JENNIFER\"") > 0);
        
        assertThat(s, containsString("\"last_name\": \"DAVIS\""));

        System.out.println("Parsed JSON");

        System.out.print(s);
    }

    @AfterClass
    static public void cleanUp() {
            // code that will be invoked after this test ends
    }
}
