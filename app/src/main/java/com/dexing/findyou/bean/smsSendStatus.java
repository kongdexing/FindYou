package com.dexing.findyou.bean;

import java.util.Date;

import cn.bmob.v3.BmobObject;

/**
 * Created by dexing on 2016/11/28.
 * No1
 */

/**
 * 每个手机号每天只允许发送三次验证码，三次后用邮箱验证注册或找回密码
 */
public class SmsSendStatus extends BmobObject {

    private String phone;
    private Date updateTime; //发送时间
    private int sendTimes; //发送次数

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getSendTimes() {
        return sendTimes;
    }

    public void setSendTimes(int sendTimes) {
        this.sendTimes = sendTimes;
    }
}
