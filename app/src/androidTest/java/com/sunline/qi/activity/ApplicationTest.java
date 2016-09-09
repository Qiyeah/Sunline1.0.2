package com.sunline.qi.activity;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.sunline.qi.control.SharedPreferencesTools;
import com.sunline.qi.db.DeviceManager;
import com.sunline.qi.entity.Channels;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }
    public void testUpdate(){
        DeviceManager manager = new DeviceManager(getContext());
        Channels channels = new Channels();
        channels.setId(1001);
        channels.setShuntNames(new String[]{
                "sdfsfnt1",
                "ssdfsft2",
                "dssadf",
                "fsdf",
                "sfd"
        });
        manager.update("update Channels set device_shuntName1 = ? ,device_shuntName2 = ? ,device_shuntName3 = ? ,device_shuntName4 = ? ," +
                "device_shuntName5 = ? where _id = ?", channels);
    }
    public void testPreference(){
        SharedPreferencesTools preferenceTools = new SharedPreferencesTools(getContext(),"preference");
        preferenceTools.insert("day", 1.12f);
        preferenceTools.insert("month", 2.23f);
        preferenceTools.insert("year", 3.24f);
        preferenceTools.insert("pastDay", 4.56f);
        preferenceTools.insert("pastMonth", 5.87f);
        preferenceTools.insert("pastYear", 6.89f);
    }
}