package com.dexing.electricline.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "EPOINT".
*/
public class EPointDao extends AbstractDao<EPoint, Void> {

    public static final String TABLENAME = "EPOINT";

    /**
     * Properties of entity EPoint.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Latitude = new Property(0, double.class, "latitude", false, "LATITUDE");
        public final static Property Longitude = new Property(1, double.class, "longitude", false, "LONGITUDE");
        public final static Property VillageId = new Property(2, String.class, "villageId", false, "VILLAGE_ID");
        public final static Property Type = new Property(3, int.class, "type", false, "TYPE");
        public final static Property Number = new Property(4, String.class, "number", false, "NUMBER");
    };


    public EPointDao(DaoConfig config) {
        super(config);
    }
    
    public EPointDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"EPOINT\" (" + //
                "\"LATITUDE\" REAL NOT NULL ," + // 0: latitude
                "\"LONGITUDE\" REAL NOT NULL ," + // 1: longitude
                "\"VILLAGE_ID\" TEXT," + // 2: villageId
                "\"TYPE\" INTEGER NOT NULL ," + // 3: type
                "\"NUMBER\" TEXT);"); // 4: number
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"EPOINT\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, EPoint entity) {
        stmt.clearBindings();
        stmt.bindDouble(1, entity.getLatitude());
        stmt.bindDouble(2, entity.getLongitude());
 
        String villageId = entity.getVillageId();
        if (villageId != null) {
            stmt.bindString(3, villageId);
        }
        stmt.bindLong(4, entity.getType());
 
        String number = entity.getNumber();
        if (number != null) {
            stmt.bindString(5, number);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, EPoint entity) {
        stmt.clearBindings();
        stmt.bindDouble(1, entity.getLatitude());
        stmt.bindDouble(2, entity.getLongitude());
 
        String villageId = entity.getVillageId();
        if (villageId != null) {
            stmt.bindString(3, villageId);
        }
        stmt.bindLong(4, entity.getType());
 
        String number = entity.getNumber();
        if (number != null) {
            stmt.bindString(5, number);
        }
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public EPoint readEntity(Cursor cursor, int offset) {
        EPoint entity = new EPoint( //
            cursor.getDouble(offset + 0), // latitude
            cursor.getDouble(offset + 1), // longitude
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // villageId
            cursor.getInt(offset + 3), // type
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4) // number
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, EPoint entity, int offset) {
        entity.setLatitude(cursor.getDouble(offset + 0));
        entity.setLongitude(cursor.getDouble(offset + 1));
        entity.setVillageId(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setType(cursor.getInt(offset + 3));
        entity.setNumber(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(EPoint entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(EPoint entity) {
        return null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
