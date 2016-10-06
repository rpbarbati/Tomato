package com.mrsoftware.udb;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mrsoftware.udb.config.EntityDataSource;
import com.mrsoftware.udb.exceptions.EntityInitializationException;
import com.mrsoftware.udb.exceptions.EntityLoadException;
import com.mrsoftware.udb.exceptions.EntitySaveException;
import com.mrsoftware.udb.exceptions.FormMetaDataInitializationException;
import com.mrsoftware.udb.exceptions.TomatoException;
import com.mrsoftware.udb.exceptions.ViewCreationException;
import com.mrsoftware.udb.keyhandlers.ForeignKey;
import com.mrsoftware.udb.meta.ChildList;
import com.mrsoftware.udb.meta.ColumnData;
import com.mrsoftware.udb.meta.ColumnList;
import com.mrsoftware.udb.meta.DbMetaCache;
import com.mrsoftware.udb.meta.FormMetaData;
import com.mrsoftware.udb.util.CommaSeparatedValues;
import com.mrsoftware.udb.util.DelimitedValueBuilder;
import com.mrsoftware.udb.util.KeyedArrayList;
import com.mrsoftware.udb.util.LowerCaseMap;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;

public class Entity implements Iterable<Entity> {

    public static boolean disableCache = false;
    
    private String name;
    protected Entity parent;

    protected ColumnList columnList;
    protected FormMetaData formMetaData;
    protected ChildList childList;

    private LowerCaseMap<Object> columnValues = new LowerCaseMap();

    protected boolean isPersisted = false;		// true if the Entity exists as a row in its table 
    protected boolean isDeleted = false;		// Set this to true then save() the entity to perform a delete
    protected boolean isDirty = false;			// save() and delete() will only operate if this is true
    protected boolean isDeep = false;			// When true, this entity will process child entities for all operations (save, load, delete)
    protected boolean isModel = false;			// If true, an entity parsed from JSON will only contain the columns contained in the JSON.

    private boolean isQBE = false;

    protected KeyedArrayList<Entity> children = new KeyedArrayList();		// For Single row entities, holds child entities, for sets, holds each record in the set

    private String filter = null;				// Used if isArray = true - provides the WHERE clause for set load()s

    public Entity(String name) {
        this.name = name;
    }

    /**
     * This is a factory method for entities and views
     *
     * @param name
     * @return
     * @throws EntityInitializationException
     */
    static public Entity createEntityOrView(String name) {
        return createEntityOrView(name, true);
    }

    /**
     *
     * @param name
     * @param fromCache	if true, the entity will initialize using cached
     * objects, if false, it will create new instances and update the cache with
     * them.
     * @return An Entity or View instance
     * @throws EntityInitializationException
     */
    static public Entity createEntityOrView(String name, boolean useCache) {
        Entity retval = null;

        try {
            // First try to create a view
            retval = View.create(name, useCache);
        } catch (ViewCreationException e) {
            retval = new Entity(name);

            if (retval != null) {
                retval.initialize(useCache);
            }
        }

        return retval;
    }

    /**
     * All of the primary components of an Entity are cached for performance
     * Call with useCache false when an external change has been made (such as a
     * change to the FieldMetaData table)
     *
     * @param useCache - if true, components will be loaded from cache if false,
     * all components are created using new and are then initialized
     * @throws EntityInitializationException
     */
    protected void initialize(boolean useCache) throws EntityInitializationException {
        
        if (disableCache)
            useCache = false;
        
        try {
            columnList = DbMetaCache.getColumnList(this, useCache);

            if (!columnList.isInitialized()) {
                initializeColumnList();
            }

            formMetaData = DbMetaCache.getFormMetaData(this, useCache);

            if (!formMetaData.isInitialized()) {
                initializeFormMetaData();
            }

            childList = DbMetaCache.getChildList(this, useCache);

            if (!childList.isInitialized()) {
                initializeChildList();
            }
        } catch (TomatoException e) {
            throw new EntityInitializationException(e, getName());
        }
    }

