package com.example.blago.accuweather.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;



import java.util.List;
@Entity(tableName = "weather_result")
public class WeatherResult implements Parcelable{

    public WeatherResult(Parcel in) {
        base = in.readString();
        dt = in.readInt();
        id = in.readInt();
        name = in.readString();
        cod = in.readInt();
    }

    public static final Creator<WeatherResult> CREATOR = new Creator<WeatherResult>() {
        @Override
        public WeatherResult createFromParcel(Parcel in) {
            return new WeatherResult(in);
        }

        @Override
        public WeatherResult[] newArray(int size) {
            return new WeatherResult[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(base);
        dest.writeInt(dt);
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(cod);
    }
    @Ignore
    private Coord coord;
    @Ignore
    private List<Weather> weather;
    private String base;
    @Ignore
    private Main main;
    @Ignore
    private Wind wind;
    @Ignore
    private Clouds clouds;
    private int dt;
    @Ignore
    private Sys sys;
    @PrimaryKey
    private int id;
    private String name;
    private int cod;


    public WeatherResult() {

    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public int getDt() {
        return dt;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }

    public Sys getSys() {
        return sys;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }


}
