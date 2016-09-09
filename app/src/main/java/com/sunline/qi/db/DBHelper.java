package com.sunline.qi.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by sunline on 2016/6/2.
 */
public class DBHelper extends SQLiteOpenHelper {


    private static final String TAG = "DBHelper";
    private static final String DB_NAME = "sunline.db";
    private static final int VERSION = 1;
    private SQLiteDatabase mDatabase;
    public DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
        mDatabase = getWritableDatabase();
    }

    /**
     *  数据库第一次创建时被调用，仅调用一次。功能是用来建表
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table Device(_id integer primary key," +
                "device_name varchar(20)," +
                "device_state integer ," +
                "device_width integer," +
                "device_height integer," +
                "device_x integer," +
                "device_y integer," +
                "device_channel1 integer," +
                "device_channel2 integer," +
                "device_channel3 integer," +
                "device_channel4 integer," +
                "device_channel5 integer" +
                ")";
        db.execSQL(sql);
        sql = "create table Channels(" +
                "_id integer primary key," +
                "device_name varchar(20)," +
                "device_shuntName1 varchar(30)," +
                "device_shuntName2 varchar(30)," +
                "device_shuntName3 varchar(30)," +
                "device_shuntName4 varchar(30)," +
                "device_shuntName5 varchar(30)" +
                ") ";
        db.execSQL(sql);
    }

    /**
     * 版本更新时才调用
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /**
         * 通道别名,设备别名（暂时未修改）
         */
        String sql = "";

        db.execSQL(sql);
    }

    /**\
     * 执行增、删、改操作
     * @param sql
     * @param params
     * @return
     */
    public boolean update(String sql,Object[] params){
        boolean flag =  false;
        try {
            mDatabase.execSQL(sql,params);
            flag = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 执行查询操作
     * @param sql
     * @param selectionArs
     * @return
     */
    public Cursor query(String sql,String[] selectionArs){
        return mDatabase.rawQuery(sql,selectionArs);
    }
}
