package com.sunline.qi.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by sunline on 2016/6/14.
 */
public class ExtrnalStorageUtils {
    /**
     *  加载外部存储的路径
     * @param imageName 文件的相对父路径
     * @return 查询文件数组
     */
    public static File loadExternalStorageFile(String imageName) {
        if (inExternalStorageWritable()) {
            File file = new File(Environment.getExternalStorageDirectory()+"/sunlines/cache",imageName);
            if (!file.exists()){
                file.mkdirs();
                if (file.isFile()){
                    return file;
                }
            }
        }
        return null;
    }
    /**
     * 检查外部存储是否可读、可写
     * @return
     */
    public static boolean inExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
}
