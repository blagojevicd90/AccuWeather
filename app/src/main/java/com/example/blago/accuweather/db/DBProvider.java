package com.example.blago.accuweather.db;

import android.arch.persistence.room.Room;
import android.content.Context;

public class DBProvider {
    private static DBProvider mInstance;
    private WeatherDataBase mDb;

    public static DBProvider getInstance(Context context) {
        if (mInstance == null) {
            synchronized (DBProvider.class) {
                if (mInstance == null) {
                    mInstance = new DBProvider(context);
                }
            }
        }
        return mInstance;
    }

    private DBProvider(Context context) {
        mDb = Room.databaseBuilder(context, WeatherDataBase.class, "weather_result")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries().build();
    }

    public WeatherDataBase getmDb() {
        return mDb;
    }
}
