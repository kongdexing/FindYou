package com.dexing.electricline.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dexing.electricline.R;
import com.dexing.electricline.adapter.BoxUserAdapter;
import com.dexing.electricline.adapter.DividerItemDecoration;
import com.dexing.electricline.model.BoxUser;
import com.dexing.electricline.model.Village;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

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
        BmobQuery<BoxUser> bmobQuery = new BmobQuery<BoxUser>();
        bmobQuery.addWhereEqualTo("VillageId", currentVillage.getObjectId());
        Log.i(TAG, "queryUser: villageId  "+currentVillage.getObjectId());
        if (!num.isEmpty()) {
            bmobQuery.addWhereEqualTo("userNum", num);
        }
        if (!name.isEmpty()) {
            bmobQuery.addWhereEqualTo("userName", name);
        }

//        BmobQuery<BoxUser> bmobQuery1 = new BmobQuery<BoxUser>();
//        bmobQuery1.addWhereEqualTo("userNum", num);
//        BmobQuery<BoxUser> bmobQuery2 = new BmobQuery<BoxUser>();
//        bmobQuery2.addWhereEqualTo("userName", name);
//        List<BmobQuery<BoxUser>> listQuery = new ArrayList<BmobQuery<BoxUser>>();
//        listQuery.add(bmobQuery1);
//        listQuery.add(bmobQuery2);
//        bmobQuery.or(listQuery);
        bmobQuery.findObjects(new FindListener<BoxUser>() {
            @Override
            public void done(List<BoxUser> list, BmobException e) {
                if (e == null) {
                    Toast.makeText(SearchActivity.this, list.size(), Toast.LENGTH_SHORT).show();
                    adapter.setGoType(adapter.GoType_Search);
                    adapter.loadData(list);
                } else {
                    Toast.makeText(SearchActivity.this, e.getErrorCode() + " -- " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

//        String sql = String.format("select * from BoxUser where VillageId = %s and (userNum like %%s% or userName like %%s%)",
//                currentVillage.getObjectId(), num, name);
//        log(sql);
//        bmobQuery.setSQL(sql);
//        bmobQuery.doSQLQuery(sql, new SQLQueryListener<BoxUser>() {
//            @Override
//            public void done(BmobQueryResult<BoxUser> bmobQueryResult, BmobException e) {
//                if (e == null) {
//                    Toast.makeText(SearchActivity.this, bmobQueryResult.getResults().size(), Toast.LENGTH_SHORT).show();
//                    adapter.setGoType(adapter.GoType_Search);
//                    adapter.loadData(bmobQueryResult.getResults());
//                } else {
//                    Toast.makeText(SearchActivity.this, e.getErrorCode() + " -- " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });


    }

}
