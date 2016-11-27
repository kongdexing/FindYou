package com.dexing.findyou.mine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.dexing.findyou.BaseActivity;
import com.dexing.findyou.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.edtAccount)
    EditText edtAccount;

    @BindView(R.id.edtPwd)
    EditText edtPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @OnClick({R.id.btnLogin, R.id.btnRegister})
    void viewOnClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:

                break;
            case R.id.btnRegister:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
//                finish();
                break;
        }
    }


}
