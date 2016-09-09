package com.sunline.qi.fragment;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunline.qi.activity.R;
import com.sunline.qi.utils.BitmapUtils;
import com.sunline.qi.utils.ExtrnalStorageUtils;
import com.sunline.qi.utils.LoadImageUtils;

import java.io.File;

/**
 * Created by sunline on 2016/5/20.
 */
public class SavingFragment extends Fragment {
    private static final String TAG = "SavingFragment";
    View view = null;
    private Drawable mDrawable;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == view){
            view = new View(getActivity());
            mDrawable = new BitmapUtils().loadDrawableImage(getResources(), R.drawable.saving_background_right01);
            view.setBackground(mDrawable);
            /*File file = ExtrnalStorageUtils.loadExternalStorageFile("saving_background_right01.png");
            new LoadImageUtils(getActivity()).setBackground(file,3,view);*/
        }
        ViewGroup parent = (ViewGroup)view.getParent();
        if (parent != null){
            parent.removeView(view);
        }
        return view;
    }
    @Override
    public void onStop() {
        Drawable drawable = view.getBackground();
        if (null != drawable)
            drawable.setCallback(null);
        view.setBackground(drawable);
        super.onStop();
    }
    @Override
    public void onDestroyView() {
        view = null;
        mDrawable = null;
        super.onDestroyView();
    }
}
