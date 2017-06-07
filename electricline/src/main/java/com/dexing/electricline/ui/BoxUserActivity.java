package com.dexing.electricline.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dexing.electricline.R;
import com.dexing.electricline.adapter.BoxUserAdapter;
import com.dexing.electricline.adapter.DividerItemDecoration;
import com.dexing.electricline.model.BoxUser;
import com.dexing.electricline.model.EPoint;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class BoxUserActivity extends BaseActivity {

    private EPoint currentPoint;
    @BindView(R.id.edtNum)
    EditText edtNum;

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refresh_layout;
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    @BindView(R.id.floatingActionButton)
    FloatingActionButton floatingActionButton;
    private BoxUserAdapter adapter;
    @BindView(R.id.progress)
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box_user);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            currentPoint = (EPoint) bundle.getSerializable("point");
        }
        if (currentPoint != null) {
            setTitle(currentPoint.getNumber());
            edtNum.setText(currentPoint.getNumber());
            edtNum.setSelection(edtNum.getText().length());
            initRecyclerView(recycleView, refresh_layout);
        } else {
            Toast.makeText(this, "数据参数错误", Toast.LENGTH_SHORT).show();
            finish();
        }
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

        adapter = new BoxUserAdapter(this);
        this.recycleView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        BmobQuery<BoxUser> bmobQuery = new BmobQuery<BoxUser>();
        bmobQuery.addWhereEqualTo("EPointId", currentPoint.getObjectId());
        bmobQuery.findObjects(new FindListener<BoxUser>() {
            @Override
            public void done(List<BoxUser> list, BmobException e) {
                adapter.loadData(list);
            }
        });
    }

    @OnClick({R.id.floatingActionButton, R.id.btnOK})
    void viewOnClick(View view) {
        switch (view.getId()) {
            case R.id.floatingActionButton:
                Intent intent = new Intent(BoxUserActivity.this, UserEditActivity.class);
                intent.putExtra("point", currentPoint);
                startActivity(intent);
                break;
            case R.id.btnOK:
                String number = edtNum.getText().toString().trim();
                if (number.isEmpty()) {
                    Toast.makeText(this, "请将资料输入完整", Toast.LENGTH_SHORT).show();
                    return;
                }
                currentPoint.setNumber(number);
                progress.setVisibility(View.VISIBLE);

                currentPoint.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        progress.setVisibility(View.GONE);
                        if (e == null) {
                            Toast.makeText(BoxUserActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(BoxUserActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                break;
        }
    }


}
