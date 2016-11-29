package com.dexing.findyou;

import android.app.Dialog;
import android.content.Intent;
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

import com.android.widget.view.CircularImageView;
import com.dexing.findyou.bean.GreenDaoHelper;
import com.dexing.findyou.mine.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bmob.v3.exception.BmobException;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class BaseActivity extends AppCompatActivity {

    public static String TAG = "";
    @BindView(R.id.llContent)
    LinearLayout llContent;  //主视图
    @BindView(R.id.includeActionBar)
    RelativeLayout llActionBar;  //ActionBar
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.txtRight)
    TextView txtRight;
    @BindView(R.id.imgHead)
    CircularImageView imgHead;

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
        llContent = (LinearLayout) parentView.findViewById(R.id.llContent);
        llContent.addView(contentView);
        setContentView(parentView);
        unbinder = ButterKnife.bind(this);
        txtTitle.setText(this.getTitle());
    }

    @OnClick({R.id.img_back, R.id.imgHead})
    void onActionBarClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.imgHead:
                if (GreenDaoHelper.getInstance().getCurrentUser() == null) {

                } else {

                }
                startActivity(new Intent(BaseActivity.this, LoginActivity.class));
                break;
        }
    }

    public void showImgBack(boolean show) {
        if (imgBack != null) {
            imgBack.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    public void showImgHead(boolean show) {
        if (imgHead != null) {
            imgHead.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 设置ActionBar名称
     *
     * @param strId
     */
    public void setTitle(int strId) {
        setTitle(getResources().getString(strId));
    }

    /**
     * 设置ActionBar名称
     *
     * @param str
     */
    public void setTitle(String str) {
        if (txtTitle != null) {
            txtTitle.setText(str);
        }
    }

    public void setTitleVisibility(int visibility) {
        if (txtTitle != null) {
            txtTitle.setVisibility(visibility);
        }
    }

    public void setTxtRight(int strId) {
        setTxtRight(getResources().getString(strId));
    }

    public void setTxtRight(String str) {
        if (txtRight != null) {
            txtRight.setText(str);
        }
    }

    public void setTextRightClickListener(View.OnClickListener listener) {
        if (txtRight != null)
            txtRight.setOnClickListener(listener);
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
