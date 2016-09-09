package com.sunline.qi.entity;

import java.util.List;

/**
 * Created by sunline on 2016/6/7.
 */
public class DeviceState {
    private int mId;
    private int mState;
    private List<Integer> shuntStates;

    public DeviceState(int id, int state, List<Integer> shuntStates) {
        mId = id;
        mState = state;
        this.shuntStates = shuntStates;
    }

    public DeviceState() {
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getState() {
        return mState;
    }

    public void setState(int state) {
        mState = state;
    }

    public List<Integer> getShuntStates() {
        return shuntStates;
    }

    public void setShuntStates(List<Integer> shuntStates) {
        this.shuntStates = shuntStates;
    }
}
