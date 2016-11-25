package com.android.volley.common;

import com.android.volley.VolleyError;

/**
 * Created by Administrator on 2016/10/18 0018.
 */

public interface VolleyRequestListener {

    void onStart();

    void onResponse(VolleyHttpResult httpResult);

    void onErrorResponse(VolleyError error);

}
