package com.dexing.findyou.bean;

import org.greenrobot.greendao.annotation.Entity;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by dexing on 2016/11/27.
 * No1
 */
@Entity
public class User extends BmobUser{

    private boolean gpsPush = true;
    //头像，昵称，性别，出生日期，

    @Generated(hash = 785176253)
    public User(boolean gpsPush) {
        this.gpsPush = gpsPush;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public boolean isGpsPush() {
        return gpsPush;
    }

    public void setGpsPush(boolean gpsPush) {
        this.gpsPush = gpsPush;
    }

    public boolean getGpsPush() {
        return this.gpsPush;
    }


}
