
Tomato is a persistence library designed for ease of use, power, flexibility.

It differs from Spring JPA in that it does not need static entity classes, repositories and other artifacts, and it does not require configuration.
In Tomato, there is only one Entity class, and it provides the ability to read and write from any table in any schema.  It also provides collection support, and data hierarchy support.

To use Tomato...
See the EntityTest.java file for usage examples.  It includes saving, loading, nested loads, nested writes, etc.
The most important thing to know is that Tomato honors the values in the __variables.  It uses __dirty to determine if it should write a record.  
It uses __persisted to determine if a record should be INSERTed or UPDATEd.

To use it you must modify the EntityDataSource class to provide a DataSource for your database.  In future versions, EntityDataSource this will become an abstract class that you will need to derive from.

If you want to use the data on a web front end, such as angular, then you should also download the Basil project.  
The Basil project adds a web service for reading and writing JSON data to and from the database via the Entity class.  One web service, any number of tables.

The Basil project provides dynamic angular forms for any entity structure.
Just 3 lines of HTML and you can have a form or table that is fully configurable without coding.