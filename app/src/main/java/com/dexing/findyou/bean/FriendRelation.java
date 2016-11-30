package com.dexing.findyou.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by dexing on 2016/11/28.
 * No1
 */

public class FriendRelation extends BmobObject {

    private String fromUser;
    private String toUser;
    //（1同意，0未同意，-1未请求）
    private int status;
    private String groupName;
    private boolean isCare;
    private boolean visibilityGPS;
    private String remark;

    public FriendRelation() {
        isCare = false;
        visibilityGPS = true;
        groupName = "好友";
        remark = "";
    }

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

    public boolean isVisibilityGPS() {
        return visibilityGPS;
    }

    public void setVisibilityGPS(boolean visibilityGPS) {
        this.visibilityGPS = visibilityGPS;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
