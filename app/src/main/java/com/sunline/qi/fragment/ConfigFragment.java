package com.sunline.qi.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.sunline.qi.activity.R;
import com.sunline.qi.ui.DeviceUtils;
import com.sunline.qi.utils.BitmapUtils;

/**
 * Created by sunline on 2016/6/7.
 */
public class ConfigFragment extends Fragment {
    private DeviceUtils utils ;
    private RelativeLayout layout;
    private String[] channelNames;

    private View contentView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initView(inflater, container);
        initLayout();
        return contentView;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("创建设备菜单");
        menu.add(0, 0, 0, "添加设备");
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case 0:
                //Log.i(TAG,"onContextItemSelected(item)");
                Button button = utils.createDevice(layout);
                utils.setListener(button);
                layout.addView(button);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        ((ViewGroup)contentView.getParent()).removeView(contentView);
        super.onDestroyView();
    }
    public void loadData(){
        channelNames = getResources().getStringArray(R.array.channel);
    }
    public void initView(LayoutInflater inflater,ViewGroup container){
        if (null == contentView ){
            contentView = inflater.inflate(R.layout.fragment_config,container,false);
            layout = (RelativeLayout) contentView.findViewById(R.id.config_container);
        }
        utils = new DeviceUtils(getActivity(),inflater,layout,channelNames);
        //layout = utils.loadView(layout);
        registerForContextMenu(contentView);
    }
    public void initLayout(){
        layout = utils.loadView(layout);
    }

}
