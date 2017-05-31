package com.dexing.electricline.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by dexing on 2017/5/31.
 * No1
 */

public class Person extends BmobObject {

    private String loginName;
    private String password;
    //1 管理员（添加，删除数据），2 电工（）
    private int type;



}
