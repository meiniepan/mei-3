package com.wuyou.worker.bean.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Solang on 2018/7/10.
 */

public class ChosenServiceEntity implements Parcelable{
    public String service_id;
    public String service_name;
    public String image;
    public int number;
    public String has_specification;
    public String stage;
    public String amount;
    public String visiting_fee;
    public Float price;
    public ServeSpecificationEntity specification;


    public ChosenServiceEntity(Parcel in) {
        service_id = in.readString();
        service_name = in.readString();
        image = in.readString();
        number = in.readInt();
        has_specification = in.readString();
        stage = in.readString();
        amount = in.readString();
        visiting_fee = in.readString();
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readFloat();
        }
        specification = in.readParcelable(ServeSpecificationEntity.class.getClassLoader());
    }

    public ChosenServiceEntity() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(service_id);
        dest.writeString(service_name);
        dest.writeString(image);
        dest.writeInt(number);
        dest.writeString(has_specification);
        dest.writeString(stage);
        dest.writeString(amount);
        dest.writeString(visiting_fee);
        if (price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(price);
        }
        dest.writeParcelable(specification, flags);
    }

    @Override
    public int describeContents() {
        return 0;
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
}
