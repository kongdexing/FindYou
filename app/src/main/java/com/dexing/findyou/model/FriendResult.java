package com.dexing.findyou.model;

import java.util.Date;

/**
 * Created by dexing on 2016/11/28.
 * No1
 */

public class FriendResult {

    private String objectId;
    private String userName;
    private String headImg;
    private int sex;
    private String birthday;
    private int friendStatus;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    /**
     * 好友关系状态（1同意，0未同意，-1未请求）
     * @return
     */
    public int getFriendStatus() {
        return friendStatus;
    }

    public void setFriendStatus(int friendStatus) {
        this.friendStatus = friendStatus;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
