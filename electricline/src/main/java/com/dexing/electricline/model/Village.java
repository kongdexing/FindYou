package com.dexing.electricline.model;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by dexing on 2017/5/31.
 * No1
 */

public class Village extends BmobObject implements Serializable {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
