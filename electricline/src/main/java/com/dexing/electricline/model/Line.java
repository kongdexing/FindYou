package com.dexing.electricline.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by dexing on 2017/7/12 0012.
 * No1
 */

public class Line extends BmobObject {

    private String VillageId;
    private String LinePoint;

    public String getVillageId() {
        return VillageId;
    }

    public void setVillageId(String villageId) {
        this.VillageId = villageId;
    }

    public String getLinePoint() {
        return LinePoint;
    }

    public void setLinePoint(String linePoint) {
        this.LinePoint = linePoint;
    }
}
