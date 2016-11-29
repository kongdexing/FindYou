package com.dexing.findyou.friend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.widget.view.CircularImageView;
import com.dexing.findyou.R;
import com.dexing.findyou.model.FriendResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dexing on 2016/11/29.
 * No1
 */

public class FindResultAdapter extends BaseAdapter {

    private List<FriendResult> listResult = new ArrayList<>();
    private Context mContext;

    public FindResultAdapter(Context context) {
        super();
        mContext = context;
    }

    public void setListResult(List<FriendResult> results) {
        listResult = results;
        notifyDataSetChanged();
    }

    public void clearList() {
        listResult.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return listResult.size();
    }

    @Override
    public FriendResult getItem(int i) {
        return listResult.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_find_friend, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        FriendResult result = getItem(i);
        holder.txtUserName.setText(result.getUserName());
        //（1同意，0未同意，-1未请求）
        if (result.getFriendStatus() == 1) {
            holder.btnRequest.setVisibility(View.GONE);
            holder.txtRequested.setVisibility(View.GONE);
        } else if (result.getFriendStatus() == 0) {
            holder.btnRequest.setVisibility(View.GONE);
            holder.txtRequested.setVisibility(View.VISIBLE);
        } else {
            holder.btnRequest.setVisibility(View.VISIBLE);
            holder.txtRequested.setVisibility(View.GONE);
            holder.btnRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
        return view;
    }

    class ViewHolder {
        @BindView(R.id.imgHead)
        CircularImageView headImgView;
        @BindView(R.id.txtUserName)
        TextView txtUserName;
        @BindView(R.id.btnRequest)
        Button btnRequest;
        @BindView(R.id.txtRequested)
        TextView txtRequested;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }

}
