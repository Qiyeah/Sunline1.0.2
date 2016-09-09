package com.sunline.qi.entity;

/**
 * Created by sunline on 2016/5/26.
 */
public class Degree {
    private int channel;
    private float degree;

    public Degree() {
    }

    public Degree(int channel, float degree) {
        this.channel = channel;
        this.degree = degree;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public float getDegree() {
        return degree;
    }

    public void setDegree(float degree) {
        this.degree = degree;
    }
}