    public void resetFormMetaData() {
        formMetaData = DbMetaCache.getFormMetaData(this, false);

        initializeFormMetaData();
    }

    public void makeSet() {
        children.isArray(true);
    }

    @Override
    public Iterator<Entity> iterator() {
        return getChildren().iterator();
    }
    
    

    /**
     *
     * @return a where clause suitable for a PreparedStatement All values are
     * specified as named parameters
     *
     */
    public String getWhereClause() {
        DelimitedValueBuilder dvb = new DelimitedValueBuilder(" AND ");

        if (isQBE) {
            // Build where clause using all columns
            for (ColumnData cd : formMetaData.getColumns()) {
                Object o = getValue(cd.getName());

                if (o != null) {
                    dvb.append(cd.getName() + " = ? ");
                }
            }
        } else {
            // Build where clause using only key columns
            for (KeyColumn kc : formMetaData.getKeyColumns()) {
                Object o = getValue(kc.getName());

                if (o != null) {
                    dvb.append(kc.getName() + " = ? ");
                }
            }
        }

        if (filter != null) {
            dvb.append(filter);
        }

        return dvb.toString();
    }

    public PreparedStatement setParameterValues(PreparedStatement ps) throws SQLException {
        int index = 1;

        if (isQBE) {
            // Build where clause using all columns
            for (ColumnData cd : formMetaData.getColumns()) {
                Object o = getValue(cd.getName());

                if (o != null) {
                    ps.setObject(index++, o);
                }
            }
        } else {
            // Build where clause using only key columns
            for (KeyColumn kc : formMetaData.getKeyColumns()) {
                Object o = getValue(kc.getName());

                if (o != null) {
                    ps.setObject(index++, o);
                }
            }
        }

        return ps;
    }
    
    public String getLoadSQL()
    {
         return "SELECT "
                + getColumnSelectClause() + " "
                + "FROM "
                + getName() + " "
                + "WHERE "
                + getWhereClause();
    }

    public void load() throws EntityLoadException {
        // Load any foreign keys
        preLoad();

        String sql = getLoadSQL();

        try {
            ArrayList<LowerCaseMap> rows = EntityDataSource.getInstance().executeQuery(sql, this);

            // Convert to a set if more than one row
            if (rows.size() > 1) {
                isArray(true);
            }

            // Now process the rows
            for (LowerCaseMap row : rows) {
                if (isArray()) {
                    Entity e = Entity.createEntityOrView(getViewName());

                    e.setChildList(getChildList());

                    e.setDeep(isDeep());
                    e.setModel(isModel());

                    e.processRow(row);

                    add(e);
                } else {
                    processRow(row);
                }
            }

            isDirty = false;
            isPersisted = rows.size() > 0;

        } catch (Exception e) {
            throw new EntityLoadException(e, getName());
        }
    }

    /**
     * Load any child entities
     *
     * @throws EntityLoadException
     */
    protected void onLoad() throws EntityLoadException {
        getChildList().load(this);
    }

    public void save() throws Exception {
        if (isDirty()) {
            if (isDeleted()) {
                // Delete the record
                doDelete();
            } 
            else // Check for update
            {
                if (isPersisted()) {
                    // Update
                    doUpdate();
                } 
                else 
                {
                    // Insert
                    doInsert();
                }
            }
        }

        onSave();
    }

    public void deleteChildren() throws Exception {
        for (Entity child : children) {
            child.setDirty(true);
            child.setDeleted(true);

            child.save();
        }
    }
    

