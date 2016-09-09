package com.sunline.qi.utils;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;

import com.sunline.qi.service.LocalService;


/**
 * Created by sunline on 2016/6/22.
 */
public class ServiceUtils {
    private boolean isBound = false;
    private Context mContext;
    private Handler mHandler;
    public ServiceUtils(Context context,Handler handler){
        mContext = context;
        mHandler = handler;
    }
    /**
     * 绑定服务
     */
    public void doBindService(ServiceConnection mConnection) {
        if (!isBound){
            Intent intent = new Intent(mContext, LocalService.class);
            isBound = mContext.bindService(intent, mConnection, mContext.BIND_AUTO_CREATE);
        }
    }

    /**
     * 解绑服务
     */
    public void doUnBindService(ServiceConnection mConnection) {
        Intent intent = new Intent(mContext, LocalService.class);

        if (isBound) {

            mContext.unbindService(mConnection);

            mContext.stopService(intent);

            mHandler.removeCallbacks(runnable);

            isBound = false;
        }
    }
    /**
     * 发送消息
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(1);
        }
    };
}
