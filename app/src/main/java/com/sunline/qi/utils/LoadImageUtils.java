package com.sunline.qi.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.sunline.qi.activity.R;

import java.io.File;
import java.util.concurrent.ExecutionException;

/**
 * Created by sunline on 2016/6/14.
 */
public class LoadImageUtils {
    public static final int MAIN = 0;
    public static final int PAGE = 1;


    private Context mContext;
    public LoadImageUtils(Context context){
        mContext = context;
    }

    public void setBackground(final File file, final int viewType, final View view){
        new AsyncTask<Void,Void,Bitmap>(){
            private Bitmap bitmap;
            @Override
            protected Bitmap doInBackground(Void... params) {
                try {
                    if (PAGE == viewType){
                        bitmap = Glide.with(mContext).load(file).asBitmap().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.RESULT).placeholder(R.drawable.list_fragment_background).into(430,270).get();
                    }else if (MAIN == viewType){
                        bitmap = Glide.
                                with(mContext).
                                load(file).
                                asBitmap().
                                skipMemoryCache(true).
                                diskCacheStrategy(DiskCacheStrategy.RESULT).
                                placeholder(R.drawable.main_bg_nopue).
                                into(600,320).
                                get();
                    }else {
                        bitmap = Glide.with(mContext).load(file).asBitmap().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.RESULT).into(480,270).get();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (null != bitmap){
                    view.setBackground(new BitmapDrawable(bitmap));
                }
            }
        }.execute();
    }
    private Bitmap mBitmap;
    private String ss = "";
    public Bitmap decodeByFile(final File file){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("thread is run");
                    ss = "new thread";
                    mBitmap = Glide.
                            with(mContext).
                            load(file).
                            asBitmap().
                            skipMemoryCache(true).
                            diskCacheStrategy(DiskCacheStrategy.RESULT).
                            placeholder(R.drawable.main_bg_nopue).
                            into(600,320).
                            get();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }).start();
       /* try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        System.out.println("ss = "+ss);
        return mBitmap;
    }

}
