package com.coolmikasa.android.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Lee on 2017/11/26/026.
 */

public class Weather {
    public String status;
    public Basic basic;
    public Aqi aqi;
    public Now now;
    public Suggestion suggestion;
    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
}
