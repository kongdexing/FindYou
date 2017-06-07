package com.dexing.electricline.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dexing.electricline.R;
import com.dexing.electricline.model.BoxUser;
import com.dexing.electricline.model.EPoint;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class UserEditActivity extends BaseActivity {

    @BindView(R.id.edtNum)
    EditText edtNum;

    @BindView(R.id.edtPropertyNum)
    EditText edtPropertyNum;

    @BindView(R.id.edtName)
    EditText edtName;

    @BindView(R.id.edtPhone)
    EditText edtPhone;

    @BindView(R.id.edtMark)
    EditText edtMark;

    @BindView(R.id.btnDel)
    Button btnDel;

    private EPoint currentPoint;
    private BoxUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);
        setTitle("用户信息");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            try {
                currentUser = (BoxUser) bundle.getSerializable("user");
            } catch (Exception ex) {

            }
            try {
                currentPoint = (EPoint) bundle.getSerializable("point");
            } catch (Exception ex) {

            }
        }

        if (currentUser != null) {
            edtNum.setText(currentUser.getUserNum());
            edtName.setText(currentUser.getUserName());
            edtPhone.setText(currentUser.getUserPhone());
            edtMark.setText(currentUser.getMark());
            btnDel.setVisibility(View.VISIBLE);
        } else {
            btnDel.setVisibility(View.GONE);
        }

    }

    @OnClick({R.id.btnOK, R.id.btnDel})
    void viewOnClick(View view) {
        switch (view.getId()) {
            case R.id.btnOK:
                String num = edtNum.getText().toString().trim();
                String property = edtPropertyNum.getText().toString().trim();
                String name = edtName.getText().toString().trim();
                String phone = edtPhone.getText().toString().trim();
                String mark = edtMark.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(this, "用户名不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (currentUser == null) {
                    BoxUser user = new BoxUser();
                    user.setEPointId(currentPoint.getObjectId());
                    user.setVillageId(currentPoint.getVillageId());
                    user.setUserNum(num);
                    user.setPropertyNum(property);
                    user.setUserName(name);
                    user.setUserPhone(phone);
                    user.setMark(mark);
                    user.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                Toast.makeText(UserEditActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(UserEditActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    currentUser.setVillageId(currentPoint.getVillageId());
                    currentUser.setUserNum(num);
                    currentUser.setPropertyNum(property);
                    currentUser.setUserName(name);
                    currentUser.setUserPhone(phone);
                    currentUser.setMark(mark);
                    currentUser.update(currentPoint.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(UserEditActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(UserEditActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                break;
            case R.id.btnDel:
                if (currentUser == null) {
                    return;
                }
                currentUser.delete(currentUser.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(UserEditActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(UserEditActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
        }
    }

}
