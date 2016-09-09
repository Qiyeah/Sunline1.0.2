package com.sunline.qi.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by sunline on 2016/6/2.
 */
public class ScreenUtils {

    private int mScreenWidth;
    private int mScreenHeight;
    public ScreenUtils(Context context){
        Resources resources = context.getResources();
        DisplayMetrics dm =  resources.getDisplayMetrics();
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;
    }
    public int getScreenWidth(){
        return mScreenWidth;
    }
    public int getScreenHeight(){
        return mScreenHeight;
    }
}
