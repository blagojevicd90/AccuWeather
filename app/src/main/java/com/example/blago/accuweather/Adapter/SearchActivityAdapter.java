package com.example.blago.accuweather.Adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.blago.accuweather.Model.WeatherResult;
import com.example.blago.accuweather.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchActivityAdapter extends RecyclerView.Adapter<SearchActivityAdapter.MyViewHolder> {
    Context context;
    List<WeatherResult> weatherResult;


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
        String temperature = String.valueOf(weatherResult.get(i).getMain().getTemp());

        myViewHolder.txt_city_name.setText(weatherResult.get(i).getName());
        myViewHolder.txt_temperature.setText(temperature.substring(0, temperature.indexOf(".") + 2) + "°C");

        Picasso.get().load(new StringBuilder("https://openweathermap.org/img/w/")
                .append(weatherResult.get(i).getWeather().get(0).getIcon())
                .append(".png").toString()).into(myViewHolder.img_weather);
    }

    @Override
    public int getItemCount() {
        return weatherResult.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_city_name, txt_temperature;
        public ImageView img_weather;
        public RelativeLayout foreground, background;


        public MyViewHolder(final View itemView) {
            super(itemView);
            txt_city_name = (TextView) itemView.findViewById(R.id.txt_city_name);
            txt_temperature = (TextView) itemView.findViewById(R.id.temperature);
            img_weather = (ImageView) itemView.findViewById(R.id.img_weather);
            foreground = (RelativeLayout) itemView.findViewById(R.id.view_foreground);
            background = (RelativeLayout) itemView.findViewById(R.id.background);
        }
    }
}