    // Will only delete the current row
    public void doDelete() throws Exception {

        if (isArray())
        {
            for (Entity e : this)
            {
                if (e.isDeep() && e.getChildCount() > 0)
                    e.onDelete();
                
                e.deleteRow();
            }
        }
        else
        {
            if (isDeep() && getChildCount() > 0)
                onDelete();
                
            deleteRow();
        }
        
//        isQBE = false;
//
//        setFilter(null);
//
////	preLoad();
//
//        String sql = "DELETE FROM " + getName() + " WHERE " + getWhereClause();
//
//        try {
//            int result = EntityDataSource.getInstance().executeUpdate(sql, this);
//
//            if (result == 1) {
//
//                if (parent != null) {
//                    parent.children.remove(getViewName());
//                }
//
//                setDirty(false);
//                setPersisted(false);
//            }
//
//            // Delete any children in this entity
//            onDelete();
//
//        } catch (Exception e) {
//            throw new EntitySaveException(e, this);
//        }
    }
    
    protected void deleteRow() throws Exception
    {

//        preLoad();
        
        String sql = "DELETE FROM " + getName() + " WHERE " + getWhereClause();

        try {
            if (parent != null) {
                parent.children.remove(getViewName());

                // Null the foreign key (if any)
                for (KeyColumn kc : formMetaData.getKeyColumns())
                {
                    if (kc instanceof ForeignKey)
                        parent.setValue(kc.getName(), null);
                }
            }
            
            EntityDataSource.getInstance().executeUpdate(sql, this);

            setDirty(false);
            setPersisted(false);
       } catch (Exception e) {
            throw new EntitySaveException(e, this);
        }
    }

    protected void preInsert() {
        for (KeyColumn kc : formMetaData.getKeyColumns()) {
            kc.preInsert(this);
        }
    }

    protected void doInsert() throws Exception {
        preInsert();

        String sql = "SELECT " + getColumnSelectClause() + " FROM " + getName() + " WHERE 1=0";

        try (
                Statement s = EntityDataSource.getInstance().getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet rs = s.executeQuery(sql);) {
            rs.moveToInsertRow();

            for (ColumnData cd : formMetaData.getColumns()) {
                rs.updateObject(cd.getName(), getValue(cd.getName()));
            }

            rs.insertRow();

            // Now read back any key values
            rs.last();

            for (KeyColumn kc : formMetaData.getKeyColumns()) {
                setValue(kc.getName(), rs.getObject(kc.getName()));
            }

            setDirty(false);
            setPersisted(true);
        } catch (Exception e) {
            throw new EntitySaveException(e, this);
        }
    }

    protected void preUpdate() {
        for (KeyColumn kc : getFormMetaData().getKeyColumns()) {
            kc.preUpdate(this);
        }
    }

    protected void doUpdate() throws Exception {
        preUpdate();

        String sql = "SELECT " + getColumnSelectClause() + " FROM " + getName() + " WHERE " + getWhereClause();

        try {
            PreparedStatement s = EntityDataSource.getInstance().getConnection().prepareStatement(sql, ResultSet.FETCH_FORWARD, ResultSet.CONCUR_UPDATABLE);
            setParameterValues(s);
            ResultSet rs = s.executeQuery();

            if (rs.next()) {
                for (ColumnData cd : getFormMetaData().getColumns()) {
                    rs.updateObject(cd.getName(), getValue(cd.getName()));
                }

                rs.updateRow();
            }
        } catch (Exception e) {
            throw new EntitySaveException(e, this);
        }
    }

    /**
     * Save any child entities
     *
     * @throws Exception
     * @throws EntityLoadException
     */
    protected void onSave() throws Exception {
        for (Entity child : getChildren()) {
            child.save();
        }
    }

    protected void onDelete() throws Exception {
        for (Entity child : getChildren()) {
            child.doDelete();
        }
    }

    protected void processRow(LowerCaseMap row) throws EntityLoadException {
        columnValues = row;

        isPersisted = true;

        if (isDeep) {
            onLoad();
        }
    }

    public Entity getParent() {
        return parent;
    }

    public void setParent(Entity parent) {
        this.parent = parent;
    }

    public boolean isPersisted() {
        return isPersisted;
    }

