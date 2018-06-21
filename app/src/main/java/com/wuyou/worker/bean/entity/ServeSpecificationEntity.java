package com.wuyou.worker.bean.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DELL on 2018/3/13.
 */

public class ServeSpecificationEntity implements Parcelable, Cloneable {
    public String id;
    public String name;
    public float price;
    public int stock;
    public String photo;
    public String sales;

    public ServeSpecificationEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeFloat(this.price);
        dest.writeInt(this.stock);
        dest.writeString(this.photo);
        dest.writeString(this.sales);
    }

    protected ServeSpecificationEntity(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.price = in.readFloat();
        this.stock = in.readInt();
        this.photo = in.readString();
        this.sales = in.readString();
    }

    public static final Creator<ServeSpecificationEntity> CREATOR = new Creator<ServeSpecificationEntity>() {
        @Override
        public ServeSpecificationEntity createFromParcel(Parcel source) {
            return new ServeSpecificationEntity(source);
        }

        @Override
        public ServeSpecificationEntity[] newArray(int size) {
            return new ServeSpecificationEntity[size];
        }
    };
}

