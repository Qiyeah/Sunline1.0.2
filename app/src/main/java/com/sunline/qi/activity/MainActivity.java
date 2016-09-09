package com.sunline.qi.activity;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.sunline.qi.service.LocalService;
import com.sunline.qi.utils.BitmapUtils;
import com.sunline.qi.utils.ExtrnalStorageUtils;
import com.sunline.qi.utils.LoadImageUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static android.graphics.BitmapFactory.*;


/**
 * 程序主界面
 * 修改时间：2016-06-12 端午节后
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private Button main_btn_info, main_btn_power, main_btn_saving, main_btn_honor, main_btn_support, main_btn_pue;
    private PercentRelativeLayout mLayout;
    private SharedPreferences preferences;
    private float day;

    /**
     * 用来处理消息和更新UI
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (1 == msg.what) {
                day = preferences.getFloat("day", 0f);
                main_btn_pue.setText("PUE:" + day);
            }
           /* handler.postDelayed(runnable, 1000 * 2);*/
        }
    };

    /**
     * 发送消息
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.sendEmptyMessage(1);
            handler.postDelayed(this,3000);
        }
    };
    private boolean isBound = false;
    BitmapUtils utils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        utils = new BitmapUtils();
        doBindService();
        initView();
    }

    /**
     * 加载UI
     */
    Bitmap bitmap = null;
    private Drawable mDrawable;
    private File file = null;
    public void initView() {
        /**
         * 设置主界面背景
         *  //TODO 背景图需要处理
         */
       mLayout = (PercentRelativeLayout) findViewById(R.id.main_background);
        /* file = ExtrnalStorageUtils.loadExternalStorageFile("main_bg_nopue.png");
        System.out.println("---file-"+file.getAbsoluteFile());
        mDrawable = utils.loadDrawableImage(file.getAbsolutePath());*/
        mDrawable = new BitmapUtils().loadDrawableImage(getResources(), R.drawable.main_bg_nopue);
        mLayout.setBackground(mDrawable);

        main_btn_pue = (Button) this.findViewById(R.id.main_btn_pue);
        main_btn_info = (Button) this.findViewById(R.id.main_btn_info);
        main_btn_power = (Button) this.findViewById(R.id.main_btn_power);
        main_btn_saving = (Button) this.findViewById(R.id.main_btn_saving);
        main_btn_support = (Button) this.findViewById(R.id.main_btn_support);

        /*-- 设置监听器 --*/
        main_btn_info.setOnClickListener(this);
        main_btn_power.setOnClickListener(this);
        main_btn_saving.setOnClickListener(this);
        main_btn_support.setOnClickListener(this);
        main_btn_pue.setOnClickListener(this);

        /*-- 获取缓存数据，并更新到UI --*/
        preferences = getSharedPreferences("config", MODE_APPEND);
        day = preferences.getFloat("day", 0f);

        main_btn_pue.setText("PUE:" + day);
    }

    @Override
    public void onClick(View v) {

         /*-- Activity跳转的意图 --*/
        Intent intent = new Intent(this, ListViewActivity.class);
         /*-- Activity跳转时，根据控件的ID传递不同的数据 --*/
        if (R.id.main_btn_info == v.getId()){
            intent.putExtra("pageIndex", 1);
        }else  if (R.id.main_btn_power == v.getId()){
            intent.putExtra("pageIndex", 2);
        }else  if (R.id.main_btn_saving == v.getId()){
            intent.putExtra("pageIndex", 3);
        }else  if (R.id.main_btn_pue == v.getId()){
            intent.putExtra("pageIndex",4);
        }else  if (R.id.main_btn_support == v.getId()){
            intent.putExtra("pageIndex",5);
        }

         /*-- 启动Activity --*/
        startActivity(intent);
         /*-- 结束当前Activity --*/
        finish();
    }

    /**
     * 处理“BACK”事件
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("您确定退出程序吗？");
            builder.setCancelable(false);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                    System.exit(0);
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }
        return true;
    }

    /**
     * 连接后台Service，连接成功后执行定时任务
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //localService = ((LocalService.LocalBinder)service).getService();
            /*-- 执行定时任务 --*/
            handler.postDelayed(runnable, 1000 * 2);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    /**
     * 绑定服务
     */
    public void doBindService() {
        if (!isBound){
            Intent intent = new Intent(this, LocalService.class);
            isBound = bindService(intent, mConnection, BIND_AUTO_CREATE);
        }
    }

    /**
     * 解绑服务
     */
    public void doUnBindService() {

        Intent intent = new Intent(this, LocalService.class);

        if (isBound) {

            unbindService(mConnection);

            stopService(intent);

            handler.removeCallbacks(runnable);

            isBound = false;
        }
    }

    /**
     * Activity停止运行时，清空背景图片
     */
    @Override
    protected void onStop() {
        bitmap = null;
        super.onStop();
    }

    /**
     * Activity销毁时，停止后台服务
     */
    @Override
    protected void onDestroy() {
        /**
         * 退出程序前，解绑服务
         */
        doUnBindService();
        main_btn_pue = null;
        main_btn_info = null;
        main_btn_power = null;
        main_btn_saving = null;
        main_btn_support = null;
//        utils.releaseImage(file.getAbsolutePath());
        //setContentView(R.layout.null_view);
        super.onDestroy();
    }

}
