package com.wuyou.worker.bean.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DELL on 2018/3/25.
 */

public class ServiceEntity implements Parcelable {
    public String service_id;
    public String service_name;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.service_id);
        dest.writeString(this.service_name);
    }

    public ServiceEntity() {
    }

    protected ServiceEntity(Parcel in) {
        this.service_id = in.readString();
        this.service_name = in.readString();
    }

    public static final Parcelable.Creator<ServiceEntity> CREATOR = new Parcelable.Creator<ServiceEntity>() {
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
