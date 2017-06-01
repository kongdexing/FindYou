package com.dexing.electricline.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dexing.electricline.R;

/**
 */
public class MarkerView extends LinearLayout {

    private FrameLayout flContent;
    private CircularImageView imgHead;
    private TextView txtNum;

    public MarkerView(Context context) {
        this(context, null);
    }

    public MarkerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.view_marker_student, this, true);
        flContent = (FrameLayout) view.findViewById(R.id.fl_Content);
        imgHead = (CircularImageView) view.findViewById(R.id.imgHead);
        txtNum = (TextView) view.findViewById(R.id.txtNum);
    }

    public void isPolePoint(boolean b) {
        if (b) {
            flContent.setBackgroundResource(R.drawable.icon_marker_pole_bg);
            imgHead.setImageResource(R.drawable.icon_tower);
        } else {
            flContent.setBackgroundResource(R.drawable.icon_marker_box_bg);
            imgHead.setImageResource(R.drawable.icon_box);
        }
    }

    public void setPointNum(String number) {
        txtNum.setText(number);
    }

}