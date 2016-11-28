package com.dexing.findyou;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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

        String userName = (String) SharedPreferencesUtil.getData(SplashActivity.this, SharedPreferencesUtil.KEY_LOGIN_NAME, "");
        String password = (String) SharedPreferencesUtil.getData(SplashActivity.this, SharedPreferencesUtil.KEY_PWD, "");
        if (userName.isEmpty() || password.isEmpty()) {
            toLoginActivity();
        } else {
            toLogin(userName, password);
        }
    }

    private void toLogin(final String name, final String pwd) {
        final BmobUser bu2 = new BmobUser();
        bu2.setUsername(name);
        bu2.setPassword(pwd);
        //login回调
        bu2.loginObservable(BmobUser.class).subscribe(new Subscriber<BmobUser>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                toLoginActivity();
            }

            @Override
            public void onNext(BmobUser bmobUser) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private void toLoginActivity(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        }, 2000);
    }

}
