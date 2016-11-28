package com.dexing.findyou.mine;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dexing.findyou.BaseActivity;
import com.dexing.findyou.R;
import com.dexing.findyou.bean.GreenDaoHelper;
import com.dexing.findyou.bean.SmsSendStatus;
import com.dexing.findyou.bean.User;
import com.dexing.findyou.util.CommonUtil;
import com.dexing.findyou.util.SharedPreferencesUtil;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import rx.Subscriber;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.edtPhone)
    EditText edtPhone;

    @BindView(R.id.txtSendStatus)
    TextView txtSendStatus;

    @BindView(R.id.edtCode)
    EditText edtCode;

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

    @OnClick({R.id.imgDelPhone, R.id.imgDelEmail, R.id.btnRegister, R.id.txtSendStatus})
    void viewOnClick(View view) {
        switch (view.getId()) {
            case R.id.imgDelPhone:
                edtPhone.setText("");
                edtPhone.requestFocus();
                break;
            case R.id.txtSendStatus:
                String phone = edtPhone.getText().toString().trim();
                if (phone.isEmpty() || !CommonUtil.isPhone(phone)) {
                    Toast.makeText(this, R.string.toast_phone_error, Toast.LENGTH_SHORT).show();
                    return;
                }
                //验证发送次数
                txtSendStatus.setText("正在发送");
                analyseSendTime(phone);

                break;
            case R.id.btnRegister:
                //验证手机号，邮箱，密码
                phone = edtPhone.getText().toString().trim();
                String email = edtCode.getText().toString().trim();
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

    private void analyseSendTime(final String phone) {
        //判断用户是否存在
        BmobQuery<SmsSendStatus> bmobQuery = new BmobQuery<SmsSendStatus>();
        bmobQuery.addWhereEqualTo("phone", phone);
        addSubscription(bmobQuery.findObjects(new FindListener<SmsSendStatus>() {
            @Override
            public void done(List<SmsSendStatus> sendStatuses, BmobException e) {
                if (e == null) {
                    SmsSendStatus status = null;
                    if (sendStatuses.size() == 1) {
                        status = sendStatuses.get(0);
                    }
                    //获取当前时间，判断时间是不是当天
                    Date currentDate = new java.util.Date();

                    if (status == null || (currentDate.getDay() != status.getUpdateTime().getDay()) ||
                            (currentDate.getDay() == status.getUpdateTime().getDay() && status.getSendTimes() < 3)) {
                        //修改数据库
                        SmsSendStatus newStatus = new SmsSendStatus();
                        if (currentDate.getDay() != status.getUpdateTime().getDay()) {
                            newStatus.setUpdateTime(currentDate);
                            newStatus.setSendTimes(1);
                        } else {
                            newStatus.setSendTimes(status.getSendTimes() + 1);
                        }
                        newStatus.update(status.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    //更新数据成功，发送短信
                                    SendSMSCode(phone);
                                }
                            }
                        });

                    } else {
                        //超过次数
                        toast(R.string.toast_sms_max);
                        txtSendStatus.setVisibility(View.GONE);
                    }
                } else {
                    txtSendStatus.setText("验证码");
                    toast("验证码失败 " + e.getMessage());
                    loge(e);
                }
            }
        }));
    }

    private void SendSMSCode(String phone) {
        BmobSMS.requestSMSCode(phone, "找你短信", new QueryListener<Integer>() {
            @Override
            public void done(Integer smsId, BmobException ex) {
                if (ex == null) {//验证码发送成功
                    log("短信id：" + smsId);
                    //
                } else {

                }
            }
        });
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
        addSubscription(bmobQuery.findObjects(new FindListener<User>() {

            @Override
            public void done(List<User> persons, BmobException e) {
                if (e == null) {
                    if (persons.size() == 0) {
                        insertUser(myUser);
                    } else {
                        progress.setVisibility(View.GONE);
                        toast(R.string.toast_exist_user);
                        edtPhone.requestFocus();
                        edtPhone.setSelection(phone.length());
                    }
                } else {
                    progress.setVisibility(View.GONE);
                    toast("注册失败");
                    loge(e);
                }
            }
        }));
    }

    private void insertUser(final User myUser) {
        addSubscription(myUser.signUp(new SaveListener<User>() {
            @Override
            public void done(User s, BmobException e) {
                progress.setVisibility(View.GONE);
                if (e == null) {
                    toast("注册成功");
                    //保存数据
                    GreenDaoHelper.getInstance().insertUser(myUser);
                    SharedPreferencesUtil.saveData(RegisterActivity.this, SharedPreferencesUtil.KEY_PWD, edtPwd.getText().toString().trim());
                    setResult(1);
                    finish();
                } else {
                    toast("注册失败");
                    loge(e);
                }
            }
        }));
    }

}
