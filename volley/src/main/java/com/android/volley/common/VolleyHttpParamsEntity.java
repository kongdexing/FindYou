package com.android.volley.common;

import java.util.HashMap;

public class VolleyHttpParamsEntity {

    private HashMap<String, String> map = null;

    public VolleyHttpParamsEntity() {
        map = new HashMap<String, String>();
    }

    public VolleyHttpParamsEntity addParam(String key, String val) {
        map.put(key, val);
        return this;
    }

    public HashMap<String, String> getMap() {
        return map;
    }

}
