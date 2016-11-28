package com.dexing.findyou.mine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dexing.findyou.BaseActivity;
import com.dexing.findyou.MainActivity;
import com.dexing.findyou.R;
import com.dexing.findyou.SplashActivity;
import com.dexing.findyou.bean.GreenDaoHelper;
import com.dexing.findyou.bean.User;
import com.dexing.findyou.util.CommonUtil;
import com.dexing.findyou.util.SharedPreferencesUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import rx.Subscriber;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.edtAccount)
    EditText edtAccount;

    @BindView(R.id.edtPwd)
    EditText edtPwd;

    @BindView(R.id.progress)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @OnClick({R.id.btnLogin, R.id.btnRegister, R.id.txtForgetPWD})
    void viewOnClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                String phone = edtAccount.getText().toString().trim();
                String pwd = edtPwd.getText().toString().trim();
                if (phone.isEmpty() || pwd.isEmpty()) {
                    Toast.makeText(this, R.string.toast_input_empty, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!CommonUtil.isPhone(phone)) {
                    Toast.makeText(this, R.string.toast_phone_error, Toast.LENGTH_SHORT).show();
                    edtAccount.setSelection(phone.length());
                    edtAccount.setFocusable(true);
                    return;
                }
                toLogin(phone, pwd);
                break;
            case R.id.btnRegister:
                startActivityForResult(new Intent(LoginActivity.this, RegisterActivity.class), 1);
//                finish();
                break;
            case R.id.txtForgetPWD:
                startActivity(new Intent(LoginActivity.this, ForgetPwdActivity.class));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void toLogin(final String name, final String pwd) {
        final User bu2 = new User();
        bu2.setUsername(name);
        bu2.setPassword(pwd);
        //login回调
        progressBar.setVisibility(View.VISIBLE);
        bu2.loginObservable(User.class).subscribe(new Subscriber<User>() {
            @Override
            public void onCompleted() {
                log("----onCompleted----");
            }

            @Override
            public void onError(Throwable e) {
                progressBar.setVisibility(View.GONE);
                loge(new BmobException(e));
                toast("登陆失败");
            }

            @Override
            public void onNext(User bmobUser) {
                progressBar.setVisibility(View.GONE);
                toast("登陆成功");
                GreenDaoHelper.getInstance().insertUser(bmobUser);
                SharedPreferencesUtil.saveData(LoginActivity.this, SharedPreferencesUtil.KEY_PWD, pwd);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });
    }


}
