package com.android.volley.common;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import ch.boye.httpclientandroidlib.HttpEntity;
import ch.boye.httpclientandroidlib.entity.ContentType;
import ch.boye.httpclientandroidlib.entity.mime.MultipartEntityBuilder;
import ch.boye.httpclientandroidlib.entity.mime.content.FileBody;

public class MultipartRequest extends Request<String> {

    private HttpEntity mHttpEntity;
    private Response.Listener mListener;
    private List<String> mFilePathList;
    HashMap<String, String> mParams;

    public MultipartRequest(String url, List<String> filePathList,
                            HashMap<String, String> params,
                            Response.Listener<String> listener,
                            Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        mFilePathList = filePathList;
        mParams = params;
        mListener = listener;
        mHttpEntity = buildMultipartEntity();
    }

    private HttpEntity buildMultipartEntity() {
        try {
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            int size = mFilePathList.size();
            for (int i = 0; i < size; i++) {// add Image File
                String fileName = mFilePathList.get(i);
                File file = new File(fileName);
                FileBody fileBody = new FileBody(file);
                builder.addPart("pic" + i, fileBody);
                Log.i("volley", "upload file " + fileName);
            }
            Log.i("volley", "upload params " + mParams.toString());
            Iterator iterator = mParams.keySet().iterator();
            while (iterator.hasNext()) {
                Object key = iterator.next();
                builder.addTextBody(key.toString(), mParams.get(key), ContentType.APPLICATION_JSON);
            }
            return builder.build();
        } catch (Exception e) {

        }

        return null;
    }

    @Override
    public String getBodyContentType() {
        return mHttpEntity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            mHttpEntity.writeTo(bos);
        } catch (Exception e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream " + e.getMessage());
        }
        return bos.toByteArray();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String xmlString = null;
        try {
            xmlString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
        } catch (Exception e) {

        }
        return Response.success(xmlString, getCacheEntry());
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }
}