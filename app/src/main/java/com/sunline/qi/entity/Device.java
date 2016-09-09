package com.sunline.qi.entity;

import java.util.List;

/**
 * Created by sunline on 2016/5/23.
 */
public class Device {
    private String deviceName;
    private List<Degree> degrees;


    public Device(String deviceName, List<Degree> degrees) {
        this.deviceName = deviceName;
        this.degrees = degrees;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public Device() {
    }


    public List<Degree> getDegrees() {
        return degrees;
    }

    public void setDegrees(List<Degree> degrees) {
        this.degrees = degrees;
    }
}
