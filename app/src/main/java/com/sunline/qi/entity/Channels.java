package com.sunline.qi.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunline on 2016/6/16.
 */
public class Channels {
    private int id;
    private String mDeviceName;
    private String[] mShuntNames;

    public Channels() {
    }

    public Channels(int id, String deviceName, String[] shuntNames) {
        this.id = id;
        mDeviceName = deviceName;
        mShuntNames = shuntNames;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeviceName() {
        return mDeviceName;
    }

    public void setDeviceName(String deviceName) {
        mDeviceName = deviceName;
    }

    public String[] getShuntNames() {
        return mShuntNames;
    }

    public void setShuntNames(String[] shuntNames) {
        if (null == shuntNames || 0 == shuntNames.length) {
            shuntNames = new String[]{"通道1",
                    "通道2",
                    "通道3",
                    "通道4",
                    "通道5"};
        }
        mShuntNames = shuntNames;
    }
}
