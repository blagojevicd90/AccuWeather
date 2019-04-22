package com.example.blago.accuweather.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.blago.accuweather.Model.WeatherResult;
import com.example.blago.accuweather.R;

import java.util.Calendar;
import java.util.List;

public class WidgetActivityAdapter extends RecyclerView.Adapter<WidgetActivityAdapter.MyViewHolder> {

    Context context;
    List<WeatherResult> weatherResult;
    private int lastSelectedPosition = -1;


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
    public void onBindViewHolder(@NonNull WidgetActivityAdapter.MyViewHolder myViewHolder, int i) {
        myViewHolder.txt_location.setText(weatherResult.get(i).getName());
        myViewHolder.radioButton.setChecked(lastSelectedPosition == i);

    }

    @Override
    public int getItemCount() {
        return weatherResult.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_location;
        RadioButton radioButton;
        LinearLayout linearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_location = (TextView) itemView.findViewById(R.id.txt_location);
            radioButton = (RadioButton) itemView.findViewById(R.id.img_circle);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.widget_item);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        }
    }
}
