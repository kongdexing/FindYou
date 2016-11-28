package com.dexing.findyou.mine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dexing.findyou.BaseActivity;
import com.dexing.findyou.R;
import com.dexing.findyou.util.CommonUtil;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class ForgetPwdActivity extends BaseActivity {

    @BindView(R.id.edtEmail)
    EditText edtEmail;

    @BindView(R.id.progress)
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
    }

    @OnClick({R.id.imgDelEmail, R.id.btnForget})
    void viewOnClick(View view) {
        switch (view.getId()) {
            case R.id.imgDelEmail:
                edtEmail.setText("");
                edtEmail.requestFocus();
                break;
            case R.id.btnForget:
                String email = edtEmail.getText().toString().trim();
                if (!CommonUtil.isEmail(email)) {
                    Toast.makeText(this, R.string.toast_email_error, Toast.LENGTH_SHORT).show();
                    edtEmail.setSelection(email.length());
                    edtEmail.setFocusable(true);
                    return;
                }
                toResetPassword(email);
                break;
        }
    }

    /**
     * 重置密码
     */
    private void toResetPassword(final String email) {
        progress.setVisibility(View.VISIBLE);

        addSubscription(BmobUser.resetPasswordByEmail(email, new UpdateListener() {

            @Override
            public void done(BmobException e) {
                progress.setVisibility(View.GONE);
                if (e == null) {
                    toast("重置密码请求成功，请到" + email + "邮箱进行密码重置操作");
                    finish();
                } else {
                    loge(e);
                }
            }
        }));
    }

}