    public void setPersisted(boolean isPersisted) {
        this.isPersisted = isPersisted;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void deleteAll() throws Exception
    {
        setDeleted(true);
        
        for (Entity e : this)
        {
            e.setDeleted(true);
        }
        
        save();
    }
    
    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
        this.isDirty = true;
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void setDirty(boolean isDirty) {
        this.isDirty = isDirty;
    }

    public boolean isDeep() {
        return isDeep;
    }

    public void setDeep(boolean isDeep) {
        this.isDeep = isDeep;
    }

    public boolean isModel() {
        return isModel;
    }

    public void setModel(boolean set) {
        isModel = set;
    }

    public List<Entity> getChildren() {
        return children.getList();
    }

    public ColumnList getColumnList() {
        return columnList;
    }

    public FormMetaData getFormMetaData() {
        return formMetaData;
    }

    public ChildList getChildList() {
        return childList;
    }

    public void setChildList(ChildList cl) {
        this.childList = cl;
    }

    public void setEntityValue(String name, String value) {
        switch (name) {
            case "__persisted":
                setPersisted(value.equals("true"));
                break;

            case "__deleted":
                setDeleted(value.equals("true"));
                break;

            case "__dirty":
                setDirty(value.equals("true"));
                break;

            case "__deep":
                setDeep(value.equals("true"));
                break;

            case "__set":
                isArray(value.equals("true"));
                break;

            case "__filter":
                setFilter(value);
                break;

            case "__elements":
                isArray(true);
                break;

            case "__model":
                setModel(value.equals("true"));
                break;
        }
    }

    /**
     *
     * @return The schema and table name in the form schema.tablename
     */
    public String getName() {
        return name;
    }

    /**
     * Override this method in a View to specify the columns for that view
     *
     * Be sure to call this method from the overridden method
     */
    public void initializeColumnList() {
        columnList.setInitialized(true);
    }

    /**
     * Typically there will not be a reason to override this method
     *
     * Be sure to call this method from any overridden method
     *
     * @throws EntityInitializationException
     * @throws FormMetaDataInitializationException
     */
    public void initializeFormMetaData() throws EntityInitializationException {
        try {
            formMetaData.initialize(this);

            formMetaData.setInitialized(true);
        } catch (Exception e) {
            throw new EntityInitializationException(e, getViewName());
        }
    }

    /**
     * Override this method in a View to specify the child entities for that
     * view
     *
     * Be sure to call this method from the overridden method
     */
    public void initializeChildList() {
        childList.setInitialized(true);
    }

    /**
     *
     * @return For an Entity, returns schema.tableName, for a view it returns
     * schema.viewName
     */
    public String getViewName() {
        return name;
    }

    public String getColumnSelectClause() {
        return getColumnList().getColumnSelectClause();
    }

    public void add(Entity entity) {
        if (children.isArray()) {
            // Add as index item
            children.add(entity);
        } else {
            // Add as child entity - no duplicates allowed
            children.add(entity.getViewName(), entity);

            setDeep(true);
        }
        
//        entity.setModel(isModel());

        entity.setParent(this);
    }

    public <T> T getChild(String name) {
        return (T) children.get(name);
    }

    // For set operations
    public <T> T getChild(int index) {
        return (T) children.getList().get(index);
    }

    /**
     * Key column handlers
     */
    public void preLoad() {
        for (KeyColumn kc : getFormMetaData().getKeyColumns()) {
            kc.preLoad(this);
        }
    }

    public <T> T getValue(String name) {
        return (T) columnValues.get(name);
    }

    public void setValue(String name, Object value) {
        if (value == null) {
            columnValues.remove(name);
        } else {
            columnValues.put(name, value);
        }

        setDirty(true);
    }

    public void setParsedValue(String name, String value) {
        if (value == null) {
            setValue(name, null);

            // if model, add the column to the entities column list
            if (isModel()) {
                getColumnList().add(name);
            }

        } else {
            ColumnData cd = formMetaData.getColumn(name);

            if (cd != null) {
                Object o = cd.getTypedValue(value);

                setValue(name, o);
            }
        }
    }

    public Map<String, Object> getColumnValues() {
        return columnValues;
    }

    static PrintStream ps = null;

    static private boolean beenHere = false;

    static private CommaSeparatedValues ecsv = new CommaSeparatedValues();
    static private CommaSeparatedValues vcsv = new CommaSeparatedValues();

    public String toJSON() throws IOException {

        beenHere = false;
        emitComma = false;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        ps = new PrintStream(bos);

        print("{\n\t");

        indent();

        toJSONInternal();

        outdent();

        print("\n}");

        ps.flush();

        String retval = bos.toString();

        ps.close();

        ps = null;

        return retval;
    }

    protected void toJSONInternal() throws IOException {
        toJSONInternal(true);
    }

    protected void toJSONInternal(boolean outputName) throws IOException {

        try {
            indent();

            if (outputName) {
                print("\n\"" + getViewName() + "\": {\n");
            }

            suppressComma();

            if (!isModel)
            {
                // Output control values

                println("\"__persisted\": " + isPersisted);
                println("\"__deleted\": " + isDeleted);
                println("\"__dirty\": " + isDirty);
                println("\"__deep\": " + isDeep);
                println("\"__set\": " + isArray());
                println("\"__filter\": " + ((filter != null) ? "\"" + filter + "\"" : filter));
                println("\"__model\": " + isModel);
            }
            
            if (isArray()) {
                println("\"__elements\": [", false);

                indent();

                boolean outOne = false;

                for (Entity child : children) {

                    if (outOne) {
                        print(",\n");
                    }

                    println("{");

                    child.toJSONInternal(false);

                    print("}");

                    outOne = true;
                }

                outdent();

                print("\n]");
            } else {
                // Output values
                for (ColumnData cd : formMetaData.getColumns()) {
                    println("\"" + cd.getName() + "\": " + cd.getStringValue(columnValues.get(cd.getName())));
                }

                if (getChildCount() > 0) {
                    print(",\n");

                    for (Entity child : children) {
                        child.toJSONInternal();
                    }
                }
            }

            outdent();

            if (outputName) {
                print("\n}");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getChildCount() {
        return children.getList().size();
    }

    private void printStart() {

    }

    private void printBody() {

    }

    private void printEnd() {

    }

    public String getStringValue(String name) {
        ColumnData cd = formMetaData.getColumn(name);

        Object o = getValue(name);

        return cd.getStringValue(o);
    }

    static protected int level = 0;
    static protected String padString = "";

    protected void indent() {
        level++;
    }

    protected void outdent() {
        level--;

        beenHere = false;
    }

    private String padding() {
        padString = "";

        for (int i = 0; i < level; i++) {
            padString += "\t";
        }

        return padString;
    }

    static String lastChar = null;

    protected void println(String s) throws IOException {
        println(s, true);
    }

    static boolean emitComma = false;

    protected void println(String s, boolean useComma) throws IOException {
        if (beenHere & emitComma) {
            print(",\n");
        } else {
            print("\n");
        }

        print(padding());

        print(s);

        beenHere = true;
        emitComma = useComma;

    }

    private void suppressComma() {
        beenHere = false;
        emitComma = false;
    }

    private void print(String s) throws IOException {
        ps.write(s.getBytes());

//		if (s.equals(","))
//			ps.write("\n".getBytes());
    }

    public void qbe() {
        isQBE = true;

        load();

        isQBE = false;
    }

    public void isArray(boolean set) {
        children.isArray(set);
    }

    public boolean isArray() {
        return children.isArray();
    }

    public void setFilter(String filter) {
        this.filter = filter;
        
        // Prepare to load a set
        isArray(filter != null);
    }

    public String getFilter() {
        return filter;
    }
    
    public void updateAll(String columnName, Object value)
    {
        for (Object o : getChildren().toArray()) {
         
            Entity e = (Entity) o;
            e.setValue(columnName, value);
        }
    }
}