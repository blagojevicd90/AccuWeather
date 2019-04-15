package com.example.blago.accuweather.Retrofit;

import com.example.blago.accuweather.Model.WeatherForcastResult;
import com.example.blago.accuweather.Model.WeatherResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherMap {

    @GET("weather")
    Observable<WeatherResult> getWeatherByLatLng(@Query("lat") String lat,
                                                 @Query("lon") String lng,
                                                 @Query("appid") String appid,
                                                 @Query("units") String unit);


    @GET("forecast")
    Observable<WeatherForcastResult> getForecastWeatherByLatLng(@Query("lat") String lat,
                                                                @Query("lon") String lng,
                                                                @Query("appid") String appid,
                                                                @Query("units") String unit);


    @GET("weather")
    Observable<WeatherResult> getWeatherByCityName(@Query("q") String name,
                                                   @Query("appid") String appid,
                                                   @Query("units") String unit);


    @GET("forecast")
    Observable<WeatherForcastResult> getForecastWeatherByName(@Query("q") String name,
                                                              @Query("appid") String appid,
                                                              @Query("units") String unit);

}
