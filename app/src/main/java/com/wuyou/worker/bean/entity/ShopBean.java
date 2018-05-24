package com.wuyou.worker.bean.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DELL on 2018/3/19.
 */

public class ShopBean implements Parcelable {
    public String shop_name;
    public String shop_id;
    public String shop_tel;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.shop_name);
        dest.writeString(this.shop_id);
        dest.writeString(this.shop_tel);
    }

    public ShopBean() {
    }

    protected ShopBean(Parcel in) {
        this.shop_name = in.readString();
        this.shop_id = in.readString();
        this.shop_tel = in.readString();
    }

    public static final Creator<ShopBean> CREATOR = new Creator<ShopBean>() {
        @Override
        public ShopBean createFromParcel(Parcel source) {
            return new ShopBean(source);
        }

        @Override
        public ShopBean[] newArray(int size) {
            return new ShopBean[size];
        }
    };
}
