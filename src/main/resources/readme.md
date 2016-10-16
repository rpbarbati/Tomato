
Tomato Entities

Every tomato Entity instance carries its own metadata for tracking its state.  These are the values starting with __.

Currently, the names are meaning are...
"__persisted": if true, the record exists in the database.
"__deleted": set this to true and save in order to delete this record in the database.
"__dirty": true if any of the values have been changed after the record was loaded.  A freshly loaded entity will have dirty false.
"__deep": set to true if the entity has any child entities and you want the child entities to be included in processing saves and loads.
"__set": The engine will set this to true if the entity instance carries a set of records.
"__filter": Set this to a logical expression in order to load a set of matching records.  Can also be used with QBE search in order to refine the selection.
"__model": Setting this to true and submitting a JSON object for processing to the back end will cause the resulting JSON entity to only contain the columns which were 
passed in the request.  In other words, if a table has 20 columns, and I only am interested in 2 of them, then I can submit a request JSON with just the two columns and __model set to true and I 
will only get two columns in the resulting entity or set.  An easy way to retrieve a desired subset of columns.

Saving and loading

To save a Tomato entity, call save() on it.  
To load an entity, set some field values and call load() or qbe().  
load() will only use key fields in the SELECT WHERE clause, qbe() will use all field values which are not null.

Tomato knows the primary keys for all tables automatically.  It will only know a foreign key column when you add a child and specify the foreign key column name.

When saving or loading hierarchies, make sure to set ___deep to true.  Also, save() will only operate on entities where __dirty is true.  Most of the time, __dirty will get set to 
true simply by changing a value in the entity.  

Deleting rows (this is not completed yet)
Set __deleted to true, set __dirty to true then call save().  If the resulting entity has __persisted as false, the record was deleted, otherwise it was not deleted.

See the EntityTest.java file for syntax for loading and saving single entities and hierarchies.  

To create and use Views - for data hierarchies, special handling, etc.

Create a package for the schema which will contain the views you are going to create.  This package must be under the com.mrsoftware.udb.views package.  The project
already has two such packages for examples of creating and using views - check out the classes in them.

To create a view, simply call Entity.createEntityOrView("schema.ViewNameHere").  That's it.

If you have defined child entities for a view, set __deep to true if you want the entity to load and save its children when it is loaded or saved.  This is not like spring where
where you will always have to be aware of the children of a JPA entity.  In Tomato, an entity could have 50 child tables, and when __deep is set to false, 
it basically acts like it doesn't have any.  When __deep is true, it will load and save all 50 (depending on the value of __dirty in each child).


I hope you like using Tomato!

Please feel free to contact me if you run into difficulties.


