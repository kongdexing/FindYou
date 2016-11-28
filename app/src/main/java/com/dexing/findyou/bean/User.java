package com.dexing.findyou.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

/**
 * Created by dexing on 2016/11/27.
 * No1
 */

public class User extends BmobUser{

    private boolean gpsPush = true;

    public boolean isGpsPush() {
        return gpsPush;
    }

    public void setGpsPush(boolean gpsPush) {
        this.gpsPush = gpsPush;
    }


}
