package com.sunline.qi.thread;

import android.content.Context;
import android.database.Cursor;

import com.sunline.qi.db.DeviceManager;
import com.sunline.qi.entity.ConfigDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunline on 2016/6/6.
 */
public abstract class LoadLocalViewThread implements Runnable {
    DeviceManager manager;
    List<ConfigDetail> devices;
    public LoadLocalViewThread(Context context) {
        manager = new DeviceManager(context);
        devices = new ArrayList<ConfigDetail>();
    }
    @Override
    public void run() {
        String sql = "select * from Device";
        Cursor cursor = manager.query(sql);
        while (cursor.moveToNext()) {
            ConfigDetail detail = new ConfigDetail();
            int deviceId = cursor.getInt(cursor.getColumnIndex("_id"));
            String deviceName = cursor.getString(cursor.getColumnIndex("device_name"));
            int deviceState = cursor.getInt(cursor.getColumnIndex("device_state"));
            int deviceWidth = cursor.getInt(cursor.getColumnIndex("device_width"));
            int deviceHeight = cursor.getInt(cursor.getColumnIndex("device_height"));
            int deviceX = cursor.getInt(cursor.getColumnIndex("device_x"));
            int deviceY = cursor.getInt(cursor.getColumnIndex("device_y"));
            int[] tempState = new int[5];
            tempState[0] = cursor.getInt(cursor.getColumnIndex("device_channel1"));
            tempState[1] = cursor.getInt(cursor.getColumnIndex("device_channel2"));
            tempState[2] = cursor.getInt(cursor.getColumnIndex("device_channel3"));
            tempState[3] = cursor.getInt(cursor.getColumnIndex("device_channel4"));
            tempState[4] = cursor.getInt(cursor.getColumnIndex("device_channel5"));

            detail.setId(deviceId);
            detail.setName(deviceName);
            detail.setState(deviceState);
            detail.setWidth(deviceWidth);
            detail.setHeight(deviceHeight);
            detail.setMarginLeft(deviceX);
            detail.setMarginTop(deviceY);
            detail.setChannelStates(tempState);

            devices.add(detail);
        }
        LoadLocalViewCallBack(devices);
    }
    public abstract void LoadLocalViewCallBack(List<ConfigDetail> devices);
}
