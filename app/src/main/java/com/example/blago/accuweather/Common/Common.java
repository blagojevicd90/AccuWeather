package com.example.blago.accuweather.Common;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.location.Location;

import org.joda.time.DateTimeUtils;
import org.joda.time.format.DateTimeFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Entity(tableName = "common")
public class Common {

    public static final String API_KEY = "60d5387adf37033dda635ca536d16ebf";
    public static Location current_location = null;
    private String temp_unit;
    private int dialogPosition;
    @PrimaryKey(autoGenerate = true)
    private int id;

    public Common() {
    }

    public int getDialogPosition() {
        return dialogPosition;
    }

    public void setDialogPosition(int dialogPosition) {
        this.dialogPosition = dialogPosition;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTemp_unit() {
        return temp_unit;
    }

    public void setTemp_unit(String temp_unit) {
        this.temp_unit = temp_unit;
    }

    public static String convertUnixToDate(long dt) {
        Date date = new Date(dt * 1000L);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm dd EEE MM yyyy");
        String formatted = simpleDateFormat.format(date);
        return formatted;
    }

    public static String convertUnixToHour(long dt) {
        Date date = new Date(dt * 1000L);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String formatted = simpleDateFormat.format(date);

        return formatted;
    }

    public static String convertUnix(long dt) {
        Date date = new Date(dt * 1000L);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("H");
        String formatted = simpleDateFormat.format(date);

        return formatted;
    }

    public static String convertUnixToDay(long dt) {
        Date date = new Date(dt * 1000L);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE");
        String formatted = simpleDateFormat.format(date);

        return formatted;
    }

    public static String convertUnixToDayTime(long dt) {
        Date date = new Date(dt * 1000L);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd.MM.yyyy");
        String formatted = simpleDateFormat.format(date);
        return formatted;
    }
}
