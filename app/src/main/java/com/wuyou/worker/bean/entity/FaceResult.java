package com.wuyou.worker.bean.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DELL on 2018/4/3.
 */

public class FaceResult {

    /**
     * time_used : 473
     * confidence : 96.46
     * thresholds : {"1e-3":65.3,"1e-5":76.5,"1e-4":71.8}
     * request_id : 1469761507,07174361-027c-46e1-811f-ba0909760b18
     */

    public int time_used;
    public double confidence;
    public ThresholdsBean thresholds;
    public String request_id;

    public static class ThresholdsBean {
        /**
         * 1e-3 : 65.3
         * 1e-5 : 76.5
         * 1e-4 : 71.8
         */

        @SerializedName("1e-3")
        public double _$1e3;
        @SerializedName("1e-5")
        public double _$1e5;
        @SerializedName("1e-4")
        public double _$1e4;
    }
}
