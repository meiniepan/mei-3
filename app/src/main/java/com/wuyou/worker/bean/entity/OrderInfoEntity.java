package com.wuyou.worker.bean.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by solang on 2018/2/8.
 */

public class OrderInfoEntity implements Parcelable {
    public String id;
    public String category;
    public long created_at;
    public String address;
    public String price;
    public String service_time;
    public String status;
    public String is_dispatch;
    public String receiver;
    public long accept_at;
    public String order_num;
    public String phone;
    public String pay_type;
    public String pay_status;
    public int position;

    public OrderInfoEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.category);
        dest.writeLong(this.created_at);
        dest.writeString(this.address);
        dest.writeString(this.price);
        dest.writeString(this.service_time);
        dest.writeString(this.status);
        dest.writeString(this.is_dispatch);
        dest.writeString(this.receiver);
        dest.writeLong(this.accept_at);
        dest.writeString(this.order_num);
        dest.writeString(this.phone);
        dest.writeString(this.pay_type);
        dest.writeString(this.pay_status);
        dest.writeInt(this.position);
    }

    protected OrderInfoEntity(Parcel in) {
        this.id = in.readString();
        this.category = in.readString();
        this.created_at = in.readLong();
        this.address = in.readString();
        this.price = in.readString();
        this.service_time = in.readString();
        this.status = in.readString();
        this.is_dispatch = in.readString();
        this.receiver = in.readString();
        this.accept_at = in.readLong();
        this.order_num = in.readString();
        this.phone = in.readString();
        this.pay_type = in.readString();
        this.pay_status = in.readString();
        this.position = in.readInt();
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
