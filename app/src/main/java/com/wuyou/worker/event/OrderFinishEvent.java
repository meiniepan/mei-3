package com.wuyou.worker.event;

/**
 * Created by DELL on 2018/3/22.
 */

public class OrderFinishEvent {
    private int pos;

    public OrderFinishEvent(int position) {
        pos = position;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
