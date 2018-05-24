package com.wuyou.worker.bean.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by solang on 2018/2/8.
 */

public class OrderInfoEntity implements Parcelable {
    public String id;
    public String order_id;
    public String category;
    public long created_at;
    public ServiceEntity service;
    public AddressEntity address;
    public String price;
    public String service_time;
    public int status;
    public String is_dispatch;
    public String receiver;
    public long accept_at;
    public String order_no;
    public String phone;
    public String pay_type;
    public String pay_status;
    public long dispatched_at;
    public int is_finished;
    public String number;
    public float total_amount;
    public float amount;
    public float second_payment;
    public ShopBean shop;
    public String service_mode;
    public String service_date;
    public String remark;
    public String serial;
    public long pay_time;

    public OrderInfoEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.order_id);
        dest.writeString(this.category);
        dest.writeLong(this.created_at);
        dest.writeParcelable(this.service, flags);
        dest.writeParcelable(this.address, flags);
        dest.writeString(this.price);
        dest.writeString(this.service_time);
        dest.writeInt(this.status);
        dest.writeString(this.is_dispatch);
        dest.writeString(this.receiver);
        dest.writeLong(this.accept_at);
        dest.writeString(this.order_no);
        dest.writeString(this.phone);
        dest.writeString(this.pay_type);
        dest.writeString(this.pay_status);
        dest.writeLong(this.dispatched_at);
        dest.writeInt(this.is_finished);
        dest.writeString(this.number);
        dest.writeFloat(this.total_amount);
        dest.writeFloat(this.amount);
        dest.writeFloat(this.second_payment);
        dest.writeParcelable(this.shop, flags);
        dest.writeString(this.service_mode);
        dest.writeString(this.service_date);
        dest.writeString(this.remark);
        dest.writeString(this.serial);
        dest.writeLong(this.pay_time);
    }

    protected OrderInfoEntity(Parcel in) {
        this.id = in.readString();
        this.order_id = in.readString();
        this.category = in.readString();
        this.created_at = in.readLong();
        this.service = in.readParcelable(ServiceEntity.class.getClassLoader());
        this.address = in.readParcelable(AddressEntity.class.getClassLoader());
        this.price = in.readString();
        this.service_time = in.readString();
        this.status = in.readInt();
        this.is_dispatch = in.readString();
        this.receiver = in.readString();
        this.accept_at = in.readLong();
        this.order_no = in.readString();
        this.phone = in.readString();
        this.pay_type = in.readString();
        this.pay_status = in.readString();
        this.dispatched_at = in.readLong();
        this.is_finished = in.readInt();
        this.number = in.readString();
        this.total_amount = in.readFloat();
        this.amount = in.readFloat();
        this.second_payment = in.readFloat();
        this.shop = in.readParcelable(ShopBean.class.getClassLoader());
        this.service_mode = in.readString();
        this.service_date = in.readString();
        this.remark = in.readString();
        this.serial = in.readString();
        this.pay_time = in.readLong();
    }

    public static final Creator<OrderInfoEntity> CREATOR = new Creator<OrderInfoEntity>() {
        @Override
        public OrderInfoEntity createFromParcel(Parcel source) {
            return new OrderInfoEntity(source);
        }

        @Override
        public OrderInfoEntity[] newArray(int size) {
            return new OrderInfoEntity[size];
        }
    };
}
