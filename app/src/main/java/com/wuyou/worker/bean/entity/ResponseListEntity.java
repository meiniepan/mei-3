package com.wuyou.worker.bean.entity;

import java.util.List;

/**
 * Created by solang on 2018/2/8.
 */

public class ResponseListEntity<T> {
    public String has_more;
    public List<T> list;
}
