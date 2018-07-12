package com.wuyou.worker.bean.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DELL on 2018/3/25.
 */

public class ServiceEntity implements Parcelable {
    public String service_id;
    public String service_name;
    public String image;
    public int number;
    public String has_specification;
    public ChosenServiceEntity specification;
    public String title;
    public String stage;
    public float amount;
    public String photo;
    public float visiting_fee;
    public float price;

    public ServiceEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.service_id);
        dest.writeString(this.service_name);
        dest.writeString(this.title);
        dest.writeString(this.photo);
        dest.writeFloat(this.visiting_fee);
        dest.writeFloat(this.price);
    }

    protected ServiceEntity(Parcel in) {
        this.service_id = in.readString();
        this.service_name = in.readString();
        this.title = in.readString();
        this.photo = in.readString();
        this.visiting_fee = in.readFloat();
        this.price = in.readFloat();
    }

    public static final Creator<ServiceEntity> CREATOR = new Creator<ServiceEntity>() {
        @Override
        public ServiceEntity createFromParcel(Parcel source) {
            return new ServiceEntity(source);
        }

        @Override
        public ServiceEntity[] newArray(int size) {
            return new ServiceEntity[size];
        }
    };
}
