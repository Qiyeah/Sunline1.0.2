package com.sunline.qi.utils;

import java.text.DecimalFormat;

/**
 * Created by sunline on 2016/6/7.
 */
public class PueUtils {
    public float getPue(float[] start,float[] end) {
        if (end[0] <= start[0]){
            start[0] = 0;
            start[1] = 0;
        }
        if (end[0] == 0){
            return 0f;
        }else {
            if (0!=end[1])
                return formatPue((end[0] - start[0]) / (end[1] - start[1]));
            else
                return 0f;
        }
    }
    public float formatPue(float pue){
        DecimalFormat df = new DecimalFormat("#.##");
        return Float.parseFloat(df.format(pue));
    }
}
