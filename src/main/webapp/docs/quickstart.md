Using Basil
================

Before using Basil, ensure that Tomato is working properly.  A utility has been included which will allow you to determine this easily.
After installing both Tomato and Basil, and after modifying the EntityDataSource class so that it provides connections to the desired database, 
load your browser and type the following address in the address bar...

EntityTester/EntityTester.html

The form that loads will allow you to retrieve Tomato entities as JSON into the results area below the fields.  To use, follow these steps...
Angular was loaded correctly if the text 40 and Detail Form appear to the right of the first field.
Type a schema.table name into the TableName field and click the new button.  This should load an empty entity structure for the given table.
To load a set of records, edit the __filter value in the returned JSON (changing it to "1=1" will load all rows, otherwise use something like "field = value").
The filter can be as complex as you desire.
Then click the load button. If your filter specifies more than one row, you will get an entity set back (__set = true) immediately.  You can also load entities by QBE.
Specify the search values in the appropriate fields in the JSON structure and click load.  It will retrieve the matching row.



# Creating forms and tables for entities and entity collections

In your html file, before the closing </body> tag, include the following lines...

    <script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.4.5/angular.js"></script>

    <script src="js/dynamicForms/DynamicForms.js"></script>

    <script src="js/dynamicForms/DynamicDataFactory.js"></script>

    <script src="js/dynamicForms/EntityServices.js"></script>
    <script src="js/dynamicForms/FormMetaDataService.js"></script>

    <script src="js/dynamicForms/DynamicEntityDirective.js"></script>

    <script src="js/dynamicForms/DynamicFormDirective.js"></script>
    <script src="js/dynamicForms/DynamicTableDirective.js"></script>

# To create a form for a table in your database

Paste the following lines into the body of your page.

    <dynamic-entity name="sakila.staff" deep="false" keys="{'staff_id': 1}">

        <dynamic-form name="sakila.staff" path="sakila.staff"></dynamic-form>

    </dynamic-entity>

The dynamic-entity element is used to load data for simple testing.  It acts as a host for an entity or data hierarchy.  
Use the keys attribute to specify a key value which specifies the record to load.
The dynamic-form element renders a form for the data loaded by the dynamic-entity element.
For data hierarchies, the path should specify the path to the desired entity.  For example, to render a form for 
a child record of staff, for example, from the rental table, the path would be "sakila.staff.sakila.rental".  
All entities always use schema.tableOrViewName, whether nested or not.

The path must point to the top of the entity the form is to edit.  
For the above example, the path is sakila.staff.sakila.rental because the data structure would look like the following, and
we want the form to edit the rental entity...

{
    sakila.staff: {
        id: 
        blah:
        sakila.rental: {
            id:
            field:
        }
    }
}


# To create a table for a collection of rows

    <dynamic-collection name="sakila.staff" filter="1=1">

        <dynamic-table name="sakila.staff" path="sakila.staff"></dynamic-table>

    </dynamic-collection>

The dynamic-collection element loads all records from the sakila.staff table (because I have provided a filter that would be true for all rows).
The dynamic-table element renders the rows loaded by the dynamic-collection element. 
The rendered table supports adding, updating, and deleting rows.  You can enable the desired options by passing a controller method to any of the following 
attributes on the dynamic-table element.  Doing so will enable a button for the feature you enabled.

    add=
    update=
    delete=

For ease of use, both the dynamic-entity and dynamic-collection directives support these attributes, so you can actually just pass the appropriate method from those
directives into the table directive.


Customizing Dynamic Forms (without having to write any code)

The EntityDataSource class that you provide contains a method named getMetaSchema() that returns the name of the
schema in your database where it should look for a table named FormMetaData.

By inserting rows in this table, you can customize the handling of any table column in any or all views, as desired.

When an entity is loaded, all of the relevant form meta data is extracted from the ResultSet and is then stored in the Entity instance.
if caching is on, which it is by default, all instances of an entity which have the same name (or view name if a view), will
share the first instance of the form meta data that gets created using that name.  

Basil is designed to allow you to customize the content of that meta data for all instances of a given field, or for a given field
in any specific views you desire.  This customization is performed by adding records to the formMetaData table, and in most instances,
no coding will be required.  

After extracting the dynamic meta data associated with an entity, the contents of the formMetaData table are applied using a simple set of rules.

Any records which are found to apply to the entity in the desired view are applied over the existing dynamic data.  This allows 
you to override default values, as well as add new values which you can use for whatever purpose you like.  These are the rules that Tomato applies to rows in FormMetaData.

1) If schematable and viewname are the same - These are default overrides which will be applied to all instances of the named entity.  
2) If schematable and viewname are different - these rows are applied only for the specifically named view.

Because of the order in which they are applied, you are able to effect all or specific entities as needed.

This mechanism allows you to do many things easily when using Basil.  The most widely desired things you might want to do are...

1) Change the component type for a field globally or only in specific views.
2) Inject customized HTML for a column directly into dynamically generated forms and tables - globally or only in specific views.
3) Add new fields to an entity or view that don't actually exist in the database table.


1) Change the component type associated with a given table column - i.e. from a simple text edit to perhaps a masked edit or even a combo box or checkbox.
And because the mechanism lets you specify views, you can have different information for the same field - ie. a given table field may be 
a text edit in one view and a checkbox in another.

2) CUSTOMIZING a dynamic Basil form!
You can inject any custom HTML, including angular directives or whatever, for any given field, into a dynamically generated form or table, by simply adding a row to the formmetadata
table for the desired table, column and view, and setting the label to "html".  The value for the html label is a URL, from which it 
will load your html.  Basil will sanitize and compile whatever it loads, and insert it into the dynamically generated form or table
when the specified column is being rendered.
This should make just about any customizations possible, and if applied at the table level (ie, table name and view name the same), then 
your customizations will be applied everywhere the field appears, unless it is overridden in a particular view.  In other words, you won't need
to do anything going forward to have your customization applied, even in new views.

There are a multitude of labels that can be specified in the formmetadata table.  Each controls some particular aspect of the rendering of the form or table.
Some labels, if used, will require the use of other labels, just like combobox requires a valuelist.



ComboBoxes

If you change the "type" label of a given field to "combobox", then you must also add the valueList label as well, and set its value to the name of the value list to use for 
the combobox.  No additional code is needed - the field will appear on the desired basil forms or tables as a combobox with the value list set as the available options.
            
            
            
    