package com.example.blago.accuweather.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.blago.accuweather.Model.WeatherForcastResult;
import com.example.blago.accuweather.Model.WeatherResult;

import java.util.List;

@Dao
public interface WeatherDao {

    @Query("SELECT * FROM weather_result")
    List<WeatherResult> getAll();

    @Insert
    void insertAll(WeatherResult... weatherResults);

    @Delete
    void delete(WeatherResult weatherResult);

}
