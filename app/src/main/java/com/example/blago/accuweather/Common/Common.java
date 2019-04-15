package com.example.blago.accuweather.Common;

import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Common {

    public static final String API_KEY = "60d5387adf37033dda635ca536d16ebf";
    public static Location current_location = null;
    private ArrayList <String> city_name;


    public static String convertUnixToDate(long dt) {
        Date date = new Date(dt*1000L);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm dd EEE MM yyyy");
        String formatted = simpleDateFormat.format(date);

        return formatted;
    }

    public static String convertUnixToHour(long dt) {
        Date date = new Date(dt*1000L);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String formatted = simpleDateFormat.format(date);

        return formatted;
    }

    public static String convertUnix(long dt) {
        Date date = new Date(dt*1000L);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("H");
        String formatted = simpleDateFormat.format(date);

        return formatted;
    }


    public static String convertUnixToDay(long dt) {
        Date date = new Date(dt*1000L);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE");
        String formatted = simpleDateFormat.format(date);

        return formatted;
    }

    public ArrayList<String> getCity_name() {
        if(city_name==null){
            city_name = new ArrayList<>();
        }
        return city_name;
    }
}
