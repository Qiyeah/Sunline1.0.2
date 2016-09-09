package com.sunline.qi.entity;

import java.util.List;

/**
 * Created by sunline on 2016/6/7.
 */
public class ChannelState {
    private List<Integer> states;

    public ChannelState() {
    }

    public ChannelState(List<Integer> states) {
        this.states = states;
    }

    public List<Integer> getStates() {
        return states;
    }

    public void setStates(List<Integer> states) {
        this.states = states;
    }
}
