package com.sunline.qi.listener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.sunline.qi.activity.R;
import com.sunline.qi.db.DeviceManager;
import com.sunline.qi.entity.Channels;
import com.sunline.qi.entity.ConfigDetail;
import com.sunline.qi.entity.MainConfig;
import com.sunline.qi.thread.LocalThread;
import com.sunline.qi.utils.ScreenUtils;
import com.sunline.qi.utils.ServiceUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by sunline on 2016/6/2.
 */
public class OnTouchListenerImpl implements View.OnTouchListener {
    private static final String TAG = "OnTouchListenerImpl";
    private long currentDownTime = 0;
    private long currentUpTime = 0;
    private int tempX;
    private int tempY;
    private RelativeLayout mLayout;
    private int mId;
    private DeviceManager manager;
    private Context mContext;
    private boolean isDelete = false;
    private boolean isOpen = true;
    private LayoutInflater mInflater;
    private DeviceTag tag = null;
    private int btnId = 0;
    private int mScreenWidth,mScreenHeight,width,height;
    private ScreenUtils mScreenUtils ;
    private int rows = 10;
    private int cols = 15;
    private int leftMargin,topMargin;
    public OnTouchListenerImpl(LayoutInflater inflater, Context context, DeviceManager manager,
                               RelativeLayout layout, int id) {
        mLayout = layout;
        mContext = context;
        mInflater = inflater;
        mId = id;
        this.manager = manager;
        sqlChannelNames = new String[5];
        mScreenUtils = new ScreenUtils(context);

        mScreenWidth = mScreenUtils.getScreenWidth();
        mScreenHeight = mScreenUtils.getScreenHeight();
        width = mScreenWidth/cols;
        height = mScreenHeight/rows;
    }

    @Override
    public boolean onTouch(final View v, MotionEvent event) {

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v
                .getLayoutParams();
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                currentDownTime = Calendar.getInstance().getTimeInMillis();
                tempX = x - layoutParams.leftMargin;
                tempY = y - layoutParams.topMargin;

                break;
            case MotionEvent.ACTION_UP:
                currentUpTime = Calendar.getInstance().getTimeInMillis();

                if (1200 <= (currentUpTime - currentDownTime)) {
                    btnId = v.getId();
                    //初始化设备参数列表
                    View view = initConfigMenu();

                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("设备编辑器");

                    builder.setView(view);
                    builder.setCancelable(false);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (isDelete) {
                                manager.update("delete from Device where _id = ?", btnId);
                                manager.update("delete from Channels where _id = ? ", btnId);
                                v.setVisibility(View.GONE);
                            }
                            //TODO 更新通道别名
                            String[] vChannelNames = new String[]{tag.mItems.get(0).getText().toString().trim(),
                                    tag.mItems.get(1).getText().toString().trim(),
                                    tag.mItems.get(2).getText().toString().trim(),
                                    tag.mItems.get(3).getText().toString().trim(),
                                    tag.mItems.get(4).getText().toString().trim(),
                            };
                            Channels channels = new Channels();
                            channels.setId(v.getId());
                            channels.setShuntNames(vChannelNames);
                            manager.update("update Channels set device_shuntName1 = ? ,device_shuntName2 = ? ,device_shuntName3 = ? ,device_shuntName4 = ? ," +
                                    "device_shuntName5 = ? where _id = ?", channels);
                            //TODO 更新设备明细
                            MainConfig config = new MainConfig();
                            config.setId(v.getId());
                            config.setState(isOpen);
                            for (int i = 0; i < tag.mSpinners.size(); i++) {
                                states.add(tag.mSpinners.get(i).getSelectedItemPosition());
                            }
                            config.setShuntStates(states);
                            manager.update("update Device set device_state = ? ,device_channel1 = ?," +
                                    "device_channel2 = ?," +
                                    "device_channel3 = ?," +
                                    "device_channel4 = ?," +
                                    "device_channel5 = ?" +
                                    "where _id = ? ", config);
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
                /**
                 * 更新配置文件中设备的坐标数据
                 */
                manager.update("update Device set device_x = " + layoutParams.leftMargin +
                    ",device_y = " + layoutParams.topMargin + " where _id= ? ", mId);
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                tempX = x - layoutParams.leftMargin;
                tempY = y - layoutParams.topMargin;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                leftMargin = x - tempX;
                topMargin = y - tempY;
                if (leftMargin < 0) {
                    leftMargin = 0;
                }
                if (topMargin < 0) {
                    topMargin = 0;
                }

                int top = topMargin/height;
                int left = leftMargin/width;
                layoutParams.topMargin = top*height;
                layoutParams.leftMargin = left*width;
                v.setLayoutParams(layoutParams);
                break;
        }

        mLayout.invalidate();
        return false;
    }

    /**
     * 设备菜单中视图组件标签
     */
    public class DeviceTag {
        private List<Spinner> mSpinners;
        private Switch mSwitch;
        private CheckBox mChecked;
        private List<TextView> mItems;
    }

    /**
     * 存储数据库中通道别名
     */
    private String[] sqlChannelNames;
    /**
     * 设备对话框视图中的通道名称
     */
    private List<TextView> shuntNames;
    /**
     * 更改通道名称时，打开的对话框视图
     */
    private View titleView;

