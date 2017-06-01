package com.dexing.electricline.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.dexing.electricline.R;

public class BottomPointView extends LinearLayout implements View.OnClickListener {

    private String TAG = BottomPointView.class.getSimpleName();
    private LinearLayout llLook;
    private LinearLayout llDel;
    private LinearLayout rlBack;
    private OnBottomChatClickListener clickListener;

    public BottomPointView(Context context) {
        this(context, null);
    }

    public BottomPointView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.widget_bottom_chat, this, true);
        try {
            llLook = (LinearLayout) view.findViewById(R.id.llLook);
            llDel = (LinearLayout) view.findViewById(R.id.llDel);
            rlBack = (LinearLayout) view.findViewById(R.id.llBack);
            llLook.setOnClickListener(this);
            llDel.setOnClickListener(this);
            rlBack.setOnClickListener(this);
        } catch (Exception ex) {
            Log.i(TAG, "AlbumSourceView: " + ex.getMessage());
        }
    }

    public void setOnBottomChatClickListener(OnBottomChatClickListener listener) {
        clickListener = listener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llLook:
                if (clickListener != null) {
                    clickListener.onLookClick();
                }
                break;
            case R.id.llDel:
                if (clickListener != null) {
                    clickListener.onDeleteClick();
                }
                break;
            case R.id.llBack:
                if (clickListener != null) {
                    clickListener.onBack();
                }
                break;
        }
    }

    public interface OnBottomChatClickListener {
        void onLookClick();

        void onDeleteClick();

        void onBack();
    }

}
