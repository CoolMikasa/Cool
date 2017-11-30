package com.coolmikasa.android.com.coolmikasa.android.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.coolmikasa.android.WeatherActivity;
import com.coolmikasa.android.gson.Weather;
import com.coolmikasa.android.util.HttpUtil;
import com.coolmikasa.android.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateService extends Service {
    public AutoUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int time = 8 * 60 * 60 * 1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + time;
        Intent i = new Intent(this, AutoUpdateService.class);
        PendingIntent pi =PendingIntent.getService(this, 0 , i , 0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateWeather() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);

        if (weatherString != null) {//有缓存 直接处理解析出weatherId 然后查询天气
            Weather weather = Utility.handleWeatherResponse(weatherString);
            String weatherId = weather.basic.weatherId;
            String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=e15dd02122344410a2a32e23222b5cb7";
            HttpUtil.SendOKHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseText = response.body().string();
                    Weather newWeather = Utility.handleWeatherResponse(responseText);
                    if (newWeather != null && newWeather.status.equals("ok")) {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).
                                edit();
                        editor.putString("weather", responseText);//保存未经处理的返回数据文本到.xml文件中
                        editor.apply();
                    }

                }
            });
        }
    }
}