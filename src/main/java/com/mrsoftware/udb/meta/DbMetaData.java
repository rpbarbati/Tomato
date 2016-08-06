package com.mrsoftware.udb.meta;

public class DbMetaData {

    public static enum MetaType {
        ColumnList, FormMetaData, ChildList
    };

    private MetaType metaType;
    private String name;
//	protected Entity entity;

    protected boolean initialized = false;

    protected DbMetaData(MetaType metaType, String name) {
        this.metaType = metaType;
        this.name = name;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public String getName() {
        return metaType.name() + "." + name;
    }

//	public String getName()
//	{
//		return name;
//	}
}
