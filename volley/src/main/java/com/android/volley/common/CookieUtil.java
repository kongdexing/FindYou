package com.android.volley.common;

import android.util.Log;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/11/20.
 */

public class CookieUtil {

    private static HashMap<String, String> cookieMap = new HashMap<>();

    public static void setCookieMap(HashMap<String, String> cookie) {
        cookieMap = cookie;
    }

    public static void putCookie(String key, String val) {
        cookieMap.put(key, val);
    }

    public static HashMap<String, String> getCookieMap() {
        return cookieMap;
    }

}
