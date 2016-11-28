package com.dexing.findyou.friend;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.dexing.findyou.BaseActivity;
import com.dexing.findyou.R;
import com.dexing.findyou.bean.FriendRelation;
import com.dexing.findyou.bean.User;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend);
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
        BmobQuery<User> or1 = new BmobQuery<User>();
        or1.addWhereMatches("mobilePhoneNumber", name);
        BmobQuery<User> or2 = new BmobQuery<User>();
        or2.addWhereMatches("email", name);
        List<BmobQuery<User>> ors = new ArrayList<BmobQuery<User>>();
        ors.add(or1);
        ors.add(or2);
        BmobQuery<User> mainQuery = new BmobQuery<User>();
        mainQuery.or(ors);
        mainQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> users, BmobException e) {
                if (e == null) {
                    if (users.size() == 0) {
                        toast("暂无该用户信息，请重新搜索");
                        return;
                    }
                    //去好友关系中查找

                    toast("年龄为29或者6岁人的个数：" + users.size());
                } else {
                    loge(e);
                }
            }

        });

    }

    private void queryFriendRelation(String fromUser, String toUsers) {
        String sql = String.format("select * from FriendRelation where fromUser = %s and toUser in (%s)", fromUser, toUsers);

        BmobQuery<FriendRelation> mainQuery = new BmobQuery<FriendRelation>();
        mainQuery.setSQL(sql);
        mainQuery.doSQLQuery(sql, new SQLQueryListener<FriendRelation>() {

            @Override
            public void done(BmobQueryResult<FriendRelation> bmobQueryResult, BmobException e) {

            }
        });
    }

}
