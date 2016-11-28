package com.dexing.findyou.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by dexing on 2016/11/28.
 * No1
 */

public class FriendRelation extends BmobObject {

    private String fromUser;
    private String toUser;
    private int status;
    private String groupName;
    private boolean isCare;
    private boolean visiableGPS;
    private String remark;

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public boolean isCare() {
        return isCare;
    }

    public void setCare(boolean care) {
        isCare = care;
    }

    public boolean isVisiableGPS() {
        return visiableGPS;
    }

    public void setVisiableGPS(boolean visiableGPS) {
        this.visiableGPS = visiableGPS;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
