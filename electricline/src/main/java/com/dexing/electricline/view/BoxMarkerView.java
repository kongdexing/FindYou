package com.dexing.electricline.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dexing.electricline.R;
import com.dexing.electricline.model.EPoint;

/**
 */
public class BoxMarkerView extends LinearLayout {

    private CircularImageView imgHead;
    private TextView txtNum;

    public BoxMarkerView(Context context) {
        this(context, null);
    }

    public BoxMarkerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.view_marker_box, this, true);
//        flContent = (FrameLayout) view.findViewById(R.id.fl_Content);
        imgHead = (CircularImageView) view.findViewById(R.id.imgHead);
        txtNum = (TextView) view.findViewById(R.id.txtNum);
    }

    public void setBackResource(int resouce) {
        imgHead.setImageResource(resouce);
    }

    public void setPointNum(String number) {
        txtNum.setText(number);
    }

}
