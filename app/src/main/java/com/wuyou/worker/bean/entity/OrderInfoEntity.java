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
    public String status;
    public String is_dispatch;
    public String receiver;
    public long accept_at;
    public String order_no;
    public String phone;
    public String pay_type;
    public String pay_status;
    public long dispatched_at;
    public int position;
    public int is_finished;

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
        dest.writeString(this.status);
        dest.writeString(this.is_dispatch);
        dest.writeString(this.receiver);
        dest.writeLong(this.accept_at);
        dest.writeString(this.order_no);
        dest.writeString(this.phone);
        dest.writeString(this.pay_type);
        dest.writeString(this.pay_status);
        dest.writeLong(this.dispatched_at);
        dest.writeInt(this.position);
        dest.writeInt(this.is_finished);
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
        this.status = in.readString();
        this.is_dispatch = in.readString();
        this.receiver = in.readString();
        this.accept_at = in.readLong();
        this.order_no = in.readString();
        this.phone = in.readString();
        this.pay_type = in.readString();
        this.pay_status = in.readString();
        this.dispatched_at = in.readLong();
        this.position = in.readInt();
        this.is_finished = in.readInt();
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
