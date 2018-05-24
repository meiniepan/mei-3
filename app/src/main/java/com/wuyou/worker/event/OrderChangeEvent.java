package com.wuyou.worker.event;

/**
 * Created by DELL on 2018/3/22.
 */

public class OrderChangeEvent {
    private String serveTime;
    private String serveDate;

    public OrderChangeEvent() {
    }

    public String getServeTime() {
        return serveTime;
    }

    public void setServeTime(String serveTime) {
        this.serveTime = serveTime;
    }

    public String getServeDate() {
        return serveDate;
    }

    public void setServeDate(String serveDate) {
        this.serveDate = serveDate;
    }

    public OrderChangeEvent(String serveDate, String serveTime) {
        this.serveDate = serveDate;
        this.serveTime = serveTime;
    }
}
