package com.dexing.findyou.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dexing.findyou.BaseActivity;
import com.dexing.findyou.MainActivity;
import com.dexing.findyou.R;
import com.dexing.findyou.bean.FUser;
import com.dexing.findyou.bean.GreenDaoHelper;
import com.dexing.findyou.util.CommonUtil;
import com.dexing.findyou.util.SharedPreferencesUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
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
                toLogin(phone, CommonUtil.md5(pwd));
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
        //判断用户是否存在
        BmobQuery<FUser> bmobQuery = new BmobQuery<FUser>();
        bmobQuery.addWhereEqualTo("loginName", name);
        bmobQuery.addWhereEqualTo("password", pwd);
        addSubscription(bmobQuery.findObjects(new FindListener<FUser>() {

            @Override
            public void done(List<FUser> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        progressBar.setVisibility(View.GONE);
                        toast("登陆成功");
                        GreenDaoHelper.getInstance().insertUser(list.get(0));
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        loge(new BmobException(e));
                        toast("登陆失败，用户名或密码错误");
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    loge(new BmobException(e));
                    toast("登陆失败");
                }
            }
        }));

    }


}
