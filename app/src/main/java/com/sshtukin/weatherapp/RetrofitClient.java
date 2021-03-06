package com.sshtukin.weatherapp;

import com.sshtukin.weatherapp.model.ClearedWeather;
import com.sshtukin.weatherapp.model.Weather;
import com.sshtukin.weatherapp.model.WeatherList;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private String api_key ="7bccd542feed844db9955824b4eb2820";
    private String TAG = "RetrofitClient";

    private Retrofit getInstance() {
        Retrofit instance = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return instance;
    }

    public List<ClearedWeather> clearData(List<WeatherList> result_list){

        final List<ClearedWeather> clearedWeatherList = new ArrayList<>();
        int i = 0;
        while(i < result_list.size()){
            ClearedWeather clearedWeather = new ClearedWeather();

            Date date = new Date(result_list.get(i).getDt() * 1000L);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            Date next_date = new Date(result_list.get(i+1).getDt()* 1000L);
            Calendar next_calendar = Calendar.getInstance();
            next_calendar.setTime(next_date);
            int max_temp = -100;
            int min_temp = 100;
            int saved_i = 0;

            for (; calendar.get(Calendar.DAY_OF_WEEK) == next_calendar.get(Calendar.DAY_OF_WEEK); i++){
                if (i >= result_list.size()){
                    break;
                }
                if (max_temp < result_list.get(i).getMain().getTemp()){
                    max_temp = result_list.get(i).getMain().getTemp().intValue();
                    saved_i = i;
                }
                if (min_temp > result_list.get(i).getMain().getTemp()){
                    min_temp = result_list.get(i).getMain().getTemp().intValue();
                }
                next_date = new Date(result_list.get(i).getDt()* 1000L);
                next_calendar.setTime(next_date);

            }
            if ((calendar.get(Calendar.DAY_OF_WEEK) != next_calendar.get(Calendar.DAY_OF_WEEK)) && (i ==0 )){
                i++;
            }

            clearedWeather.setDay(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US));
            clearedWeather.setMaxTemp(max_temp);
            clearedWeather.setMinTemp(min_temp);
            clearedWeather.setDescription(result_list.get(saved_i).getWeather().get(0).getDescription());
            String icon_url= "http://openweathermap.org/img/w/" + result_list.get(saved_i).getWeather().get(0).getIcon() + ".png";
            clearedWeather.setImage(icon_url);
            clearedWeatherList.add(clearedWeather);
        }
        return clearedWeatherList;
    }


    public Call<Weather> getCall(String lat, String lot) {
        Retrofit retrofit = getInstance();
        IOpenWeatherMap service = retrofit.create(IOpenWeatherMap.class);
        Call<Weather> call = service.getWeatherByLatLng(lat, lot, api_key, "metric");
        return call;
    }
}
