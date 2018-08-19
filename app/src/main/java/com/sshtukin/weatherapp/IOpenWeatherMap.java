package com.sshtukin.weatherapp;

import com.sshtukin.weatherapp.model.Weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IOpenWeatherMap {
    @GET("forecast")
    Call<Weather> getWeatherByLatLng(@Query("lat") String lat,
                                     @Query("lon") String lot,
                                     @Query("appid") String appid,
                                     @Query("units") String units);
}
