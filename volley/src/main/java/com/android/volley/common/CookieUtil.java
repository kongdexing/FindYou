package com.android.volley.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.StateSet;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/11/20.
 */

public class CookieUtil {

    private static Context mContext;
    private static SharedPreferences sharedPreferences;
    public static String COOKIE = "Cookie";

    public static void initLocalCookie(Context context) {
        if (context == null) {
            return;
        }
        mContext = context;
        Log.i("volley", "initLocalCookie: " + mContext.getPackageName());
        sharedPreferences = mContext
                .getSharedPreferences(mContext.getPackageName(), Context.MODE_PRIVATE);
    }

    public static void putCookie(String val) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(COOKIE, val);
        editor.commit();
    }

    public static String getCookie() {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(COOKIE, "");
        }
        return "";
    }

}
