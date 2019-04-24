package com.example.blago.accuweather.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.blago.accuweather.Common.Common;
import com.example.blago.accuweather.Model.WeatherResult;
import com.example.blago.accuweather.dao.WeatherDao;


@Database(entities = {WeatherResult.class, Common.class},
        version = 1)

public abstract class WeatherDataBase extends RoomDatabase {
    public abstract WeatherDao weatherDao();
}
