package com.wuyou.worker.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by Administrator on 2018\1\25 0025.
 */
@Entity
public class UserInfo {
    @Id
    private long mid;
    @Property(nameInDb = "USERNAME")
    private String worker_name;
    @Property(nameInDb = "PHONE")
    private String phone;
    @Property(nameInDb = "UID")
    private String worker_id;
    @Property(nameInDb = "HEAD")
    private String avatar;
    @Property(nameInDb = "TOKEN")
    private String token;
    @Property(nameInDb = "PWD")
    private String password;
    @Property(nameInDb = "RC_ID")
    private String rc_id;
    @Property(nameInDb = "RC_TOKEN")
    private String rc_token;
    @Property(nameInDb = "AMOUNT")
    private String amount;

    @Generated(hash = 1712130219)
    public UserInfo(long mid, String worker_name, String phone, String worker_id,
                    String avatar, String token, String password, String rc_id,
                    String rc_token, String amount) {
        this.mid = mid;
        this.worker_name = worker_name;
        this.phone = phone;
        this.worker_id = worker_id;
        this.avatar = avatar;
        this.token = token;
        this.password = password;
        this.rc_id = rc_id;
        this.rc_token = rc_token;
        this.amount = amount;
    }

    @Generated(hash = 1279772520)
    public UserInfo() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getWorker_id() {
        return worker_id;
    }

    public void setWorker_id(String worker_id) {
        this.worker_id = worker_id;
    }

    public String getName() {
        return worker_name;
    }

    public void setName(String name) {
        this.worker_name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getMid() {
        return this.mid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMid(long mid) {

        this.mid = mid;
    }

    public String getWorker_name() {
        return this.worker_name;
    }

    public void setWorker_name(String worker_name) {
        this.worker_name = worker_name;
    }

    public String getRc_id() {
        return this.rc_id;
    }

    public void setRc_id(String rc_id) {
        this.rc_id = rc_id;
    }

    public String getRc_token() {
        return this.rc_token;
    }

    public void setRc_token(String rc_token) {
        this.rc_token = rc_token;
    }

    public String getAmount() {
        return this.amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
