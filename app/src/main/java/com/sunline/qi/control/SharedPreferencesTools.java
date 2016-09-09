package com.sunline.qi.control;

import android.content.Context;
import android.content.SharedPreferences;

import com.sunline.qi.entity.Pue;

/**
 * Created by sunline on 2016/6/16.
 */
public class SharedPreferencesTools {
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private Context mContext;

    public SharedPreferencesTools(Context context, String xmlName) {
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(xmlName, Context.MODE_APPEND);
        mEditor = mSharedPreferences.edit();
    }

    public void insert(String key, Object value) {

        if (value instanceof Integer) {
            mEditor.putInt(key, (Integer) value);
        } else if (value instanceof Float) {
            mEditor.putFloat(key, (Float) value);
        } else if (value instanceof String) {
            mEditor.putString(key, (String) value);
        }
        mEditor.commit();
    }

    public boolean isSuccess(String username, String password) {
        String preUser = mSharedPreferences.getString("username", "").trim();
        String prePass = mSharedPreferences.getString("password", "").trim();
        if (username.equals(preUser) && password.equals(prePass))
            return true;
        else
            return false;
    }
}
