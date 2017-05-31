package com.dexing.electricline.ui;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.dexing.electricline.R;
import com.dexing.electricline.adapter.DividerItemDecoration;
import com.dexing.electricline.adapter.VillageAdapter;
import com.dexing.electricline.view.CustomEditDialog;

import butterknife.BindView;
import butterknife.OnClick;

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

        initView();
        initRecyclerView(recycleView, refresh_layout);
    }

    private void initView() {

    }

    public void initRecyclerView(RecyclerView recyclerView, SwipeRefreshLayout swipeRefreshLayout) {
        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new DividerItemDecoration(this,
                    LinearLayoutManager.VERTICAL, R.drawable.line_dotted));
        }
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.google_colors));
        }
    }

    @OnClick(R.id.floatingActionButton)
    void viewOnClick(View view) {
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


                    }
                });
                break;
        }
    }

}
