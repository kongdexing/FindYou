package com.dexing.findyou;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.dexing.findyou.bean.GreenDaoHelper;
import com.dexing.findyou.bean.User;
import com.dexing.findyou.mine.LoginActivity;
import com.dexing.findyou.util.SharedPreferencesUtil;

import butterknife.BindView;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import rx.Subscriber;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        User user = GreenDaoHelper.getInstance().getCurrentUser();
        String password = (String) SharedPreferencesUtil.getData(this, SharedPreferencesUtil.KEY_PWD, "");
        if (user == null || user.getUsername().isEmpty() || password.isEmpty()) {
            toLoginActivity();
        } else {
            toLogin(user.getUsername(), password);
        }
    }

    private void toLogin(final String name, final String pwd) {
        final User bu2 = new User();
        bu2.setUsername(name);
        bu2.setPassword(pwd);
        //login回调
        bu2.loginObservable(User.class).subscribe(new Subscriber<User>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                toLoginActivity();
            }

            @Override
            public void onNext(User bmobUser) {
                GreenDaoHelper.getInstance().insertUser(bmobUser);
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        });
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
