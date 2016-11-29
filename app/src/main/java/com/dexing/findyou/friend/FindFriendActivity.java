package com.dexing.findyou.friend;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.dexing.findyou.BaseActivity;
import com.dexing.findyou.R;
import com.dexing.findyou.bean.FUser;
import com.dexing.findyou.bean.FriendRelation;
import com.dexing.findyou.bean.GreenDaoHelper;
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
        BmobQuery<FUser> or1 = new BmobQuery<FUser>();
        or1.addWhereMatches("phoneNum", name);
        BmobQuery<FUser> or2 = new BmobQuery<FUser>();
        or1.addWhereMatches("email", name);
        BmobQuery<FUser> or3 = new BmobQuery<FUser>();
        or1.addWhereMatches("nickName", name);
        List<BmobQuery<FUser>> ors = new ArrayList<BmobQuery<FUser>>();
        ors.add(or1);
        ors.add(or2);
        ors.add(or3);

        BmobQuery<FUser> mainQuery = new BmobQuery<FUser>();
        mainQuery.or(ors);
        mainQuery.findObjects(new FindListener<FUser>() {
            @Override
            public void done(List<FUser> users, BmobException e) {
                if (e == null) {
                    if (users.size() == 0) {
                        toast("暂无该用户信息，请重新搜索");
                        adapter.clearList();
                        return;
                    }
                    //去好友关系中查找
                    StringBuilder findIds = new StringBuilder();
                    for (int i = 0; i < users.size(); i++) {
                        FUser user = users.get(i);
                        findIds.append(user.getObjectId() + ",");
                        FriendResult result = new FriendResult();
                        result.setObjectId(user.getObjectId());
                        result.setUserName(user.getNickName());
                        result.setBirthday(user.getBirthday());
                        result.setHeadImg(user.getHeadImg());
                        result.setSex(user.getSex());
                        result.setFriendStatus(-1);
                        listResult.add(result);
                    }
                    queryFriendRelation(GreenDaoHelper.getInstance().getCurrentUser().getObjectId(),
                            findIds.substring(findIds.length() - 1).toString());
                } else {
                    adapter.clearList();
                    toast(e.getMessage());
                    loge(e);
                }
            }

        });

    }

    private void queryFriendRelation(String fromUser, String toUsers) {
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
