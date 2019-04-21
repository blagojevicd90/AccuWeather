package com.example.blago.accuweather.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.blago.accuweather.Model.WeatherResult;
import com.example.blago.accuweather.R;
import com.example.blago.accuweather.WeatherWidgetActivity;

import java.util.Calendar;
import java.util.List;

public class WidgetActivityAdapter extends RecyclerView.Adapter<WidgetActivityAdapter.MyViewHolder> {

    Context context;
    List<WeatherResult> weatherResult;
    Calendar calendar = Calendar.getInstance();


    public WidgetActivityAdapter(Context context, List<WeatherResult> weatherResult) {
        this.context = context;
        this.weatherResult = weatherResult;
    }

    @NonNull
    @Override
    public WidgetActivityAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.weather_widget_item, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final WidgetActivityAdapter.MyViewHolder myViewHolder, int i) {
        myViewHolder.txt_location.setText(weatherResult.get(i).getName());

    }

    @Override
    public int getItemCount() {
        return weatherResult.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_location;
        ImageView img_circle;
        LinearLayout linearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_location = (TextView) itemView.findViewById(R.id.txt_location);
            img_circle = (ImageView) itemView.findViewById(R.id.img_circle);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.widget_item);
        }
    }
}
