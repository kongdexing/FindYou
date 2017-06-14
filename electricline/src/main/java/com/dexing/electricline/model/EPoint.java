package com.dexing.electricline.model;

import com.amap.api.maps2d.model.LatLng;

import org.greenrobot.greendao.annotation.Entity;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by dexing on 2017/6/1.
 * No1
 */
@Entity
public class EPoint extends BmobObject implements Serializable {

    private double latitude;
    private double longitude;
    private String villageId;
    private int type;//0 12米，1 15米杆，2电表箱
    private String number;//编号

    @Generated(hash = 2030037275)
    public EPoint(double latitude, double longitude, String villageId, int type,
            String number) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.villageId = villageId;
        this.type = type;
        this.number = number;
    }

    @Generated(hash = 1759290670)
    public EPoint() {
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getVillageId() {
        return villageId;
    }

    public void setVillageId(String villageId) {
        this.villageId = villageId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LatLng getLatLng() {
        return new LatLng(latitude, longitude);
    }

}
