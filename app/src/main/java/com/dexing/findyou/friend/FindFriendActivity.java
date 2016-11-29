package com.dexing.findyou.friend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.dexing.findyou.BaseActivity;
import com.dexing.findyou.MainActivity;
import com.dexing.findyou.R;
import com.dexing.findyou.bean.FUser;
import com.dexing.findyou.bean.FriendRelation;
import com.dexing.findyou.bean.GreenDaoHelper;
import com.dexing.findyou.mine.LoginActivity;
import com.dexing.findyou.model.FriendResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SQLQueryListener;

public class FindFriendActivity extends BaseActivity {

    @BindView(R.id.listview)
    ListView listview;

    @BindView(R.id.edtFriend)
    EditText edtFriend;

    FindResultAdapter adapter;
    private List<FriendResult> listResult = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend);

        adapter = new FindResultAdapter(this);
        listview.setAdapter(adapter);
    }

    @OnClick({R.id.btnSearch})
    void viewOnClick(View view) {
        switch (view.getId()) {
            case R.id.btnSearch:
                String name = edtFriend.getText().toString();
                if (name.isEmpty()) {
                    toast(R.string.toast_friendname_empty);
                    return;
                }
                queryFriends(name);
                break;
        }
    }

    private void queryFriends(String name) {
        listResult.clear();
        BmobQuery<FUser> bmobQuery = new BmobQuery<FUser>();
        bmobQuery.addWhereEqualTo("phoneNum", name);
//        bmobQuery.addWhereContains("email", name);
//        bmobQuery.addWhereContains("nickName", name);
        addSubscription(bmobQuery.findObjects(new FindListener<FUser>() {

            @Override
            public void done(List<FUser> users, BmobException e) {
                if (e == null) {
                    if (users.size() == 0) {
                        toast("暂无该用户信息，请重新搜索");
                        adapter.clearList();
                        return;
                    }
                    //去好友关系中查找
//                    StringBuilder findIds = new StringBuilder();
                    List<String> listIds = new ArrayList<String>();

                    for (int i = 0; i < users.size(); i++) {
                        FUser user = users.get(i);
//                        findIds.append(user.getObjectId() + ",");
                        listIds.add(user.getObjectId());
                        FriendResult result = new FriendResult();
                        result.setObjectId(user.getObjectId());
                        result.setUserName(user.getNickName());
                        result.setBirthday(user.getBirthday());
                        result.setHeadImg(user.getHeadImg());
                        result.setSex(user.getSex());
                        result.setFriendStatus(-1);
                        listResult.add(result);
                    }
//                    Log.i(TAG, "done: " + findIds);
                    queryFriendRelation(GreenDaoHelper.getInstance().getCurrentUser().getUserId(), listIds);
                } else {
                    adapter.clearList();
                    toast(e.getMessage());
                    loge(e);
                }
            }
        }));
    }

    private void queryFriendRelation(String fromUser, List<String> toUsers) {

        BmobQuery<FriendRelation> relationQuery = new BmobQuery<FriendRelation>();
        relationQuery.addWhereContains("fromUser", fromUser);
        relationQuery.addWhereContainedIn("toUser", toUsers);
        addSubscription(relationQuery.findObjects(new FindListener<FriendRelation>() {
            @Override
            public void done(List<FriendRelation> listFriend, BmobException e) {
                if (e == null) {
                    if (listFriend.size() == 0) {
                        for (int i = 0; i < listFriend.size(); i++) {
                            FriendRelation relation = listFriend.get(i);
                            for (int j = 0; j < listResult.size(); j++) {
                                if (relation.getToUser().equals(listResult.get(j).getObjectId())) {
                                    //修改好友关系状态
                                    listResult.get(j).setFriendStatus(relation.getStatus());
                                    break;
                                }
                            }
                        }
                    }
                }
                bindResult();
            }
        }));


        if (true)
            return;

        String sql = String.format("select * from FriendRelation where fromUser = %s and toUser in (%s)", fromUser, toUsers);
        log(sql);
        BmobQuery<FriendRelation> mainQuery = new BmobQuery<FriendRelation>();
        mainQuery.setSQL(sql);
        mainQuery.doSQLQuery(sql, new SQLQueryListener<FriendRelation>() {

            @Override
            public void done(BmobQueryResult<FriendRelation> bmobQueryResult, BmobException e) {
                if (e == null) {
                    List<FriendRelation> listFriend = bmobQueryResult.getResults();
                    if (listFriend.size() == 0) {
                        for (int i = 0; i < listFriend.size(); i++) {
                            FriendRelation relation = listFriend.get(i);
                            for (int j = 0; j < listResult.size(); j++) {
                                if (relation.getToUser().equals(listResult.get(j).getObjectId())) {
                                    //修改好友关系状态
                                    listResult.get(j).setFriendStatus(relation.getStatus());
                                    break;
                                }
                            }
                        }
                    }
                }
                bindResult();
            }
        });
    }

    private void bindResult() {
        adapter.setListResult(listResult);
    }

}
