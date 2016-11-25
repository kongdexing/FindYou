package com.android.volley.common;

import com.google.gson.JsonElement;

import org.json.JSONObject;

public class VolleyHttpResult {

    private int status;
    private String url;
    private String info;
    private Object data;

    public VolleyHttpResult() {
        status = 0;
        url = info = "";
        data = null;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
