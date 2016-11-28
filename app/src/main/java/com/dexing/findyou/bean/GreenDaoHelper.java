package com.dexing.findyou.bean;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dexing on 2016/11/2.
 */
public class GreenDaoHelper {

    private String TAG = GreenDaoHelper.class.getSimpleName();
    private static GreenDaoHelper mInstance = null;
    private SQLiteDatabase writeDB, readDB;
    private DaoMaster writeDaoMaster, readDaoMaster;
    private DaoSession writeDaoSession, readDaoSession;

    private GreenDaoHelper() {
    }

    public static GreenDaoHelper getInstance() {
        synchronized (GreenDaoHelper.class) {
            if (mInstance == null) {
                mInstance = new GreenDaoHelper();
            }
        }
        return mInstance;
    }

    public void initGreenDao(Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "findyou", null);

        writeDB = helper.getWritableDatabase();
        readDB = helper.getReadableDatabase();

        writeDaoMaster = new DaoMaster(writeDB);
        readDaoMaster = new DaoMaster(readDB);

        writeDaoSession = writeDaoMaster.newSession();
        readDaoSession = readDaoMaster.newSession();

    }

    public void clearData() {
        if (writeDaoSession != null) {
            writeDaoSession.getUserDao().deleteAll();
        }
    }

    public void insertUser(User user) {
        if (writeDaoSession != null) {
            clearData();
            writeDaoSession.getUserDao().insert(user);

        }
    }

    public User getCurrentUser() {
        if (readDaoSession != null) {
            return readDaoSession.getUserDao().queryBuilder().unique();
        }
        return null;
    }

}
