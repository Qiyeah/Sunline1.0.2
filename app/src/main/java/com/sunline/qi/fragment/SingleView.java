package com.sunline.qi.fragment;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;

/**
 * Created by sunline on 2016/5/25.
 */
public class SingleView {
    public View instance(View view,Resources resources,int imgId){
        BitmapFactory factory = new BitmapFactory();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = null;
        int be = (int) ((options.outHeight > options.outWidth ? options.outHeight / 150
                : options.outWidth / 200));
        if (be <= 0) // 判断200是否超过原始图片高度
            be = 1; // 如果超过，则不进行缩放
        options.inSampleSize = be;
        options.inPreferredConfig = Bitmap.Config.ARGB_4444;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inJustDecodeBounds = false;
        try {
            bitmap = BitmapFactory.decodeResource(resources,imgId);
        } catch (OutOfMemoryError e) {
            System.gc();
        }
        BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
        view.setBackground(bitmapDrawable);
        return view;
    }
}
