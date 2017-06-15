package com.dexing.electricline.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dexing on 2017-6-14 0014.
 */

public class GreenDaoHelper {

    private String TAG = GreenDaoHelper.class.getSimpleName();
    private static GreenDaoHelper mInstance = null;
    private SQLiteDatabase writeDB, readDB;
    private static DaoMaster writeDaoMaster, readDaoMaster;
    private static DaoSession writeDaoSession, readDaoSession;

    private GreenDaoHelper() {
    }

    public static GreenDaoHelper getInstance() {
        synchronized (GreenDaoHelper.class) {
            if (mInstance == null) {
                mInstance = new GreenDaoHelper();
            }
        }
        if (writeDaoMaster != null) {
            writeDaoSession = writeDaoMaster.newSession();
        } else {
            writeDaoSession = null;
        }

        if (readDaoMaster != null) {
            readDaoSession = readDaoMaster.newSession();
        } else {
            readDaoSession = null;
        }
        return mInstance;
    }

    public void initGreenDao(Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "electric", null);
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;

        writeDB = helper.getWritableDatabase();
        readDB = helper.getReadableDatabase();

        writeDaoMaster = new DaoMaster(writeDB);
        readDaoMaster = new DaoMaster(readDB);
    }

    public void insertBoxUser(Village village, List<BoxUser> users) {
        if (writeDaoSession != null) {
            String sql = "delete from " + BoxUserDao.TABLENAME + " where " + BoxUserDao.Properties.VillageId.columnName + "='" + village.getObjectId() + "'";
            writeDaoSession.getDatabase().execSQL(sql);
            writeDaoSession.getBoxUserDao().insertInTx(users);
        }
    }

    public List<BoxUser> getUserByInfo(String villageId) {
        if (readDaoSession != null) {
//            String sql = "select * from " + BoxUserDao.TABLENAME + " where " + BoxUserDao.Properties.VillageId.columnName + "='" + villageId + "'"
//                    + " and (" + BoxUserDao.Properties.UserNum.columnName + " like '%" + number + "%' or " + BoxUserDao.Properties.UserName.columnName + " like '%" + name + "%')";
            QueryBuilder qb = readDaoSession.getBoxUserDao().queryBuilder();
            qb.where(BoxUserDao.Properties.VillageId.eq(villageId));
//            qb.or(BoxUserDao.Properties.UserNum.like(number), BoxUserDao.Properties.UserName.like(name))
            return qb.build().list();

//            return readDaoSession.getBoxUserDao().queryRaw(" where ? = ? and (? like ? or ? like ?)",
//                    BoxUserDao.Properties.VillageId.columnName, villageId, BoxUserDao.Properties.UserNum.columnName,
//                    "'%" + number + "%'", BoxUserDao.Properties.UserName.columnName, "'%" + name + "%'");

        }
        return new ArrayList<BoxUser>();
    }

}
