package com.sunline.qi.entity;

/**
 * Created by note on 2016/5/2.
 */
public class Pue {
    private float dayPue;
    private float monthPue;
    private float yearPue;
    private float pastDayPue;
    private float pastMonthPue;
    private float pastYearPue;

    public Pue() {
    }

    public Pue(float dayPue, float monthPue, float yearPue, float pastDayPue, float pastMonthPue, float pastYearPue) {
        this.dayPue = dayPue;
        this.monthPue = monthPue;
        this.yearPue = yearPue;
        this.pastDayPue = pastDayPue;
        this.pastMonthPue = pastMonthPue;
        this.pastYearPue = pastYearPue;
    }

    public float getDayPue() {
        return dayPue;
    }

    public void setDayPue(float dayPue) {
        this.dayPue = dayPue;
    }

    public float getMonthPue() {
        return monthPue;
    }

    public void setMonthPue(float monthPue) {
        this.monthPue = monthPue;
    }

    public float getYearPue() {
        return yearPue;
    }

    public void setYearPue(float yearPue) {
        this.yearPue = yearPue;
    }

    public float getPastDayPue() {
        return pastDayPue;
    }

    public void setPastDayPue(float pastDayPue) {
        this.pastDayPue = pastDayPue;
    }

    public float getPastMonthPue() {
        return pastMonthPue;
    }

    public void setPastMonthPue(float pastMonthPue) {
        this.pastMonthPue = pastMonthPue;
    }

    public float getPastYearPue() {
        return pastYearPue;
    }

    public void setPastYearPue(float pastYearPue) {
        this.pastYearPue = pastYearPue;
    }
}
