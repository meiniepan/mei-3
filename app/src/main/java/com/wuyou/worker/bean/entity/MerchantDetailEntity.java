package com.wuyou.worker.bean.entity;

import java.util.List;

/**
 * Created by solang on 2018/2/10.
 */

public class MerchantDetailEntity {
    public String id;
    public String name;
    public int star;
    public String address;
    public String tel_number;
    public String category;
    public String qualification;
    public UnionListEntity unions;
    public List<ContractEntity> contracts;
}
