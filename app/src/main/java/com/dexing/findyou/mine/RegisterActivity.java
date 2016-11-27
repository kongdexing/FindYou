package com.dexing.findyou.mine;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dexing.findyou.BaseActivity;
import com.dexing.findyou.R;
import com.dexing.findyou.bean.User;
import com.dexing.findyou.util.CommonUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import rx.Subscriber;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.edtPhone)
    EditText edtPhone;

    @BindView(R.id.edtEmail)
    EditText edtEmail;

    @BindView(R.id.edtPwd)
    EditText edtPwd;

    @BindView(R.id.edtPwdAgain)
    EditText edtPwdAgain;

    @BindView(R.id.progress)
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @OnClick({R.id.imgDelPhone, R.id.imgDelEmail, R.id.btnRegister})
    void viewOnClick(View view) {
        switch (view.getId()) {
            case R.id.imgDelPhone:
                edtPhone.setText("");
                edtPhone.requestFocus();
                break;
            case R.id.imgDelEmail:
                edtEmail.setText("");
                edtEmail.requestFocus();
                break;
            case R.id.btnRegister:
                //验证手机号，邮箱，密码
                String phone = edtPhone.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String pwd = edtPwd.getText().toString().trim();
                String pwdAgain = edtPwdAgain.getText().toString().trim();
                if (phone.isEmpty() || email.isEmpty() || pwd.isEmpty() || pwdAgain.isEmpty()) {
                    Toast.makeText(this, R.string.toast_input_empty, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!CommonUtil.isPhone(phone)) {
                    Toast.makeText(this, R.string.toast_phone_error, Toast.LENGTH_SHORT).show();
                    edtPhone.setSelection(phone.length());
                    edtPhone.setFocusable(true);
                    return;
                }
                if (!CommonUtil.isEmail(email)) {
                    Toast.makeText(this, R.string.toast_email_error, Toast.LENGTH_SHORT).show();
                    edtEmail.setSelection(email.length());
                    edtEmail.setFocusable(true);
                    return;
                }

                if (!pwd.equals(pwdAgain)) {
                    Toast.makeText(this, R.string.toast_pwd_notequal, Toast.LENGTH_SHORT).show();
                    edtPwd.setSelection(pwd.length());
                    return;
                }
                toSignUp(phone, email, pwd);
                break;
        }
    }

    @SuppressLint("UseValueOf")
    private void toSignUp(final String phone, String email, String pwd) {
        final User myUser = new User();
        myUser.setUsername(phone);
        myUser.setMobilePhoneNumber(phone);
        myUser.setEmail(email);
        myUser.setPassword(pwd);
        progress.setVisibility(View.VISIBLE);

        //判断用户是否存在
        BmobQuery<User> bmobQuery = new BmobQuery<User>();
        bmobQuery.addWhereEqualTo("username", phone);
        //先判断是否有缓存
        boolean isCache = bmobQuery.hasCachedResult(User.class);
        if (isCache) {
            bmobQuery.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 先从缓存取数据，如果没有的话，再从网络取。
        } else {
            bmobQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则先从网络中取
        }

        bmobQuery.findObjectsObservable(User.class)
                .subscribe(new Subscriber<List<User>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        progress.setVisibility(View.GONE);
                        toast("注册失败");
                        loge(e);
                    }

                    @Override
                    public void onNext(List<User> persons) {
                        if (persons.size() == 0) {
                            insertUser(myUser);
                        } else {
                            progress.setVisibility(View.GONE);
                            toast(R.string.toast_exist_user);
                            edtPhone.requestFocus();
                            edtPhone.setSelection(phone.length());
                        }
                    }
                });
    }

    private void insertUser(User myUser) {
        addSubscription(myUser.signUp(new SaveListener<User>() {
            @Override
            public void done(User s, BmobException e) {
                progress.setVisibility(View.GONE);
                if (e == null) {
                    toast("注册成功:" + s.toString());
                } else {
                    loge(e);
                }
            }
        }));
    }

}