    public View initConfigMenu() {
        View view = null;
        /**
         * 加载配置好的通道别名
         */
        loadChannelNames();


        if (null == view) {
            tag = new DeviceTag();
            view = mInflater.inflate(R.layout.device_menu, null);
            tag.mSwitch = (Switch) view.findViewById(R.id.opened);
            tag.mSpinners = new ArrayList<Spinner>();
            tag.mSpinners.add((Spinner) view.findViewById(R.id.shuntState1));
            tag.mSpinners.add((Spinner) view.findViewById(R.id.shuntState2));
            tag.mSpinners.add((Spinner) view.findViewById(R.id.shuntState3));
            tag.mSpinners.add((Spinner) view.findViewById(R.id.shuntState4));
            tag.mSpinners.add((Spinner) view.findViewById(R.id.shuntState5));
            tag.mItems = new ArrayList<TextView>();
            tag.mItems.add((TextView) view.findViewById(R.id.shuntName1));
            tag.mItems.add((TextView) view.findViewById(R.id.shuntName2));
            tag.mItems.add((TextView) view.findViewById(R.id.shuntName3));
            tag.mItems.add((TextView) view.findViewById(R.id.shuntName4));
            tag.mItems.add((TextView) view.findViewById(R.id.shuntName5));
            tag.mChecked = (CheckBox) view.findViewById(R.id.delete);
            view.setTag(tag);
        }
        tag = (DeviceTag) view.getTag();


        tag.mSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isOpen = isChecked;
                if (isOpen) {
                    manager.update("update Device set device_state = ? where _id = ? ",0,mId );
                }else {
                    manager.update("update Device set device_state = ? where _id = ? ",1,mId );
                }
            }
        });

        tag.mChecked.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isDelete = isChecked;
            }
        });
        shuntNames = tag.mItems;
        for (int i = 0; i < shuntNames.size(); i++) {
            shuntNames.get(i).setText(sqlChannelNames[i]);
        }
        /**
         * 设置通道的别名
         */
        shuntNames.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = 0;
                titleView = LayoutInflater.from(mContext).inflate(R.layout.layout_alert, null);
                setAlertDialog(titleView);
            }
        });
        shuntNames.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = 1;
                titleView = LayoutInflater.from(mContext).inflate(R.layout.layout_alert, null);
                setAlertDialog(titleView);
            }
        });
        shuntNames.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = 2;
                titleView = LayoutInflater.from(mContext).inflate(R.layout.layout_alert, null);
                setAlertDialog(titleView);
            }
        });
        shuntNames.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = 3;
                titleView = LayoutInflater.from(mContext).inflate(R.layout.layout_alert, null);
                setAlertDialog(titleView);
            }
        });
        shuntNames.get(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = 4;
                titleView = LayoutInflater.from(mContext).inflate(R.layout.layout_alert, null);
                setAlertDialog(titleView);
            }
        });
        /**
         *  //TODO 加载 开关 及 通道 状态数据
         */
        loadChannelState();
        //TODO 设置开关状态
        tag.mSwitch.setChecked(isOpen);
        for (int i = 0; i < tag.mSpinners.size(); i++) {
            final int finalI = i;
            tag.mSpinners.get(i).setSelection(tempStates[i], true);
            tag.mSpinners.get(i).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    /**
                     * 当通道性质发生改变里，更新到本地Sqlite数据库
                     */
                    String sql = "update Device set device_channel" + (finalI + 1) + " = ? where _id = ?";
                    manager.update(sql, position, mId);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        return view;
    }

    /**
     * 更改别名需要的序号
     */
    private int index;
    /**
     * 存储通道性质的集合
     */
    private List<Integer> states = new ArrayList<Integer>();

    /**
     * 更改通道别名的对话框
     *
     * @param view
     */
    public void setAlertDialog(View view) {
        final EditText editText = (EditText) view.findViewById(R.id.editText);
        editText.setText(tag.mItems.get(index).getText().toString().trim());
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(false);
        builder.setView(view);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value = editText.getText().toString().trim();
                tag.mItems.get(index).setText(value);
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

    /**
     * 加载本地配置好的通道别名
     */
    public void loadChannelNames() {
        Cursor cursor = manager.query("select * from Channels where _id = ?", new String[]{Integer.toString(mId)});
        if (cursor.moveToNext()) {
            sqlChannelNames[0] = cursor.getString(cursor.getColumnIndex("device_shuntName1"));
            sqlChannelNames[1] = cursor.getString(cursor.getColumnIndex("device_shuntName2"));
            sqlChannelNames[2] = cursor.getString(cursor.getColumnIndex("device_shuntName3"));
            sqlChannelNames[3] = cursor.getString(cursor.getColumnIndex("device_shuntName4"));
            sqlChannelNames[4] = cursor.getString(cursor.getColumnIndex("device_shuntName5"));
        }
    }

    /**
     * 存储通道性质
     */
    private int[] tempStates = new int[5];

    /**
     * 加载本地配置好的通道性质
     */
    public void loadChannelState() {
        Cursor cursor = manager.query("select device_state,device_channel1,device_channel2,device_channel3,device_channel4,device_channel5 from Device where _id = ?", new String[]{Integer.toString(mId)});
        if (0 < cursor.getCount()) {
            while (cursor.moveToNext()) {
                int deviceState = cursor.getInt(cursor.getColumnIndex("device_state"));
                if (0 == deviceState){
                    isOpen = true;
                }else {
                    isOpen = false;
                }

                tempStates[0] = cursor.getInt(cursor.getColumnIndex("device_channel1"));
                tempStates[1] = cursor.getInt(cursor.getColumnIndex("device_channel2"));
                tempStates[2] = cursor.getInt(cursor.getColumnIndex("device_channel3"));
                tempStates[3] = cursor.getInt(cursor.getColumnIndex("device_channel4"));
                tempStates[4] = cursor.getInt(cursor.getColumnIndex("device_channel5"));
            }
        }
    }
}
