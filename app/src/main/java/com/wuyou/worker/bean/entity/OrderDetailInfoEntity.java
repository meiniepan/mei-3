package com.wuyou.worker.bean.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by solang on 2018/2/8.
 */

public class OrderDetailInfoEntity implements Parcelable {
    public String id;
    public String order_id;
    public String category;
    public long created_at;
    public List<ServiceEntity> service;
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
    public int number;
    public float total_amount;
    public float amount;
    public float second_payment;
    public ShopBean shop;
    public String service_mode;
    public String service_date;
    public String remark;
    public String serial;
    public long pay_time;
    public long dispatch_at;
    public long second_pay_time;
    public int service_time_is_changed;
    public ServeSpecificationEntity specification;

    public OrderDetailInfoEntity() {
    }


    protected OrderDetailInfoEntity(Parcel in) {
        id = in.readString();
        order_id = in.readString();
        category = in.readString();
        created_at = in.readLong();
        service = in.createTypedArrayList(ServiceEntity.CREATOR);
        address = in.readParcelable(AddressEntity.class.getClassLoader());
        price = in.readString();
        service_time = in.readString();
        status = in.readInt();
        is_dispatch = in.readString();
        receiver = in.readString();
        accept_at = in.readLong();
        order_no = in.readString();
        phone = in.readString();
        pay_type = in.readString();
        pay_status = in.readString();
        dispatched_at = in.readLong();
        is_finished = in.readInt();
        number = in.readInt();
        total_amount = in.readFloat();
        amount = in.readFloat();
        second_payment = in.readFloat();
        shop = in.readParcelable(ShopBean.class.getClassLoader());
        service_mode = in.readString();
        service_date = in.readString();
        remark = in.readString();
        serial = in.readString();
        pay_time = in.readLong();
        dispatch_at = in.readLong();
        service_time_is_changed = in.readInt();
        specification = in.readParcelable(ServeSpecificationEntity.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(order_id);
        dest.writeString(category);
        dest.writeLong(created_at);
        dest.writeTypedList(service);
        dest.writeParcelable(address, flags);
        dest.writeString(price);
        dest.writeString(service_time);
        dest.writeInt(status);
        dest.writeString(is_dispatch);
        dest.writeString(receiver);
        dest.writeLong(accept_at);
        dest.writeString(order_no);
        dest.writeString(phone);
        dest.writeString(pay_type);
        dest.writeString(pay_status);
        dest.writeLong(dispatched_at);
        dest.writeInt(is_finished);
        dest.writeInt(number);
        dest.writeFloat(total_amount);
        dest.writeFloat(amount);
        dest.writeFloat(second_payment);
        dest.writeParcelable(shop, flags);
        dest.writeString(service_mode);
        dest.writeString(service_date);
        dest.writeString(remark);
        dest.writeString(serial);
        dest.writeLong(pay_time);
        dest.writeLong(dispatch_at);
        dest.writeInt(service_time_is_changed);
        dest.writeParcelable(specification, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderDetailInfoEntity> CREATOR = new Creator<OrderDetailInfoEntity>() {
        @Override
        public OrderDetailInfoEntity createFromParcel(Parcel in) {
            return new OrderDetailInfoEntity(in);
        }

        @Override
        public OrderDetailInfoEntity[] newArray(int size) {
            return new OrderDetailInfoEntity[size];
        }
    };
}
