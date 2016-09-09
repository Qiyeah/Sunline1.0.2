package com.sunline.qi.fragment;

import android.app.Fragment;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.percent.PercentRelativeLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.sunline.qi.activity.R;
import com.sunline.qi.utils.BitmapUtils;
import com.sunline.qi.utils.ExtrnalStorageUtils;
import com.sunline.qi.utils.LoadImageUtils;

import java.io.File;

/**
 * Created by sunline on 2016/5/20.
 */
public class InfoFragment extends Fragment {
    private static final String TAG = "InfoFragment";
    View view = null;
    private Drawable mDrawable;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == view){
            view = new View(getActivity());
             mDrawable = new BitmapUtils().loadDrawableImage(getResources(), R.drawable.info_background_right01);
            view.setBackground(mDrawable);
           /* File file = ExtrnalStorageUtils.loadExternalStorageFile("info_background_right01.png");
            new LoadImageUtils(getActivity()).setBackground(file, 1,view);*/
        }
        ViewGroup parent = (ViewGroup)view.getParent();
        if (parent != null){
            parent.removeView(view);
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((ViewGroup)view.getParent()).removeView(view);
        mDrawable = null;
    }
}
