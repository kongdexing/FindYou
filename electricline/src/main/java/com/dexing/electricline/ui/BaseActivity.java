package com.dexing.electricline.ui;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bmob.v3.exception.BmobException;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by dexing on 2017/5/31.
 * No1
 */

public class BaseActivity extends AppCompatActivity {

    public static String TAG = "";
    private CompositeSubscription mCompositeSubscription;
    public Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getSimpleName();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        unbinder = ButterKnife.bind(this);
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
        if (unbinder != null) {
            unbinder.unbind();
        }
        if (this.mCompositeSubscription != null) {
            this.mCompositeSubscription.unsubscribe();
        }
    }

}
