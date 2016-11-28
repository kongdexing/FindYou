package com.dexing.findyou;

import android.app.Dialog;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bmob.v3.exception.BmobException;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class BaseActivity extends AppCompatActivity {

    public static String TAG = "";
    public LinearLayout llContent;  //主视图
    private RelativeLayout llActionBar;  //ActionBar
    private ImageView imgBack;
    private TextView txtTitle, txtRight;
    private Dialog progressDialog;
    public Unbinder unbinder;
    private CompositeSubscription mCompositeSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getSimpleName();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        View contentView = LayoutInflater.from(this).inflate(layoutResID, null, false);
        View parentView = LayoutInflater.from(this).inflate(R.layout.activity_base, null, true);

        llActionBar = (RelativeLayout) parentView.findViewById(R.id.includeActionBar);
        imgBack = (ImageView) parentView.findViewById(R.id.img_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        txtTitle = (TextView) parentView.findViewById(R.id.txtTitle);
        txtRight = (TextView) parentView.findViewById(R.id.txtRight);
        llContent = (LinearLayout) parentView.findViewById(R.id.llContent);

        txtTitle.setText(this.getTitle());
        llContent.addView(contentView);
        setContentView(parentView);
        unbinder = ButterKnife.bind(this);
    }

    public void showImgBack(boolean show) {
        if (imgBack != null) {
            imgBack.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 解决Subscription内存泄露问题
     *
     * @param s
     */
    protected void addSubscription(Subscription s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }
        this.mCompositeSubscription.add(s);
    }

    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void toast(int msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public static void log(String msg) {
        Log.i(TAG, "===============================================================================");
        Log.i(TAG, msg);
    }

    public static void loge(Throwable e) {
        Log.i(TAG, "===============================================================================");
        if (e instanceof BmobException) {
            Log.e(TAG, "错误码：" + ((BmobException) e).getErrorCode() + ",错误描述：" + ((BmobException) e).getMessage());
        } else {
            Log.e(TAG, "错误描述：" + e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.mCompositeSubscription != null) {
            this.mCompositeSubscription.unsubscribe();
        }
    }

}
