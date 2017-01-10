package com.android.volley.common;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.boye.httpclientandroidlib.impl.client.cache.CacheConfig;

public class VolleyHttpService {

    private String TAG = VolleyHttpService.class.getSimpleName();
    private static VolleyHttpService mInstance = null;
    private static RequestQueue queue = null;
    private static Context mContext;
    private static String serverURL;

    private VolleyHttpService() {
    }

    public static void init(Context context, String url) {
        mContext = context;
        serverURL = url;
    }

    public static VolleyHttpService getInstance() {
        synchronized (VolleyHttpService.class) {
            if (mContext == null) {
                return null;
            }
            if (mInstance == null) {
                mInstance = new VolleyHttpService();
            }
            if (queue == null) {
                queue = Volley.newRequestQueue(mContext);
            }
            return mInstance;
        }
    }

    public void sendPostRequest(final String urlAction, final VolleyHttpParamsEntity antHttpParamsEntity, final VolleyRequestListener requestListener) {
        postRequest(urlAction, antHttpParamsEntity, null, requestListener);
    }

    public void sendPostRequest(final String urlAction, final VolleyHttpParamsEntity antHttpParamsEntity, DefaultRetryPolicy retryPolicy, final VolleyRequestListener requestListener) {
        postRequest(urlAction, antHttpParamsEntity, retryPolicy, requestListener);
    }

    public void cancelAllRequest() {
        if (queue != null) {
            queue.cancelAll(null);
        }
    }

    public void uploadFiles(final String urlAction, final VolleyHttpParamsEntity httpParamsEntity, List<String> filePathList, final VolleyRequestListener requestListener) {
        if (requestListener != null) {
            requestListener.onStart();
        }

        MultipartRequest mr = new MultipartRequest(serverURL + urlAction, filePathList,httpParamsEntity.getMap(),
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        VolleyHttpResult result = new VolleyHttpResult();
                        analyseResponse(response, result);
                        if (requestListener != null) {
                            requestListener.onResponse(result);
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onErrorResponse :" + error.getMessage());
                if (requestListener != null) {
                    requestListener.onErrorResponse(error);
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.i(TAG, "upload file getHeaders: ");
                Map<String, String> headers = new HashMap<String, String>();
                HashMap<String, String> cookieMap = CookieUtil.getCookieMap();
                for (String headerName : cookieMap.keySet()) {
                    Log.i("volley", "header  " + headerName + ":" + cookieMap.get(headerName));
                    headers.put(headerName, cookieMap.get(headerName));
                }
                return headers;
            }
        };
        queue.add(mr);
    }

    private void postRequest(final String urlAction, final VolleyHttpParamsEntity httpParamsEntity, DefaultRetryPolicy retryPolicy, final VolleyRequestListener requestListener) {
        if (requestListener != null) {
            requestListener.onStart();
        }

        if (httpParamsEntity != null) {
            Log.i(TAG, "postRequest: action " + serverURL + urlAction);
        }

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, serverURL + urlAction,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        VolleyHttpResult result = new VolleyHttpResult();
                        analyseResponse(response, result);
                        if (requestListener != null) {
                            requestListener.onResponse(result);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onErrorResponse :" + error.getMessage());
                if (requestListener != null) {
                    requestListener.onErrorResponse(error);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (httpParamsEntity == null) {
                    Log.i(TAG, "getParams: is null");
                    return null;
                }
                Log.i(TAG, "getParams: map string " + httpParamsEntity.getMap().toString());
                return httpParamsEntity.getMap();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.i(TAG, "getHeaders: ");
                Map<String, String> headers = new HashMap<String, String>();
                HashMap<String, String> cookieMap = CookieUtil.getCookieMap();
                for (String headerName : cookieMap.keySet()) {
                    Log.i("volley", "header  " + headerName + ":" + cookieMap.get(headerName));
                    headers.put(headerName, cookieMap.get(headerName));
                }
                return headers;
            }
        };
        if (retryPolicy != null) {
            stringRequest.setRetryPolicy(retryPolicy);
        }
        stringRequest.setShouldCache(true);
        queue.add(stringRequest);
    }

    private void analyseResponse(String response, VolleyHttpResult result) {
        try {
            Log.i(TAG, response.toString());
            JSONObject object = new JSONObject(response.toString());
            result.setStatus(object.getInt("status"));
            result.setUrl(object.getString("url"));
            try {
                result.setData(object.get("data"));
                Log.i(TAG, "analyseResponse: data--" + result.getData());
            } catch (Exception ex) {
                Log.e(TAG, "analyseResponse: data " + ex.getMessage());
            }

            try {
                result.setInfo(object.getString("info"));
                Log.i(TAG, "analyseResponse: info--" + result.getInfo());
            } catch (Exception ex) {
                Log.e(TAG, "analyseResponse: info " + ex.getMessage());
            }
        } catch (Exception ex) {
            Log.e(TAG, "onResponse: parserJSON error " + ex.getMessage());
            result.setStatus(ResultCode.ParseJsonError);
            result.setInfo(ex.getMessage());
        }
    }

}
