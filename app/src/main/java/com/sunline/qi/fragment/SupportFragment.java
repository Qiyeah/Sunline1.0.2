package com.sunline.qi.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.percent.PercentRelativeLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sunline.qi.activity.R;
import com.sunline.qi.control.SharedPreferencesTools;
import com.sunline.qi.utils.BitmapUtils;
import com.sunline.qi.utils.ExtrnalStorageUtils;
import com.sunline.qi.utils.LoadImageUtils;

import java.io.File;

/**
 * Created by sunline on 2016/5/25.
 */
public class SupportFragment extends Fragment {
    private static final String TAG = "SupportFragment";
    View view;
    TextView config;
    private PercentRelativeLayout layout;
    private FragmentManager fm;
    private FragmentTransaction transaction = null;
    private SharedPreferencesTools loginUtils;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fm = getFragmentManager();
        transaction = fm.beginTransaction();
        loginUtils = new SharedPreferencesTools(getActivity(), "config");
        loginUtils.insert("username","sunlines");
        loginUtils.insert("password","sl");


    }


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initView(inflater, container);
        return view;
    }

    public void initView(final LayoutInflater inflater, ViewGroup container) {
        if (null == view) {
            view = inflater.inflate(R.layout.page_support, container, false);
            config = (TextView) view.findViewById(R.id.config);

            initBackground();

            initDialog();

        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
    }

    private Drawable mDrawable;
    /**
     * 初始化背景
     */
    public void initBackground() {
        mDrawable = new BitmapUtils().loadDrawableImage(getResources(), R.drawable.support_background_right01);
        view.setBackground(mDrawable);
        /*final File file = ExtrnalStorageUtils.loadExternalStorageFile("support_background_right01.png");
        new LoadImageUtils(getActivity()).setBackground(file, 5, view);*/
    }

    private ViewTag tag = null;

    /**
     * 初始化Dialog(登陆框)
     */
    public void initDialog() {
        config.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                LayoutInflater inflater1 = LayoutInflater.from(getActivity());
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(false);
                View view = null;

                if (null == view) {
                    view = inflater1.inflate(R.layout.layout_login, null);
                    tag = new ViewTag();
                    tag.username = (EditText) view.findViewById(R.id.username);
                    tag.password = (EditText) view.findViewById(R.id.password);
                    view.setTag(tag);
                } else {
                    tag = (ViewTag) view.getTag();
                }
                builder.setView(view);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String user = tag.username.getText().toString().trim();
                        String pass = tag.password.getText().toString().trim();
                        //Toast.makeText(getActivity(),"user = "+user+"\npass = "+pass,Toast.LENGTH_LONG).show();
                        if (loginUtils.isSuccess(user, pass)) {
                            Fragment fragment = new ConfigFragment();
                            getFragmentManager().
                                    beginTransaction().
                                    replace(R.id.pageContainer, fragment).
                                    addToBackStack("ConfigFragment").
                                    commit();
                        } else
                            //Toast.makeText(getActivity(), "---对不起！输入用户名或密码错误---", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
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
        });
    }

    @Override
    public void onStop() {
        Drawable drawable = view.getBackground();
        if (null != drawable)
            drawable.setCallback(null);
        view.setBackground(drawable);
        super.onStop();
    }

    class ViewTag {
        private EditText username, password;
    }
    @Override
    public void onDestroyView() {
        loginUtils = null;
        view = null;
        mDrawable = null;
        super.onDestroyView();
    }
}
