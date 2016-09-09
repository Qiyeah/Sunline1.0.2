package com.sunline.qi.fragment;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sunline.qi.activity.R;
import com.sunline.qi.service.LocalService;
import com.sunline.qi.utils.BitmapUtils;
import com.sunline.qi.utils.ExtrnalStorageUtils;
import com.sunline.qi.utils.LoadImageUtils;
import com.sunline.qi.utils.ServiceUtils;

import java.io.File;
import java.text.DecimalFormat;

/**
 * Created by sunline on 2016/5/20.
 */
public class PueFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "PueFragment";
    SharedPreferences preferences;
    View view = null;
    Button btn_day, btn_month, btn_year, btn_24, btn_30, btn_365;
    TextView pueTitle, pueValue;
    String[] titleStrs = null;


    private ServiceUtils mServiceUtils;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 绑定后台服务
         */
        mServiceUtils = new ServiceUtils(getActivity(),handler);
        mServiceUtils.doBindService(mConnection);


        //handler.postDelayed(runnable, 1000 * 1);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initView(inflater, container);
        loadData();
        pueTitle.setText(titleStrs[0]);
        pueValue.setText("" + day);
        return view;
    }
    private Drawable mDrawable;
    public void initView(LayoutInflater inflater, ViewGroup container) {
        //Log.i(TAG,"initView is run");
        if (null == view) {
            view = inflater.inflate(R.layout.page_pue1, container, false);
            //Log.i(TAG,"view ==null : "+(null == view));
            mDrawable = new BitmapUtils().loadDrawableImage(getResources(), R.drawable.pue_background_right01);
            view.setBackground(mDrawable);
           /* File file = ExtrnalStorageUtils.loadExternalStorageFile("pue_background_right01.png");
            new LoadImageUtils(getActivity()).setBackground(file, 4, view);*/
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }


        btn_day = (Button) view.findViewById(R.id.page_pue_btn1);
        btn_month = (Button) view.findViewById(R.id.page_pue_btn2);
        btn_year = (Button) view.findViewById(R.id.page_pue_btn3);
        btn_24 = (Button) view.findViewById(R.id.page_pue_btn4);
        btn_30 = (Button) view.findViewById(R.id.page_pue_btn5);
        btn_365 = (Button) view.findViewById(R.id.page_pue_btn6);
        pueValue = (TextView) view.findViewById(R.id.pue_value);

        btn_day.setOnClickListener(this);
        btn_month.setOnClickListener(this);
        btn_year.setOnClickListener(this);
        btn_24.setOnClickListener(this);
        btn_30.setOnClickListener(this);
        btn_365.setOnClickListener(this);

        pueTitle = (TextView) view.findViewById(R.id.pue_title);
    }
    private int pueType = 0;
    @Override
    public void onClick(View v) {
        String titleStr = "";
        float value = 0f;
        switch (v.getId()) {
            case R.id.page_pue_btn1:
                pueType = 0;
                titleStr = titleStrs[0];
                value = day;
                break;
            case R.id.page_pue_btn2:
                pueType = 1;
                titleStr = titleStrs[1];
                value = month;
                break;
            case R.id.page_pue_btn3:
                pueType = 2;
                titleStr = titleStrs[2];
                value = year;
                break;
            case R.id.page_pue_btn4:
                pueType = 3;
                titleStr = titleStrs[3];
                value = pastDay;
                break;
            case R.id.page_pue_btn5:
                pueType = 4;
                titleStr = titleStrs[4];
                value = pastMonth;
                break;
            case R.id.page_pue_btn6:
                pueType = 5;
                titleStr = titleStrs[5];
                value = pastYear;
                break;
        }
        // }
        pueTitle.setText(titleStr);
        pueValue.setText(""+value);
    }
    private float day;
    private float month;
    private float year;
    private float pastDay;
    private float pastMonth;
    private float pastYear;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (1 == msg.what) {
                updateData();

            }
            /*handler.postDelayed(runnable, 1000 * 5);*/
        }
    };
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.sendEmptyMessage(1);
            handler.postDelayed(this,3000);
        }
    };





    private void updateData() {

        /**
         * 从xml中取出后台服务计算好的PUE值
         */
        day = preferences.getFloat("day", 0f);
        month = preferences.getFloat("month", 0f);
        year = preferences.getFloat("year", 0f);
        pastDay = preferences.getFloat("pastDay", 0f);
        pastMonth = preferences.getFloat("pastMonth", 0f);
        pastYear = preferences.getFloat("pastYear", 0f);

        /**
         * 更新到UI
         */

        //Log.i(TAG,"view : "+(null == view));
        btn_day.setText("\n" + day);
        btn_month.setText("\n" + month);
        btn_year.setText("\n" + year);
        btn_24.setText("\n" + pastDay);
        btn_30.setText("\n" +pastMonth);
        btn_365.setText("\n" + pastYear);
        if (0 == pueType){
            pueValue.setText(""+day);
        }else if (1 == pueType){
            pueValue.setText(""+month);
        }else if (2 == pueType){
            pueValue.setText(""+year);
        }else if (3 == pueType){
            pueValue.setText(""+pastDay);
        }else if (4 == pueType){
            pueValue.setText(""+pastMonth);
        }else if (5 == pueType){
            pueValue.setText(""+pastYear);
        }
    }

    public void loadData() {
        //Log.i(TAG,"loadData is run");
        if (null == titleStrs) {
            titleStrs = getResources().getStringArray(R.array.pueTitles);
        }
        if (null == preferences) {
            preferences = getActivity().getSharedPreferences("config", Context.MODE_APPEND);
        }
       updateData();
    }

    @Override
    public void onDestroyView() {
        view = null;

        super.onDestroyView();
    }



    /**
     * 连接后台Service，连接成功后执行定时任务
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //localService = ((LocalService.LocalBinder)service).getService();
            /*-- 执行定时任务 --*/
            handler.postDelayed(runnable, 1000 * 1);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDrawable = null;
        mServiceUtils.doUnBindService(mConnection);
    }
}
