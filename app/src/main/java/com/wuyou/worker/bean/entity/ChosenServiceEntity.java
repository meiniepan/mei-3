package com.wuyou.worker.bean.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Solang on 2018/7/10.
 */

public class ChosenServiceEntity implements Parcelable{
    public String id;
    public String subId;
    public String title;
    public String spec;
    public Float price;
    public String photo;
    public int number;

    public ChosenServiceEntity(Parcel in) {
        id = in.readString();
        subId = in.readString();
        title = in.readString();
        spec = in.readString();
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readFloat();
        }
        photo = in.readString();
        number = in.readInt();
    }

    public static final Creator<ChosenServiceEntity> CREATOR = new Creator<ChosenServiceEntity>() {
        @Override
        public ChosenServiceEntity createFromParcel(Parcel in) {
            return new ChosenServiceEntity(in);
        }

        @Override
        public ChosenServiceEntity[] newArray(int size) {
            return new ChosenServiceEntity[size];
        }
    };

    public ChosenServiceEntity() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(subId);
        dest.writeString(title);
        dest.writeString(spec);
        if (price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(price);
        }
        dest.writeString(photo);
        dest.writeInt(number);
    }
}
