package com.sunline.qi.entity;

import java.util.List;

/**
 * Created by sunline on 2016/6/1.
 */
public class Result {
    private List<SingleDegree> data;

    public Result() {
    }

    public Result(List<SingleDegree> data) {
        this.data = data;
    }

    public List<SingleDegree> getData() {
        return data;
    }

    public void setData(List<SingleDegree> data) {
        this.data = data;
    }
}
