package com.dexing.electricline.model;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by dexing on 2017/6/1.
 * No1
 */

public class BoxUser extends BmobObject implements Serializable{

    private String EPointId;
    private String VillageId;
    private String userNum;
    private String propertyNum;
    private String userName;
    private String userPhone;
    private String mark;

    public String getEPointId() {
        return EPointId;
    }

    public void setEPointId(String EPointId) {
        this.EPointId = EPointId;
    }

    public String getVillageId() {
        return VillageId;
    }

    public void setVillageId(String villageId) {
        VillageId = villageId;
    }

    public String getUserNum() {
        return userNum;
    }

    public void setUserNum(String userNum) {
        this.userNum = userNum;
    }

    public String getPropertyNum() {
        return propertyNum;
    }

    public void setPropertyNum(String propertyNum) {
        this.propertyNum = propertyNum;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
}
