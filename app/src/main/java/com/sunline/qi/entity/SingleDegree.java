package com.sunline.qi.entity;

import java.util.List;

/**
 * Created by sunline on 2016/6/1.
 */
public class SingleDegree {
    private String degreeType;
    private List<Device> device;

    public SingleDegree() {
    }

    public SingleDegree(String degreeType, List<Device> device) {
        this.degreeType = degreeType;
        this.device = device;
    }

    public String getDegreeType() {
        return degreeType;
    }

    public void setDegreeType(String degreeType) {
        this.degreeType = degreeType;
    }

    public List<Device> getDevice() {
        return device;
    }

    public void setDevice(List<Device> device) {
        this.device = device;
    }
}
