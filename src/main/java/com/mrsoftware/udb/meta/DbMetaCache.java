package com.mrsoftware.udb.meta;

import com.mrsoftware.udb.Entity;
import com.mrsoftware.udb.exceptions.FormMetaDataInitializationException;
import com.mrsoftware.udb.util.LowerCaseMap;

/**
 *
 * @author rpbarbati
 *
 * DbMetaCache stores meta data class instances that are used across multiple
 * Entity instances.
 *
 * The classes it handles are... FormData ChildViews
 *
 * All cached instances are stored using the schema.tableName or
 * schema.viewName.
 *
 */
public class DbMetaCache {

    static private LowerCaseMap<DbMetaData> cache = new LowerCaseMap();

    static public ColumnList getColumnList(Entity entity, boolean useCache) {
        ColumnList cna = null;

        String cacheName = DbMetaData.MetaType.ColumnList + "." + entity.getViewName();

        if (useCache) {
            cna = cache.getType(cacheName);
        }

        if (cna == null) {
            cna = new ColumnList(entity.getViewName());

            cache.put(cacheName, cna);
        }

        return cna;
    }

    static public FormMetaData getFormMetaData(Entity entity, boolean useCache) throws FormMetaDataInitializationException {
        FormMetaData fmd = null;

        String cacheName = DbMetaData.MetaType.FormMetaData + "." + entity.getViewName();

        if (useCache) {
            fmd = cache.getType(cacheName);
        }

        if (fmd == null) {
            fmd = new FormMetaData(entity.getViewName());

            cache.put(cacheName, fmd);
        }

        return fmd;
    }

    static public ChildList getChildList(Entity entity, boolean useCache) {
        ChildList cl = null;

        String cacheName = DbMetaData.MetaType.ChildList + "." + entity.getViewName();

        if (useCache) {
            cl = cache.getType(cacheName);
        }

        if (cl == null) {
            cl = new ChildList(entity.getViewName());

            cache.put(cacheName, cl);
        }

        return cl;
    }
}
