package com.dexing.findyou.bean;

import com.dexing.findyou.util.CommonUtil;

import org.greenrobot.greendao.annotation.Entity;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

import org.greenrobot.greendao.annotation.Generated;

import java.util.Date;

/**
 * Created by dexing on 2016/11/27.
 * No1
 */
@Entity
public class FUser extends BmobObject {

    //登录名，密码，手机号，邮箱，头像，昵称，性别，出生日期，地理位置，方位

    private String loginName;
    private String password;
    private String phoneNum;
    private String email;
    private String headImg;
    private String nickName;
    private int sex;
    private String birthday;
    private boolean gpsPush = true;

    public FUser() {
        this.loginName = "";
        this.password = "";
        this.phoneNum = "";
        this.email = "";
        this.headImg = "";
        this.nickName = "";
        this.sex = -1;
        this.birthday = "";
    }

    @Generated(hash = 801768181)
    public FUser(String loginName, String password, String phoneNum, String email,
            String headImg, String nickName, int sex, String birthday,
            boolean gpsPush) {
        this.loginName = loginName;
        this.password = password;
        this.phoneNum = phoneNum;
        this.email = email;
        this.headImg = headImg;
        this.nickName = nickName;
        this.sex = sex;
        this.birthday = birthday;
        this.gpsPush = gpsPush;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
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

    public boolean isGpsPush() {
        return gpsPush;
    }

    public void setGpsPush(boolean gpsPush) {
        this.gpsPush = gpsPush;
    }

    public boolean getGpsPush() {
        return this.gpsPush;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNum() {
        return this.phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
