package com.wuyou.worker.event;

/**
 * Created by DELL on 2018/3/22.
 */

public class OrderChangeEvent {
    private int pos;

    public OrderChangeEvent(int position) {
        pos = position;
    }

    public OrderChangeEvent() {


    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
