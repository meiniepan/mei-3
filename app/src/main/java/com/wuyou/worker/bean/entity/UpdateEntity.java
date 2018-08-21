package com.wuyou.worker.bean.entity;

/**
 * Created by solang on 2018/2/8.
 */

public class UpdateEntity {
    public int update;//是否需要更新（0：不需要；1：建议更新；2：强制更新）
    public String url;
    public String text;
    public String latest;
}
