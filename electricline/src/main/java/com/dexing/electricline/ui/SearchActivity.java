package com.dexing.electricline.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.dexing.electricline.R;
import com.dexing.electricline.adapter.BoxUserAdapter;
import com.dexing.electricline.adapter.DividerItemDecoration;
import com.dexing.electricline.model.BoxUser;
import com.dexing.electricline.model.GreenDaoHelper;
import com.dexing.electricline.model.Village;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity {

    @BindView(R.id.edtVal)
    EditText edtVal;

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
                String val = edtVal.getText().toString().trim();
                queryUser(val);
                hideInputWindow(this, view);
                break;
        }
    }

    public void hideInputWindow(Context mContext, View view) {
        if (mContext == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void queryUser(String val) {
        List<BoxUser> users = GreenDaoHelper.getInstance().getUserByInfo(currentVillage.getObjectId());
        List<BoxUser> searchUsers = new ArrayList<>();
        int size = users.size();
        for (int i = 0; i < size; i++) {
            BoxUser user = users.get(i);
            if (user.getUserNum().contains(val) || user.getUserName().contains(val)) {
                searchUsers.add(user);
            }
        }

        adapter.setGoType(adapter.GoType_Search);
        adapter.loadData(searchUsers, null);

    }

}
