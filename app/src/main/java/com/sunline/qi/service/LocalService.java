package com.sunline.qi.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.sunline.qi.control.SharedPreferencesTools;
import com.sunline.qi.db.DeviceManager;
import com.sunline.qi.entity.Degree;
import com.sunline.qi.entity.Device;
import com.sunline.qi.entity.MainConfig;
import com.sunline.qi.entity.Pue;
import com.sunline.qi.entity.Result;
import com.sunline.qi.entity.SingleDegree;
import com.sunline.qi.utils.PueUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunline on 2016/6/6.
 */
public class LocalService extends Service {
    private static final String TAG = "--LocalService->>";
    String resultStr = "";
    private Result result;
    private SingleDegree mSingleDegree;
    private DeviceManager manager;
    private Pue pue = null;
    private Gson gson = new Gson();
    private boolean isStop = false;

    //计算电度值
    private float[] day = null;
    private float[] month = null;
    private float[] year = null;
    private float[] pastDay = null;
    private float[] pastMonth = null;
    private float[] pastYear = null;
    private float[] end = null;

    public String getResultStr() {
        return resultStr;
    }

    public Pue getPue() {
        if (null == pue)
            return new Pue(0f, 0f, 0f, 0f, 0f, 0f);
        else
            return pue;
    }

    /**
     * 设置PUE
     */
    public void format2Pue() {
        pue = new Pue();
        result = gson.fromJson(resultStr, Result.class);
        List<SingleDegree> list = result.getData();
        for (int i = 0; i < list.size(); i++) {
            String degreeType = list.get(i).getDegreeType();
            List<Device> devices = list.get(i).getDevice();
            if ("day".equalsIgnoreCase(degreeType)) {
                day = parseDevices(devices);
            } else if ("month".equalsIgnoreCase(degreeType)) {
                month = parseDevices(devices);
            } else if ("year".equalsIgnoreCase(degreeType)) {
                year = parseDevices(devices);
            } else if ("pastDay".equalsIgnoreCase(degreeType)) {
                pastDay = parseDevices(devices);
            } else if ("pastMonth".equalsIgnoreCase(degreeType)) {
                pastMonth = parseDevices(devices);
            } else if ("pastYear".equalsIgnoreCase(degreeType)) {
                pastYear = parseDevices(devices);
            } else if ("end".equalsIgnoreCase(degreeType)) {
                end = parseDevices(devices);
            }
        }
        PueUtils pueUtils = new PueUtils();
        pue.setDayPue(pueUtils.getPue(day, end));
        pue.setMonthPue(pueUtils.getPue(month, end));
        pue.setYearPue(pueUtils.getPue(year, end));
        pue.setPastDayPue(pueUtils.getPue(pastDay, end));
        pue.setPastMonthPue(pueUtils.getPue(pastMonth, end));
        pue.setPastYearPue(pueUtils.getPue(pastYear, end));

       /* Log.i(TAG, Float.toString(pue.getDayPue()));
        Log.i(TAG, Float.toString(pue.getMonthPue()));
        Log.i(TAG, Float.toString(pue.getYearPue()));
        Log.i(TAG, Float.toString(pue.getPastDayPue()));
        Log.i(TAG, Float.toString(pue.getPastMonthPue()));
        Log.i(TAG, Float.toString(pue.getPastYearPue()));*/

        /**
         * //todo 更新pue到xml
         */

        preferenceTools.insert("day", Float.valueOf(pue.getDayPue()));
        preferenceTools.insert("month",Float.valueOf( pue.getMonthPue()));
        preferenceTools.insert("year", Float.valueOf(pue.getYearPue()));
        preferenceTools.insert("pastDay", Float.valueOf(pue.getPastDayPue()));
        preferenceTools.insert("pastMonth", Float.valueOf(pue.getPastMonthPue()));
        preferenceTools.insert("pastYear", Float.valueOf(pue.getPastYearPue()));

    }

