package com.dexing.findyou;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;

import com.dexing.findyou.friend.FindFriendActivity;
import com.dexing.findyou.friend.FriendFragment;
import com.dexing.findyou.message.MessageFragment;
import com.dexing.findyou.view.MainFragmentAdapter;
import com.dexing.findyou.view.UnderlinePageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

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
        showImgHead(true);

        initFragment();

        MainFragmentAdapter mAdapter = new MainFragmentAdapter(getSupportFragmentManager(), mFragments);
        mPager.setAdapter(mAdapter);
        mIndicator.setFades(false);
        mIndicator.setViewPager(mPager);
//        setFriendTip();

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    setMessageTip();
                } else if (position == 1) {
                    setFriendTip();
                } else {

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPager.setCurrentItem(1);
    }

    private void initFragment() {
        mFragments = new ArrayList<>();

        mFragments.add(new MessageFragment());
        mFragments.add(new FriendFragment());
        mFragments.add(new MessageFragment());
    }

    private void setMessageTip() {
        setTxtRight("");
        setTitle("消息");
    }

    private void setFriendTip() {
        setTxtRight("添加");
        setTitle("好友");
        setTextRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, FindFriendActivity.class));
            }
        });

    }

}
