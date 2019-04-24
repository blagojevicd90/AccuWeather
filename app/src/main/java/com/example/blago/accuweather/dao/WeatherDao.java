package com.example.blago.accuweather.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.blago.accuweather.Common.Common;
import com.example.blago.accuweather.Model.WeatherForcastResult;
import com.example.blago.accuweather.Model.WeatherResult;

import java.util.List;

@Dao
public interface WeatherDao {

    @Query("SELECT * FROM weather_result")
    List<WeatherResult> getAllWeatherResult();

    @Insert
    void insertAll(WeatherResult... weatherResults);

    @Delete
    void delete(WeatherResult weatherResult);

    @Query("SELECT * FROM common")
    List<Common> getCommons();

    @Insert
    void insertCommon(Common... commons);

    @Delete
    void deleteCommon(Common commons);

}
