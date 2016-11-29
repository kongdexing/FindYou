package com.dexing.findyou;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.dexing.findyou.bean.FUser;
import com.dexing.findyou.bean.GreenDaoHelper;
import com.dexing.findyou.mine.LoginActivity;
import com.dexing.findyou.util.SharedPreferencesUtil;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import rx.Subscriber;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setTitleVisibility(View.GONE);
        
        FUser user = GreenDaoHelper.getInstance().getCurrentUser();
        if (user == null || user.getLoginName().isEmpty() || user.getPassword().isEmpty()) {
            toLoginActivity();
        } else {
            toLogin(user.getLoginName(), user.getPassword());
        }
    }

    private void toLogin(final String name, final String pwd) {
        final FUser bu2 = new FUser();
        bu2.setLoginName(name);
        bu2.setPassword(pwd);

        BmobQuery<FUser> bmobQuery = new BmobQuery<FUser>();
        bmobQuery.addWhereEqualTo("loginName", name);
        bmobQuery.addWhereEqualTo("password", pwd);
        addSubscription(bmobQuery.findObjects(new FindListener<FUser>() {

            @Override
            public void done(List<FUser> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        GreenDaoHelper.getInstance().insertUser(list.get(0));
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    } else {
                        toLoginActivity();
                    }
                } else {
                    toLoginActivity();
                }
            }
        }));

    }

    private void toLoginActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        }, 2000);
    }

}
