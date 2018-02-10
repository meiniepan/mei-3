package com.wuyou.worker.bean.entity;

import java.util.List;

/**
 * Created by solang on 2018/2/8.
 */

public class ContractDetailEntity {
    public String id;
    public String name;
    public long start_at;
    public long end_at;
    public String divided;
    public String times;
    public List<ContractContentEntity> content;
    public String status;
}
