package com.wuyou.worker.bean.entity;

import java.util.List;

/**
 * Created by Solang on 2018/5/30.
 */

public class ServiceSort2 {
    public int number;
    public String id;
    public String title;
    public Float price;
    public String unit;
    public String photo;
    public String[] images;
    public int stock;
    public String has_specification;
    public List<ServiceSort2Spec> specification;
}
