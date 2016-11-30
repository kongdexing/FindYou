package com.dexing.findyou.friend;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.widget.view.CircularImageView;
import com.dexing.findyou.R;
import com.dexing.findyou.bean.FUser;
import com.dexing.findyou.bean.FriendRelation;
import com.dexing.findyou.bean.GreenDaoHelper;
import com.dexing.findyou.model.FriendResult;
import com.dexing.findyou.view.BaseListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by dexing on 2016/11/29.
 * No1
 */

public class FindResultAdapter extends BaseListAdapter {

    private List<FriendResult> listResult = new ArrayList<>();

    public FindResultAdapter(Context context) {
        super(context);
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
        final ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_find_friend, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final FriendResult result = getItem(i);
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
                    holder.btnRequest.setVisibility(View.GONE);
                    holder.progress.setVisibility(View.VISIBLE);

                    Log.i(TAG, "onClick: fromUser " + GreenDaoHelper.getInstance().getCurrentUser().getUserId() + " toUser " +
                            result.getObjectId());
                    final FriendRelation myUser = new FriendRelation();
                    myUser.setFromUser(GreenDaoHelper.getInstance().getCurrentUser().getUserId());
                    myUser.setToUser(result.getObjectId());
                    myUser.setStatus(0);
                    myUser.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            holder.progress.setVisibility(View.GONE);
                            if (e == null) {
                                holder.txtRequested.setVisibility(View.VISIBLE);
                                holder.txtRequested.setText("发送成功");
                            } else {
                                Log.i(TAG, "done: error code " + e.getErrorCode() + " " + e.getMessage());
                                if (e.getErrorCode() == 100) {
                                    holder.txtRequested.setVisibility(View.VISIBLE);
                                    holder.txtRequested.setText("发送成功");
                                } else {
                                    holder.btnRequest.setVisibility(View.VISIBLE);
                                    holder.btnRequest.setText("！添加");
                                }
                            }
                        }
                    });
                }
            });
        }

        if (result.getObjectId().equals(GreenDaoHelper.getInstance().getCurrentUser().getUserId())) {
            holder.btnRequest.setVisibility(View.GONE);
            holder.txtRequested.setVisibility(View.GONE);
            holder.progress.setVisibility(View.GONE);
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
        @BindView(R.id.progress)
        ProgressBar progress;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }

}
