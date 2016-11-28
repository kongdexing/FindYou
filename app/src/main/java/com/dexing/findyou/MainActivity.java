package com.dexing.findyou;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.dexing.findyou.friend.FriendFragment;
import com.dexing.findyou.message.MessageFragment;
import com.dexing.findyou.view.MainFragmentAdapter;
import com.dexing.findyou.view.UnderlinePageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.majiajie.pagerbottomtabstrip.Controller;

public class MainActivity extends BaseActivity {

    Controller controller;
    List<Fragment> mFragments;
    @BindView(R.id.pager)
    ViewPager mPager;

    @BindView(R.id.indicator)
    UnderlinePageIndicator mIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showImgBack(false);
        initFragment();

        MainFragmentAdapter mAdapter = new MainFragmentAdapter(getSupportFragmentManager(), mFragments);
        mPager.setAdapter(mAdapter);
        mIndicator.setFades(false);
        mIndicator.setViewPager(mPager);
    }

    private void initFragment() {
        mFragments = new ArrayList<>();

        mFragments.add(new FriendFragment());
        mFragments.add(new MessageFragment());
        mFragments.add(new MessageFragment());
    }


}
