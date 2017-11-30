package com.coolmikasa.android.util;


import android.text.TextUtils;

import com.coolmikasa.android.db.City;
import com.coolmikasa.android.db.County;
import com.coolmikasa.android.db.Province;
import com.coolmikasa.android.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utility {
    /**
     *解析和处理服务器返回的省级数据
     */
    public static boolean handleProvinceResponse(String response){
        if (!TextUtils.isEmpty(response)){
            try {//使用JSON解析
                JSONArray allProvinces = new JSONArray(response);//服务器返回的是JSON数组 处理过后成response字符串 再转换成JSON数组
                for (int i = 0; i < allProvinces.length(); i++){
                    JSONObject provinceObject = allProvinces.getJSONObject(i);//取出JSON数组中的一个JSONObject
                    Province province = new Province();//创建实体类对象 为了保存将要解析出来的数据
                    province.setProvinceName(provinceObject.getString("name"));//这里要手动为实体类对象的成员变量赋值
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();//保存到数据库中
                }
                return true;//返回true表示处理成功
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;

    }

    public static boolean handleCityResponse(String response, int provinceId){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allCities = new JSONArray(response);
                for (int i = 0; i < allCities.length(); i++){
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean handleCountyResponse(String response, int cityId){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allCounties = new JSONArray(response);
                for (int i = 0; i < allCounties.length(); i++){
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static Weather handleWeatherResponse(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent, Weather.class);//创建实体类对象 仍旧在内存中 还需持久化操作

        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

}
