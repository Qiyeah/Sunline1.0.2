package com.sunline.qi.ui;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.sunline.qi.db.DeviceManager;
import com.sunline.qi.entity.Channels;
import com.sunline.qi.entity.ConfigDetail;
import com.sunline.qi.listener.OnTouchListenerImpl;
import com.sunline.qi.thread.LoadLocalViewThread;
import com.sunline.qi.utils.ScreenUtils;

import java.util.List;


/**
 * Created by sunline on 2016/6/2.
 */
public class DeviceUtils {
    public static final int CHECKED = 0;
    public static final int DISCHECKED = 1;
    private ConfigDetail mDetail;
    DeviceManager manager = null;
    private int count = 0;
    private static final String TAG = "DeviceUtils";
    private Context mContext;
    private LayoutInflater mInflater;
    private String[] mChannelNames;

    private int deviceId = 1;

    private int mScreenWidth, mScreenHeight, width, height;
    private ScreenUtils mScreenUtils;
    private int rows = 10;
    private int cols = 10;
    private int leftMargin, topMargin;

    public DeviceUtils(Context context, LayoutInflater inflater, RelativeLayout layout, String[] channelNames) {
        this.mContext = context;
        this.mScreenUtils = new ScreenUtils(context);
        manager = new DeviceManager(mContext);
        mInflater = inflater;
        mChannelNames = channelNames;
    }

    /**
     * 计算新设备的ID
     */

    public void initDevieId() {
        Cursor c = manager.query("select _id from Device");
        count = c.getCount();
        if (0 < count) {
            int temp = 0;
            while (c.moveToNext()) {
                temp++;
                if (temp == count) {
                    deviceId = c.getInt(c.getColumnIndex("_id"));
                    deviceId++;
                }
            }
        }
    }

    /**
     * 创建一个默认设备
     *
     * @return
     */
    public Button createDevice(RelativeLayout layout) {
        initDevieId();
        width = mScreenUtils.getScreenWidth() / cols;
        height = mScreenUtils.getScreenHeight() / rows;
        //TODO 创建一个新的设备有待完成
        ScreenUtils mScreenUtils = new ScreenUtils(mContext);
        mDetail = new ConfigDetail();
        mDetail.setId(deviceId);
        mDetail.setState(1);
        mDetail.setChannelStates(new int[]{3, 3, 3, 3, 3});
        mDetail.setWidth(width);
        mDetail.setHeight(height);
        int flag = 0;
        if (count == 0 ){
            mDetail.setMarginLeft(width * 0);
            mDetail.setMarginTop(height * 0);
        }else {
            for (int i = 1; i < count + 1; i++) {
                if (i % 8 == 0 ){
                    flag += 1;
                }
                mDetail.setMarginLeft(width * (i % 8));
                mDetail.setMarginTop(height * flag);
            }
        }
        mDetail.setChannelStates(new int[]{3, 3, 3, 3, 3});
        Button button = new Button(mContext);

        button.setId(mDetail.getId());
        button.setText(mDetail.getName());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mDetail.getWidth(),
                mDetail.getHeight());
        params.leftMargin = mDetail.getMarginLeft();
        params.topMargin = mDetail.getMarginTop();
        button.setLayoutParams(params);
        mOnTouchListener = new OnTouchListenerImpl(mInflater, mContext, manager, layout, mDetail.getId());
        manager.update("insert into Device values(?,?,?,?,?,?,?,?,?,?,?,?)", mDetail);
        manager.insert("insert into Channels values(?,?,?,?,?,?,?)", new Channels(mDetail.getId(),
                mDetail.getName(), new String[]{mChannelNames[0], mChannelNames[1], mChannelNames[2],
                mChannelNames[3], mChannelNames[4]}));
        setListener(button);
        return button;
    }

    private List<ConfigDetail> mDevices = null;

    /**
     * 加载本地布局
     *
     * @param layout
     * @return
     */
    public RelativeLayout loadView(RelativeLayout layout) {

        LoadLocalViewThread thread = new LoadLocalViewThread(mContext) {
            @Override
            public void LoadLocalViewCallBack(List<ConfigDetail> devices) {
                mDevices = devices;
            }
        };
        new Thread(thread).start();
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < mDevices.size(); i++) {
            ConfigDetail device = mDevices.get(i);
            Button button = new Button(mContext);
            button.setId(device.getId());
            button.setText(device.getName());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(device.getWidth(),
                    device.getHeight());
            params.leftMargin = device.getMarginLeft();
            params.topMargin = device.getMarginTop();
            mOnTouchListener = new OnTouchListenerImpl(mInflater, mContext, manager, layout, button.getId());
            setListener(button);
            layout.addView(button, params);
        }
        return layout;
    }

    private View.OnTouchListener mOnTouchListener = null;

    public void setListener(Button button) {
        button.setOnTouchListener(mOnTouchListener);
    }
}
