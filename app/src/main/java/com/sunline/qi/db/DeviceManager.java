package com.sunline.qi.db;

import android.content.Context;
import android.database.Cursor;

import com.sunline.qi.entity.Channels;
import com.sunline.qi.entity.ConfigDetail;
import com.sunline.qi.entity.MainConfig;


/**
 * Created by sunline on 2016/6/2.
 */
public class DeviceManager {
    private DBHelper helper;

    public DeviceManager(Context context) {
        helper = new DBHelper(context);
    }

    /**
     * 查询SQLite数据库(无查询条件)
     *
     * @param sql
     * @return
     */
    public Cursor query(String sql) {
        return helper.query(sql, null);
    }

    /**
     * 查询SQLite数据库(带查询条件)
     *
     * @param sql
     * @param selectionArs
     * @return
     */
    public Cursor query(String sql, String[] selectionArs) {
        return helper.query(sql, selectionArs);
    }

    /**
     * 更改设备配置信息
     *
     * @param sql
     * @param obj
     * @return
     */

    public boolean update(String sql, Object obj) {
        Object[] params = null;
        if (obj instanceof ConfigDetail) {
            ConfigDetail config = (ConfigDetail) obj;
            params = new Object[]{config.getId(), config.getName(), config.getState(),
                    config.getWidth(), config.getHeight(), config.getMarginLeft(), config.getMarginTop(),
                    config.getChannelStates()[0],
                    config.getChannelStates()[1],
                    config.getChannelStates()[2],
                    config.getChannelStates()[3],
                    config.getChannelStates()[4],
            };
        } else if (obj instanceof Integer) {
            params = new Object[]{obj};
        }
        return helper.update(sql, params);
    }

    /**
     * 只改变一条记录中的一个值
     * @param sql
     * @param state
     * @param id
     * @return
     */
    public boolean update(String sql,int state,int id){
        return helper.update(sql,new Object[]{state , id});
    }
    /**
     * 插入一条设备记录
     * @param sql
     * @param detail
     * @return
     */
    public boolean insert(String sql, ConfigDetail detail) {
        Object[] params = new Object[]{detail.getId(), detail.getName(), detail.getState(),
                detail.getWidth(), detail.getHeight(), detail.getMarginLeft(), detail.getMarginTop(),
                detail.getChannelStates()[0],
                detail.getChannelStates()[1],
                detail.getChannelStates()[2],
                detail.getChannelStates()[3],
                detail.getChannelStates()[4]
        };
        return helper.update(sql, params);
    }

    /**
     * 修改设备状态
     *
     * @param sql
     * @param config
     * @return
     */
    public boolean update(String sql, MainConfig config) {
        Object[] params = new Object[]{config.getState(),
                config.getShuntStates().get(0),
                config.getShuntStates().get(1),
                config.getShuntStates().get(2),
                config.getShuntStates().get(3),
                config.getShuntStates().get(4),
                config.getId()};
        return helper.update(sql, params);
    }

    /**
     * 修改设备别名
     *
     * @param sql
     * @param channels
     * @return
     */
    public boolean update(String sql, Channels channels) {
        String[] names = channels.getShuntNames();
        Object[] params = new Object[]{
                names[0],
                names[1],
                names[2],
                names[3],
                names[4],
                channels.getId()
        };
        return helper.update(sql, params);
    }

    /**
     * 新建 一条设备别名的记录
     *
     * @param sql
     * @param channels
     * @return
     */
    public boolean insert(String sql, Channels channels) {
        String[] names = channels.getShuntNames();
        Object[] params = new Object[]{
                channels.getId(),
                channels.getDeviceName(),
                names[0],
                names[1],
                names[2],
                names[3],
                names[4]
        };
        return helper.update(sql, params);
    }
}
