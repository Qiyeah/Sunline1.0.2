package com.sunline.qi.thread;

import android.content.Context;
import android.database.Cursor;

import com.sunline.qi.db.DeviceManager;
import com.sunline.qi.entity.ConfigDetail;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by sunline on 2016/6/3.
 */
public abstract class LocalThread implements Runnable {
    DeviceManager manager;
    private int mId;

    @Override
    public void run() {
        //查询本地配置
        String sql = "select * from Device where _id = ?";
        Cursor cursor = manager.query(sql, new String[]{Integer.toString(mId)});
        ConfigDetail detail = new ConfigDetail();
        List<Integer> tempState = new ArrayList<Integer>();;
        if (0 < cursor.getCount()) {
            while (cursor.moveToNext()) {
                int deviceId = cursor.getInt(cursor.getColumnIndex("_id"));
                String deviceName = cursor.getString(cursor.getColumnIndex("device_name"));
                int deviceState = cursor.getInt(cursor.getColumnIndex("device_state"));
                int deviceWidth = cursor.getInt(cursor.getColumnIndex("device_width"));
                int deviceHeight = cursor.getInt(cursor.getColumnIndex("device_height"));
                int deviceX = cursor.getInt(cursor.getColumnIndex("device_x"));
                int deviceY = cursor.getInt(cursor.getColumnIndex("device_y"));
                int deviceChannel1 = cursor.getInt(cursor.getColumnIndex("device_channel1"));
                int deviceChannel2 = cursor.getInt(cursor.getColumnIndex("device_channel2"));
                int deviceChannel3 = cursor.getInt(cursor.getColumnIndex("device_channel3"));
                int deviceChannel4 = cursor.getInt(cursor.getColumnIndex("device_channel4"));
                int deviceChannel5 = cursor.getInt(cursor.getColumnIndex("device_channel5"));

                detail.setId(deviceId);
                detail.setName(deviceName);
                detail.setState(deviceState);
                detail.setWidth(deviceWidth);
                detail.setHeight(deviceHeight);
                detail.setMarginLeft(deviceX);
                detail.setMarginTop(deviceY);

                tempState.add(deviceChannel1);
                tempState.add(deviceChannel2);
                tempState.add(deviceChannel3);
                tempState.add(deviceChannel4);
                tempState.add(deviceChannel5);

            }
        }

        if (null == detail) {
            detail.setId(mId);
            tempState.add(3);
            tempState.add(3);
            tempState.add(3);
            tempState.add(3);
            tempState.add(3);
        }
        localThreadCallBack(detail);
    }

    public abstract void localThreadCallBack(ConfigDetail detail);

    public LocalThread(Context context, int id) {
        manager = new DeviceManager(context);
        mId = id;
    }
}
