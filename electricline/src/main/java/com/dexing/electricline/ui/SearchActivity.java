package com.dexing.electricline.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dexing.electricline.R;
import com.dexing.electricline.adapter.BoxUserAdapter;
import com.dexing.electricline.adapter.DividerItemDecoration;
import com.dexing.electricline.model.BoxUser;
import com.dexing.electricline.model.GreenDaoHelper;
import com.dexing.electricline.model.Village;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity {

    @BindView(R.id.edtNum)
    EditText edtNum;

    @BindView(R.id.edtName)
    EditText edtName;

    @BindView(R.id.recycleView)
    RecyclerView recycleView;

    BoxUserAdapter adapter;
    Village currentVillage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            try {
                currentVillage = (Village) bundle.getSerializable("village");
            } catch (Exception ex) {

            }
        }
        initRecyclerView(recycleView);
    }

    private void initRecyclerView(RecyclerView recyclerView) {
        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new DividerItemDecoration(this,
                    LinearLayoutManager.VERTICAL, R.drawable.line_dotted));
        }

        adapter = new BoxUserAdapter(this);
        this.recycleView.setAdapter(adapter);
    }

    @OnClick({R.id.btnOK})
    void viewOnClick(View view) {
        switch (view.getId()) {
            case R.id.btnOK:
                String num = edtNum.getText().toString().trim();
                String name = edtName.getText().toString().trim();
                if (num.isEmpty() && name.isEmpty()) {
                    Toast.makeText(this, "请输入相关检索信息", Toast.LENGTH_SHORT).show();
                    return;
                }
                queryUser(num, name);
                break;
        }
    }

    private void queryUser(String num, String name) {
        List<BoxUser> users = GreenDaoHelper.getInstance().getUserByInfo(currentVillage.getObjectId(), num, name);
        Toast.makeText(this, "query User size " + users.size(), Toast.LENGTH_SHORT).show();
        adapter.setGoType(adapter.GoType_Search);
        adapter.loadData(users, null);

    }

}
