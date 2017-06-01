package com.dexing.electricline.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dexing.electricline.R;
import com.dexing.electricline.model.Village;
import com.dexing.electricline.ui.DrawLineActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dexing on 2017/5/31.
 * No1
 */

public class VillageAdapter extends RecyclerView.Adapter {

    private List<Village> listVillage = new ArrayList<>();
    private Context mContext;

    public VillageAdapter(Context context) {
        this.listVillage = listVillage;
        this.mContext = context;
    }

    //  添加数据
    public void loadData(List<Village> villages) {
        listVillage = villages;
        notifyDataSetChanged();
    }

    //  添加数据
    public void addData(Village village) {
        listVillage.add(listVillage.size(), village);
        notifyItemInserted(listVillage.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_village, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Village village = listVillage.get(position);
        if (village == null) {
            return;
        }
        final MyViewHolder viewHolder = (MyViewHolder) holder;
        viewHolder.txtName.setText(village.getName());
        viewHolder.txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, village.getName() + "" + village.getObjectId(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, DrawLineActivity.class);
                intent.putExtra("village", village);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listVillage.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtName)
        TextView txtName;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
