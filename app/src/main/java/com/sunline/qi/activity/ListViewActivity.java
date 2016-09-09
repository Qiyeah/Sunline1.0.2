package com.sunline.qi.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.percent.PercentRelativeLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.sunline.qi.fragment.ConfigFragment;
import com.sunline.qi.fragment.InfoFragment;
import com.sunline.qi.fragment.PowerFragment;
import com.sunline.qi.fragment.PueFragment;
import com.sunline.qi.fragment.SavingFragment;
import com.sunline.qi.fragment.SupportFragment;
import com.sunline.qi.utils.BitmapUtils;
import com.sunline.qi.utils.ExtrnalStorageUtils;
import com.sunline.qi.utils.LoadImageUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by sunline on 2016/5/19.
 * Alert on 2016-06-12
 */
public class ListViewActivity extends Activity implements View.OnClickListener {
    private int pageIndex;
    private ListView mListView;
    private int[] titleIds = new int[]{R.id.list_item_back, R.id.list_item_info, R.id.list_item_power,
            R.id.list_item_saving, R.id.list_item_pue};
    private FragmentManager fm = getFragmentManager();
    private FragmentTransaction transaction = null;
    private BitmapUtils utils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pager);

        utils = new BitmapUtils();

        loadData();

        setDefaultPage();

        initView();

    }

    /**
     * 设置默认界面
     */
    public void setDefaultPage() {
        Fragment fragment1 = null;
        /**
         * 根据传入的值，设置显示的页面
         */
        switch (pageIndex) {
            case 1:
                fragment1 = new InfoFragment();
                getFragmentManager().beginTransaction().add(R.id.pageContainer, fragment1).commit();
                break;
            case 2:
                fragment1 = new PowerFragment();
                getFragmentManager().beginTransaction().replace(R.id.pageContainer, fragment1).commit();
                break;
            case 3:
                fragment1 = new SavingFragment();
                getFragmentManager().beginTransaction().replace(R.id.pageContainer, fragment1).commit();
                break;
            case 4:
                fragment1 = new PueFragment();
                getFragmentManager().beginTransaction().replace(R.id.pageContainer, fragment1).commit();
                break;
            case 5:
                fragment1 = new SupportFragment();
                getFragmentManager().beginTransaction().replace(R.id.pageContainer, fragment1).commit();
                break;
        }
    }

    private PercentRelativeLayout mPercentRelativeLayout;
    private Drawable mDrawable;
    private File file = null;
    /**
     * 显示界面的初始化
     */
    public void initView() {
        mPercentRelativeLayout = (PercentRelativeLayout) findViewById(R.id.list_fragment);
       /* file = ExtrnalStorageUtils.loadExternalStorageFile("list_fragment_background.png");
        mDrawable = utils.loadDrawableImage(file.getAbsolutePath());*/
        mDrawable = new BitmapUtils().loadDrawableImage(getResources(), R.drawable.list_fragment_background);

        mPercentRelativeLayout.setBackground(mDrawable);

        mListView = (ListView) findViewById(R.id.listView);
        mListView.setSelection(1);
        mListView.setItemsCanFocus(true);
        /**
         * 左侧ListView添加适配器
         */
        mListView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return titleIds.length;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                convertView = getLayoutInflater().inflate(R.layout.item_detail, null);
                Button itemButton = (Button) convertView.findViewById(R.id.list_item_btn);
                TextView itemText = (TextView) convertView.findViewById(R.id.list_item_text);
                switch (position) {
                    case 0:
                        itemButton.setBackgroundResource(R.drawable.back_selector);
                        itemText.setBackgroundResource(R.drawable.back_text);
                        break;
                    case 1:
                        itemButton.setBackgroundResource(R.drawable.info_selector);
                        itemText.setBackgroundResource(R.drawable.info_text);
                        break;
                    case 2:
                        itemButton.setBackgroundResource(R.drawable.power_selector);
                        itemText.setBackgroundResource(R.drawable.power_text);
                        break;
                    case 3:
                        itemButton.setBackgroundResource(R.drawable.saving_selector);
                        itemText.setBackgroundResource(R.drawable.saving_text);
                        break;
                    case 4:
                        itemButton.setBackgroundResource(R.drawable.pue_selector);
                        itemText.setBackgroundResource(R.drawable.pue_text);
                        break;

                }
                itemButton.setId(titleIds[position]);
                itemButton.setOnClickListener(ListViewActivity.this);

                return convertView;
            }

        });
    }

    /**
     * 数据的加载
     */
    public void loadData() {
        /**
         * Activity跳转时，接收传入的数据
         */
        pageIndex = getIntent().getIntExtra("pageIndex", 1);
    }

    /**
     * 点击事件的处理
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        transaction = fm.beginTransaction();
        Fragment fragment = null;
        switch (v.getId()) {
            case R.id.list_item_back:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.list_item_info:
                fragment = new InfoFragment();
                transaction.replace(R.id.pageContainer, fragment);
                break;
            case R.id.list_item_power:
                fragment = new PowerFragment();
                transaction.replace(R.id.pageContainer, fragment);
                break;
            case R.id.list_item_saving:
                fragment = new SavingFragment();
                transaction.replace(R.id.pageContainer, fragment);
                break;
            case R.id.list_item_pue:
                fragment = new PueFragment();
                transaction.replace(R.id.pageContainer, fragment);
                break;

        }
        transaction.commit();
    }

    /**
     * 回退键的处理（BACK）
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            super.openOptionsMenu();
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mListView.setAdapter(null);
        mListView = null;
        mPercentRelativeLayout = null;
//        utils.releaseImage(file.getAbsolutePath());
        //setContentView(R.layout.null_view);
    }
}
