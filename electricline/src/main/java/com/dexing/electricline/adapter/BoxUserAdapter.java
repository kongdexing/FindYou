package com.dexing.electricline.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dexing.electricline.R;
import com.dexing.electricline.model.BoxUser;
import com.dexing.electricline.model.EPoint;
import com.dexing.electricline.model.Village;
import com.dexing.electricline.ui.BoxUserActivity;
import com.dexing.electricline.ui.DrawLineActivity;
import com.dexing.electricline.ui.UserEditActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dexing on 2017/6/1.
 * No1
 */

public class BoxUserAdapter extends RecyclerView.Adapter {

    private List<BoxUser> listUsers = new ArrayList<>();
    private EPoint currentPoint = null;
    private Context mContext;

    public BoxUserAdapter(Context context) {
        this.mContext = context;
    }

    //  添加数据
    public void loadData(List<BoxUser> boxUsers, EPoint point) {
        listUsers = boxUsers;
        currentPoint = point;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_boxuser, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final BoxUser boxUser = listUsers.get(position);
        if (boxUser == null || currentPoint == null) {
            return;
        }
        final MyViewHolder viewHolder = (MyViewHolder) holder;
        viewHolder.txtPointNum.setText("表箱编号：" + currentPoint.getNumber());
        viewHolder.txtUserNum.setText("用户编号：" + boxUser.getUserNum());
        viewHolder.txtUserName.setText("用户名：" + boxUser.getUserName());
        viewHolder.txtUserPhone.setText("联系方式：" + boxUser.getUserPhone());
        viewHolder.txtMark.setText("备注：" + boxUser.getMark());

        viewHolder.llContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UserEditActivity.class);
                intent.putExtra("user", boxUser);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listUsers == null ? 0 : listUsers.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.llContent)
        LinearLayout llContent;

        @BindView(R.id.txtPointNum)
        TextView txtPointNum;
        @BindView(R.id.txtUserNum)
        TextView txtUserNum;

        @BindView(R.id.txtUserName)
        TextView txtUserName;

        @BindView(R.id.txtUserPhone)
        TextView txtUserPhone;

        @BindView(R.id.txtMark)
        TextView txtMark;


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}