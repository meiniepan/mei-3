package com.wuyou.worker.bean;

import java.util.List;

/**
 * Created by DELL on 2018/3/12.
 */

public class ListResponse<T> {
    public List<T> list;
    public int has_more;
}
