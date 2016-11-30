package com.dexing.findyou.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by dexing on 2016/11/30.
 * No1
 */

public class BaseListAdapter extends BaseAdapter {

    public Context mContext;
    public String TAG = getClass().getSimpleName();

    public BaseListAdapter(Context context) {
        super();
        mContext = context;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
