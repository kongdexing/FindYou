package com.dexing.electricline.ui;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.dexing.electricline.R;
import com.dexing.electricline.adapter.DividerItemDecoration;
import com.dexing.electricline.adapter.VillageAdapter;
import com.dexing.electricline.model.Village;
import com.dexing.electricline.view.CustomEditDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

//
public class VillageListActivity extends BaseActivity {

    @BindView(R.id.floatingActionButton)
    FloatingActionButton floatingActionButton;

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refresh_layout;

    @BindView(R.id.recycleView)
    RecyclerView recycleView;

    VillageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_village_list);
        setTitle("");

        initRecyclerView(recycleView, refresh_layout);
        initData();
    }

    private void initData() {
        BmobQuery<Village> bmobQuery = new BmobQuery<Village>();
        bmobQuery.findObjects(new FindListener<Village>() {
            @Override
            public void done(List<Village> list, BmobException e) {
                adapter.loadData(list);
            }
        });
    }

    public void initRecyclerView(RecyclerView recyclerView, final SwipeRefreshLayout swipeRefreshLayout) {
        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new DividerItemDecoration(this,
                    LinearLayoutManager.VERTICAL, R.drawable.line_dotted));
        }
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.google_colors));
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    initData();
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        }

        adapter = new VillageAdapter(this);
        this.recycleView.setAdapter(adapter);
    }

    @OnClick({R.id.floatingActionButton})
    public void viewOnClick(View view) {
        switch (view.getId()) {
            case R.id.floatingActionButton:
                CustomEditDialog dialog = new CustomEditDialog(VillageListActivity.this);
                dialog.setTitle("村庄名称");
                dialog.setHintEdit("请输入村名");
                dialog.setAlertDialogClickListener(new CustomEditDialog.DialogClickListener() {
                    @Override
                    public void onPositiveClick(String value) {
                        if (value.isEmpty()) {
                            Toast.makeText(VillageListActivity.this, "名称不可为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        addVillage(value);
                    }
                });
                break;
        }
    }

    private void addVillage(String village_name) {
        final Village village = new Village();
        village.setName(village_name);
        village.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    village.setVillage_id(objectId);
                    toast("添加数据成功，返回objectId为：" + objectId);
                    adapter.addData(village);
                } else {
                    toast("创建数据失败：" + e.getMessage());
                }
            }
        });
    }

}
