package com.dexing.electricline.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dexing.electricline.R;
import com.dexing.electricline.model.Village;
import com.dexing.electricline.view.CustomDialog;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class VillageEditActivity extends BaseActivity {

    @BindView(R.id.edtName)
    EditText edtName;

    private Village village;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_village_edit);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            try {
                village = (Village) bundle.getSerializable("village");
                setTitle(village.getName());
                edtName.setText(village.getName());
                edtName.setSelection(edtName.getText().length());
            } catch (Exception ex) {

            }
        }

    }

    @OnClick({R.id.btnOK, R.id.btnDel, R.id.btnDetail})
    void viewOnClick(View view) {
        switch (view.getId()) {
            case R.id.btnOK:
                String name = edtName.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(VillageEditActivity.this, "名称不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                village.setName(name);
                village.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(VillageEditActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(VillageEditActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.btnDel:
                CustomDialog dialog = new CustomDialog(VillageEditActivity.this);
                dialog.setTitle("删除提醒");
                dialog.setMessage(R.string.msg_delete_village);
                dialog.setAlertDialogClickListener(new CustomDialog.DialogClickListener() {
                    @Override
                    public void onPositiveClick() {
                        village.delete(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Toast.makeText(VillageEditActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(VillageEditActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                break;
            case R.id.btnDetail:
                Intent intent = new Intent(VillageEditActivity.this, DrawLineActivity.class);
                intent.putExtra("village", village);
                startActivity(intent);
                finish();
                break;
        }
    }

}
