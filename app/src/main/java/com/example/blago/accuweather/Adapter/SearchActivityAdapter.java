package com.example.blago.accuweather.Adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.blago.accuweather.Common.Common;
import com.example.blago.accuweather.Model.WeatherResult;
import com.example.blago.accuweather.R;

import java.util.Calendar;
import java.util.List;

public class SearchActivityAdapter extends RecyclerView.Adapter<SearchActivityAdapter.MyViewHolder> {
    Context context;
    List<WeatherResult> weatherResult;
    Calendar calendar = Calendar.getInstance();


    public SearchActivityAdapter(Context context, List<WeatherResult> weatherResult) {
        this.context = context;
        this.weatherResult = weatherResult;
    }

    @NonNull
    @Override
    public SearchActivityAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.search_item, parent, false);

        return new MyViewHolder(itemView);
    }


    public void removeItem(int position) {
        weatherResult.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(WeatherResult mWeatherResult, int position) {
        weatherResult.add(position, mWeatherResult);
        notifyItemInserted(position);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int i) {
        String temperature = String.valueOf(weatherResult.get(i).getMain().getTemp()).toString();
        temperature = temperature.substring(0, temperature.indexOf("."));

        myViewHolder.txt_city_name.setText(weatherResult.get(i).getName());
        if (Common.temp_unit.equalsIgnoreCase("metric")) {
            myViewHolder.txt_temperature.setText(temperature + "°C");
        } else {
            myViewHolder.txt_temperature.setText(temperature + "°F");
        }
        myViewHolder.txt_description.setText(weatherResult.get(i).getWeather().get(0).getDescription());


        int current_time = calendar.get(Calendar.HOUR_OF_DAY);
        int sunset = Integer.parseInt(Common.convertUnix(weatherResult.get(i).getSys().getSunrise()));
        int sunrise = Integer.parseInt(Common.convertUnix(weatherResult.get(i).getSys().getSunset()));
        if (current_time >= sunset && current_time < sunrise) {
            myViewHolder.foreground.setBackgroundResource(R.drawable.ic_day);
        } else {
            myViewHolder.foreground.setBackgroundResource(R.drawable.ic_night);
        }


    }

    @Override
    public int getItemCount() {
        return weatherResult.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_city_name, txt_temperature, txt_description;
        public RelativeLayout foreground, background;


        public MyViewHolder(final View itemView) {
            super(itemView);
            txt_city_name = (TextView) itemView.findViewById(R.id.txt_city_name);
            txt_temperature = (TextView) itemView.findViewById(R.id.temperature);
            foreground = (RelativeLayout) itemView.findViewById(R.id.view_foreground);
            background = (RelativeLayout) itemView.findViewById(R.id.background);
            txt_description = (TextView) itemView.findViewById(R.id.txt_description);
        }
    }
}



