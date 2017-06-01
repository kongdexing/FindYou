package com.dexing.electricline.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dexing.electricline.R;

public class MarkerPoleNumView extends LinearLayout {

    private TextView txtNum;

    public MarkerPoleNumView(Context context) {
        this(context, null);
    }

    public MarkerPoleNumView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.view_markernum, this, true);
        txtNum = (TextView) view.findViewById(R.id.txtNum);
    }

    public void setNumber(String number) {
        txtNum.setText(number);
    }

}
