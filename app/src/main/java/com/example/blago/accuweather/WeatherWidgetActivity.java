package com.example.blago.accuweather;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.example.blago.accuweather.Adapter.WidgetActivityAdapter;
import com.example.blago.accuweather.Common.Common;
import com.example.blago.accuweather.Helper.RecyclerTouchListener;
import com.example.blago.accuweather.Model.WeatherResult;
import com.example.blago.accuweather.Retrofit.OpenWeatherMap;
import com.example.blago.accuweather.Retrofit.RetrofitClient;
import com.example.blago.accuweather.Widget.WeatherWidget;
import com.example.blago.accuweather.db.DBProvider;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class WeatherWidgetActivity extends AppCompatActivity {

    private ImageButton btn_back;
    private TextView txt_date, txt_location, txt_temp, txt_time;
    private ArrayList<WeatherResult> mweatherResults;
    private DBProvider db;
    private WidgetActivityAdapter adapter;
    private RecyclerView recyclerView;
    private RelativeLayout relativeLayout;
    private ImageView img_weather;
    private WeatherResult weatherResult;
    private CompositeDisposable compositeDisposable;
    private OpenWeatherMap mService;
    private Retrofit retrofitClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_widget_layout);

        initComponents();
        getWeatherLocations();
        addListener();
    }

    private void initComponents() {
        compositeDisposable = new CompositeDisposable();
        retrofitClient = RetrofitClient.getInstance();
        weatherResult = new WeatherResult();
        mService = retrofitClient.create(OpenWeatherMap.class);
        btn_back = (ImageButton) findViewById(R.id.btn_back);
        relativeLayout = (RelativeLayout) findViewById(R.id.widget);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_widget);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayout.VERTICAL, false));
        txt_date = (TextView) findViewById(R.id.txt_date_time);
        txt_location = (TextView) findViewById(R.id.txt_location);
        txt_temp = (TextView) findViewById(R.id.txt_temp);
        txt_time = (TextView) findViewById(R.id.txt_time);
        db = DBProvider.getInstance(getApplicationContext());
        mweatherResults = new ArrayList<>();
        img_weather = (ImageView) findViewById(R.id.img_weather);
    }

    private void addListener() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherWidgetActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                fillComponents(position);
                weatherResult = mweatherResults.get(position);
                updateWidget();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    private void getWeatherLocations() {
        ArrayList<WeatherResult> list = new ArrayList<>();
        list.addAll(db.getmDb().weatherDao().getAll());
        for (int i = 0; i < list.size(); i++) {
            compositeDisposable.add(mService.getWeatherByCityName(String.valueOf(list.get(i).getName()),
                    Common.API_KEY,
                    Common.temp_unit)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<WeatherResult>() {
                        @Override
                        public void accept(WeatherResult weatherResult) throws Exception {
                            setAdapter(weatherResult);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                        }
                    })
            );
        }
    }


    private void setAdapter(WeatherResult weatherResult) {
        mweatherResults.add(weatherResult);
        adapter = new WidgetActivityAdapter(getApplicationContext(), mweatherResults);
        recyclerView.setAdapter(adapter);
    }


    private void fillComponents(int position) {
        String temperature = String.valueOf(mweatherResults.get(position).getMain().getTemp()).toString();
        temperature = temperature.substring(0, temperature.indexOf("."));
        txt_location.setText(mweatherResults.get(position).getName());
        txt_time.setText(Common.convertUnixToHour(mweatherResults.get(position).getDt()));
        txt_date.setText(Common.convertUnixToDayTime(mweatherResults.get(position).getDt()));

        if (Common.temp_unit.equalsIgnoreCase("metric")) {
            txt_temp.setText(temperature + "°C");
        } else {
            txt_temp.setText(temperature + "°F");
        }
        Picasso.get().load(new StringBuilder("https://openweathermap.org/img/w/")
                .append(mweatherResults.get(position).getWeather().get(0).getIcon())
                .append(".png").toString()).into(img_weather);
    }

    private void updateWidget(){
        String temperature = String.valueOf(weatherResult.getMain().getTemp()).toString();
        temperature = temperature.substring(0, temperature.indexOf("."));
        Context context = getApplicationContext();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
        ComponentName thisWidget = new ComponentName(context, WeatherWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        remoteViews.setTextViewText(R.id.txt_location, weatherResult.getName());
        remoteViews.setTextViewText(R.id.txt_time, Common.convertUnixToHour(weatherResult.getDt()));
        remoteViews.setTextViewText(R.id.txt_date_time, Common.convertUnixToDayTime(weatherResult.getDt()));
        if(Common.temp_unit.equalsIgnoreCase("metric")){
            remoteViews.setTextViewText(R.id.txt_temp, temperature + "°C");
        }else {
            remoteViews.setTextViewText(R.id.txt_temp, temperature + "°F");
        }
        Picasso.get().load(new StringBuilder("https://openweathermap.org/img/w/")
                .append(weatherResult.getWeather().get(0).getIcon())
                .append(".png").toString()).into(remoteViews, R.id.img_weather, appWidgetIds);

        appWidgetManager.updateAppWidget(thisWidget, remoteViews);
    }
}
