package com.coolmikasa.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Lee on 2017/11/26/026.
 */

public class Forecast{
    public String date;
    @SerializedName("cond")
    public More more;
    @SerializedName("tmp")
    public Temperature temperature;
    public class More{
        @SerializedName("txt_d")
        public String info;
    }
    public class Temperature{
        public String max;
        public String min;
    }
}
