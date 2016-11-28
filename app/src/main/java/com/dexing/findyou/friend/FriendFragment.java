package com.dexing.findyou.friend;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dexing.findyou.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FriendFragment extends Fragment {

    private Unbinder unbinder;

    public FriendFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_friend, container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        return mRootView;
    }
}