    /**
     * 根据设备状态计算各类电度值
     *      分路状态：0    ---    总用电
     *                1    ---    服务器用电
     *                3    ---    总用电+服务器用电
     *                4    ---    不纳入PUE计算范围
     *
     * @param devices
     * @return
     */
    public float[] parseDevices(List<Device> devices) {
        //TODO 统计电度数据
        //用来临时存储开始电度值
        float[] temp = new float[2];
        List<MainConfig> configs = getDeviceState();
        //已配置设备
        if (0 != configs.size() && null != configs) {//设备数量不为0
            //配置的设备小于实际的设备数量
            if (devices.size() > configs.size()) {
                //只遍历已配置的设备
                for (int i = 0; i < configs.size(); i++) {
                    List<Degree> degrees = devices.get(i).getDegrees();
                    //获取到实际电度值并且配置的设备是打开状态
                    if (0 != degrees.size() && 1 != configs.get(i).getState()) {
                        for (int j = 0; j < degrees.size(); i++) {
                            int channel = devices.get(i).getDegrees().get(j).getChannel();
                            int channelState = configs.get(i).getShuntStates().get(channel - 1);

                            if (channelState == 0  ) {
                                temp[0] += degrees.get(j).getDegree();
                            } else if (channelState == 1  ){
                                temp[1] += degrees.get(j).getDegree();
                            }else if (channelState == 2){
                                temp[0] += degrees.get(j).getDegree();
                                temp[1] += degrees.get(j).getDegree();
                            }
                        }
                    }
                }
            } else {//配置设备不小于实际设备数量
                for (int i = 0; i < devices.size(); i++) {
                    List<Degree> degrees = devices.get(i).getDegrees();
                    if (1 != configs.get(i).getState()) {
                        for (int j = 0; j < degrees.size(); j++) {
                            int channel = devices.get(i).getDegrees().get(j).getChannel();
                            int channelState = configs.get(i).getShuntStates().get(channel - 1);
                           // Log.i(TAG,"channel = "+channel+"    channelState = "+channelState);
                            if (channelState == 0  ) {
                                temp[0] += degrees.get(j).getDegree();
                            } else if (channelState == 1  ){
                                temp[1] += degrees.get(j).getDegree();
                            }else if (channelState == 2){
                                temp[0] += degrees.get(j).getDegree();
                                temp[1] += degrees.get(j).getDegree();
                            }
                        }
                    }
                }
            }
        }else {
            temp[0] = 0f;
            temp[1] = 0f;
        }
        //Log.i(TAG," temp[0] = "+temp[0]+"    temp[1] = "+ temp[1]);
        return temp;
    }

    /**
     * 分析设备状态
     * 设备状态：0 --  启用
     *           1 --  信用
     * @return 所有启用状态的设备
     */
    public List<MainConfig> getDeviceState() {
        Cursor cursor = manager.query("select _id,device_state,device_channel1,device_channel2,device_channel3,device_channel4" +
                ",device_channel5 from Device");
        List<MainConfig> configs = new ArrayList<MainConfig>();
        if (0 < cursor.getCount()) {
            while (cursor.moveToNext()) {
                List<Integer> temp = new ArrayList<Integer>();
                MainConfig config = new MainConfig();
                config.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                int state = cursor.getInt(cursor.getColumnIndex("device_state"));
                if (0 == state) {
                    config.setState(true);
                } else if (1 == state) {
                    config.setState(false);
                }
                temp.add(cursor.getInt(cursor.getColumnIndex("device_channel1")));
                temp.add(cursor.getInt(cursor.getColumnIndex("device_channel2")));
                temp.add(cursor.getInt(cursor.getColumnIndex("device_channel3")));
                temp.add(cursor.getInt(cursor.getColumnIndex("device_channel4")));
                temp.add(cursor.getInt(cursor.getColumnIndex("device_channel5")));
                config.setShuntStates(temp);
                configs.add(config);
            }
        }
        return configs;
    }

    public class LocalBinder extends Binder {
        public LocalService getService() {
            return LocalService.this;
        }
    }

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private SharedPreferencesTools preferenceTools;

    @Override
    public void onCreate() {
        super.onCreate();
        preferenceTools= new SharedPreferencesTools(this,"config");

      /*  preferences = getSharedPreferences("pue", MODE_APPEND);
        editor = preferences.edit();*/

        manager = new DeviceManager(this);
        handler.postDelayed(runnable, 1000 * 1);
    }

    @Override
    public IBinder onBind(Intent intent) {
        IBinder binder = null;
        if (null == binder) {
            binder = new LocalBinder();
        }
        return binder;
    }

    public class ConnDatabaseAsync extends AsyncTask<String, Integer, Object> {

        @Override
        protected Object doInBackground(String... arg0) {
            try {
                //TODO 访问服务器
                URL url = new URL("http://192.168.1.130:8080/sunlines/data");
                HttpURLConnection urlConnection = null;
                try {
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setConnectTimeout(5000);
                    try {
                        urlConnection.connect();
                    } catch (IOException e) {

                    }
                    InputStream in = urlConnection.getInputStream();
                    int len = 0;
                    byte[] buf = new byte[8192];
                    while (-1 != (len = in.read(buf))) {
                        resultStr = new String(buf, 0, len, "utf-8");
                    }
                    try {
                        new Thread().sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                   // Log.i(TAG, resultStr);
                    format2Pue();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private Handler handler = new Handler()/*{
        @Override
        public void handleMessage(Message msg) {

//                new Thread(runnable).start();
                handler.postDelayed(runnable, 1000 * 2);
        }
    }*/;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!isStop) {
                new ConnDatabaseAsync().execute();
                handler.postDelayed(this,3000);
            }
        }
    };

    @Override
    public boolean onUnbind(Intent intent) {
        isStop = true;
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }
}
