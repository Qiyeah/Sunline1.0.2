package com.sunline.qi.entity;

import java.util.List;

/**
 * Created by sunline on 2016/6/6.
 */
public class MainConfig {
    private int mId;
    private int mState;
    private List<Integer> shuntStates;

    public MainConfig(int id, int state, List<Integer> shuntStates) {
        mId = id;
        mState = state;
        this.shuntStates = shuntStates;
    }

    public MainConfig() {
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

    public void setState(boolean state) {
        if (state) {
            mState = 0;
        }else {
            mState = 1;
        }
    }

    public List<Integer> getShuntStates() {
        return shuntStates;
    }

    public void setShuntStates(List<Integer> shuntStates) {
        this.shuntStates = shuntStates;
    }
}
