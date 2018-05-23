package com.wuyou.worker.bean.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hjn on 2018/3/8.
 */

public class AddressBean implements Parcelable {
    public String id;
    public String city_id;
    public String city_name;
    public String area;
    public String address;
    public Double lat;
    public Double lng;
    public String name;
    public String mobile;
    public String district;
    public int is_default;

    public AddressBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.city_id);
        dest.writeString(this.city_name);
        dest.writeString(this.area);
        dest.writeString(this.address);
        dest.writeValue(this.lat);
        dest.writeValue(this.lng);
        dest.writeString(this.name);
        dest.writeString(this.mobile);
        dest.writeString(this.district);
        dest.writeInt(this.is_default);
    }

    protected AddressBean(Parcel in) {
        this.id = in.readString();
        this.city_id = in.readString();
        this.city_name = in.readString();
        this.area = in.readString();
        this.address = in.readString();
        this.lat = (Double) in.readValue(Double.class.getClassLoader());
        this.lng = (Double) in.readValue(Double.class.getClassLoader());
        this.name = in.readString();
        this.mobile = in.readString();
        this.district = in.readString();
        this.is_default = in.readInt();
    }

    public static final Creator<AddressBean> CREATOR = new Creator<AddressBean>() {
        @Override
        public AddressBean createFromParcel(Parcel source) {
            return new AddressBean(source);
        }

        @Override
        public AddressBean[] newArray(int size) {
            return new AddressBean[size];
        }
    };
}
