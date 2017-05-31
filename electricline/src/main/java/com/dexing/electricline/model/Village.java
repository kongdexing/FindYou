package com.dexing.electricline.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by dexing on 2017/5/31.
 * No1
 */

public class Village extends BmobObject {

    private String name;
    private String village_id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVillage_id() {
        return village_id;
    }

    public void setVillage_id(String village_id) {
        this.village_id = village_id;
    }
}
