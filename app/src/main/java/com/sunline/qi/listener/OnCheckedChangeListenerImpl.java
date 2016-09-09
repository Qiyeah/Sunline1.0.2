package com.sunline.qi.listener;

import android.widget.CompoundButton;

/**
 * Created by sunline on 2016/6/3.
 */
public abstract class OnCheckedChangeListenerImpl implements CompoundButton.OnCheckedChangeListener {
    public static final boolean CHECKED = true;
    public static final boolean DISCHECKED = false;
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            checkedChangeCallBack(CHECKED);
        } else {
            checkedChangeCallBack(DISCHECKED);
        }
    }
    abstract void checkedChangeCallBack(boolean state);
}
