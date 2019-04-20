package com.example.blago.accuweather.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.blago.accuweather.Common.Common;
import com.example.blago.accuweather.Model.WeatherForcastResult;
import com.example.blago.accuweather.R;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class WeatherForecastAdapter extends RecyclerView.Adapter<WeatherForecastAdapter.MyViewHolder> {

    Context context;
    WeatherForcastResult weatherForcastResult;

    public WeatherForecastAdapter(Context context, WeatherForcastResult weatherForcastResult) {
        this.context = context;
        this.weatherForcastResult = weatherForcastResult;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_weather_forecast, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String temperature;

        Picasso.get().load(new StringBuilder("https://openweathermap.org/img/w/")
                .append(weatherForcastResult.list.get(position).weather.get(0).getIcon())
                .append(".png").toString()).into(holder.img_weather);

        temperature = String.valueOf(weatherForcastResult.list.get(position).main.getTemp());
        if(Common.temp_unit.equalsIgnoreCase("metric")) {
            holder.txt_temperature.setText(temperature.substring(0, temperature.indexOf(".")) + "°C");
        }else {
            holder.txt_temperature.setText(temperature.substring(0, temperature.indexOf(".")) + "°F");
        }
        holder.txt_time.setText(new StringBuilder(Common.convertUnixToHour(weatherForcastResult.list.get(position).dt)));
        holder.txt_day_of_week.setText(new StringBuilder(Common.convertUnixToDay(weatherForcastResult.list.get(position).dt)));
        holder.forecast_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://openweathermap.org/city/" + weatherForcastResult.getCity().getId()); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return weatherForcastResult.list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_time, txt_temperature, txt_day_of_week;
        ImageView img_weather;
        LinearLayout forecast_layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            img_weather = (ImageView) itemView.findViewById(R.id.img_weather);
            txt_time = (TextView) itemView.findViewById(R.id.txt_time);
            txt_temperature = (TextView) itemView.findViewById(R.id.txt_temperature);
            txt_day_of_week = (TextView) itemView.findViewById(R.id.txt_day_of_week);
            forecast_layout = (LinearLayout) itemView.findViewById(R.id.forecast_layout);
        }
    }
}
