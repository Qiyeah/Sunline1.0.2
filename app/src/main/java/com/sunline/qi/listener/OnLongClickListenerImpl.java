package com.sunline.qi.listener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.sunline.qi.activity.R;
import com.sunline.qi.db.DeviceManager;
import com.sunline.qi.entity.ConfigDetail;
import com.sunline.qi.thread.LocalThread;


/**
 * Created by sunline on 2016/6/3.
 */
public abstract class OnLongClickListenerImpl implements View.OnLongClickListener {
    private int btnId = 0;
    DeviceManager manager;
    DeviceTag tag =null;
    private boolean isOpen = true;
    private boolean isDelete = false;
    private int path1 = 0;
    private int path2 = 0;
    private int path3 = 0;
    private int path4 = 0;
    private int path5 = 0;
    private Context mContext;
    private LayoutInflater mInflater;
    private String[] channelNames;
    public OnLongClickListenerImpl(Context context,LayoutInflater inflater,String[] channelNames){
        mContext = context;
        mInflater = inflater;
        this.channelNames = channelNames;
        manager = new DeviceManager(mContext);


    }
    @Override
    public boolean onLongClick(View v) {
        btnId = v.getId();
        //初始化设备参数列表
        View view = initView();

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("设备编辑器");

        builder.setView(view);
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isDelete) {
                    manager.update("delete from Device where _id = ?",btnId);
                    longClickCallBack(isDelete);
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dialog.dismiss();
            }
        });
        builder.show();
        return true;
    }
    public class DeviceTag {

        private Switch mSwitch;
        private CheckBox checked;
        private TextView shuntName1, shuntName2, shuntName3, shuntName4, shuntName5;
    }
    public View initView(){
        View view = null;

        if (null == view){
            tag = new DeviceTag();
            view = mInflater.inflate(R.layout.device_menu,null);
            tag.mSwitch = (Switch) view.findViewById(R.id.opened);
        /*    tag.shuntState1 = (EditText) view.findViewById(R.id.shuntState1);
            tag.shuntState2 = (EditText) view.findViewById(R.id.shuntState2);
            tag.shuntState3 = (EditText) view.findViewById(R.id.shuntState3);
            tag.shuntState4 = (EditText) view.findViewById(R.id.shuntState4);
            tag.shuntState5 = (EditText) view.findViewById(R.id.shuntState5);*/
            tag.checked = (CheckBox) view.findViewById(R.id.delete);
            view.setTag(tag);
        }
        tag = (DeviceTag) view.getTag();

        //初始化第一次设备配置界面
        initTag();

        tag.mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isOpen = isChecked;
            }
        });

        tag.checked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isDelete = isChecked;
            }
        });
        return view;
    }
    private ConfigDetail mDetail;
    public void initTag(){
        //查询本地配置
        new Thread(new LocalThread(mContext, btnId) {
            @Override
            public void localThreadCallBack(ConfigDetail detail) {
                if (null != detail){
                    mDetail = detail;
                }
            }
        }).start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
      /*  tag.shuntState1.setText("" + mDetail.getChannel1State());
        tag.shuntState2.setText("" + mDetail.getChannel2State());
        tag.shuntState3.setText("" + mDetail.getChannel3State());
        tag.shuntState4.setText(""+mDetail.getChannel4State());
        tag.shuntState5.setText(""+mDetail.getChannel5State());*/
        tag.checked.setChecked(false);
    }
    public abstract void longClickCallBack(boolean isTrue);
}
