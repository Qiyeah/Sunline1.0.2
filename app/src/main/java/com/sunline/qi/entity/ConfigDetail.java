package com.sunline.qi.entity;

import java.util.List;

/**
 * Created by sunline on 2016/6/2.
 */
public class ConfigDetail {
    private int mId;
    private String mName;
    private int mState;
    private int mWidth;
    private int mHeight;
    private int mMarginLeft;
    private int mMarginTop;
    private int[] channelStates;
    private int mDelete;

    public ConfigDetail() {
    }

    public ConfigDetail(int id, String name, int state, int width, int height, int marginLeft,
                        int marginTop, int[] channelStates, int delete) {
        mId = id;
        mName = name;
        mState = state;
        mWidth = width;
        mHeight = height;
        mMarginLeft = marginLeft;
        mMarginTop = marginTop;
        this.channelStates = channelStates;
        mDelete = delete;
    }

    public String getName() {
        if (null == mName)
            return "设备 " + getId() + " #";
        else
            return mName;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getState() {
        return mState;
    }

    public void setState(int state) {
        mState = state;
    }

    public int getWidth() {
        return mWidth;
    }

    public void setWidth(int width) {
        mWidth = width;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int height) {
        mHeight = height;
    }

    public int getMarginLeft() {
        return mMarginLeft;
    }

    public void setMarginLeft(int marginLeft) {
        mMarginLeft = marginLeft;
    }

    public int getMarginTop() {
        return mMarginTop;
    }

    public void setMarginTop(int marginTop) {
        mMarginTop = marginTop;
    }

    public int[] getChannelStates() {
        return channelStates;
    }

    public void setChannelStates(int[] channelStates) {
        this.channelStates = channelStates;
    }

    public int getDelete() {
        return mDelete;
    }

    public void setDelete(int delete) {
        mDelete = delete;
    }
}
