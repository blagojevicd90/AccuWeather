package com.example.blago.accuweather;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blago.accuweather.Adapter.ViewPagerAdapter;
import com.example.blago.accuweather.Adapter.WeatherForecastAdapter;
import com.example.blago.accuweather.Common.Common;
import com.example.blago.accuweather.Model.WeatherForcastResult;
import com.example.blago.accuweather.Model.WeatherResult;
import com.example.blago.accuweather.Retrofit.OpenWeatherMap;
import com.example.blago.accuweather.Retrofit.RetrofitClient;
import com.github.ahmadnemati.wind.WindView;
import com.github.ahmadnemati.wind.enums.TrendType;
import com.squareup.picasso.Picasso;


import java.util.Calendar;

import io.netopen.hotbitmapgg.library.view.RingProgressBar;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


public class TodayWeatherFragment extends Fragment {

    private TextView txt_temperature, txt_description, txt_city_name, txt_sunrise, txt_sunset;
    private RingProgressBar ringProgressBar;
    private WindView windView;
    private NestedScrollView scrollView;
    private LinearLayout rootLayout;
    private ImageButton imageButton;
    private RecyclerView recycler_forecast;
    private Calendar calendar;

    CompositeDisposable compositeDisposable;
    OpenWeatherMap mService;


    static TodayWeatherFragment instance;

    public static TodayWeatherFragment getInstance() {

        instance = new TodayWeatherFragment();

        return instance;
    }


    public TodayWeatherFragment() {
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        mService = retrofit.create(OpenWeatherMap.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View itemView = inflater.inflate(R.layout.fragment_today_weather, container, false);

        initComponents(itemView);
        getWeatherInformation();
        getForecastWeatherInformation();
        addListener();


        return itemView;


    }

    private void getWeatherInformation() {
        compositeDisposable.add(mService.getWeatherByLatLng(String.valueOf(Common.current_location.getLatitude()),
                String.valueOf(Common.current_location.getLongitude()),
                Common.API_KEY,
                "metric")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherResult>() {
                    @Override
                    public void accept(WeatherResult weatherResult) throws Exception {
                        fillData(weatherResult);
                        setBackgroundOnScreen(weatherResult);
                        rootLayout.setVisibility(View.VISIBLE);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(getActivity(), "" + throwable.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
        );
    }

    private void fillData(WeatherResult weatherResult) {
        txt_city_name.setText(weatherResult.getName());
        txt_description.setText(weatherResult.getWeather().get(0).getDescription());
        txt_temperature.setText(new StringBuilder
                (String.valueOf(weatherResult.getMain().getTemp())).append("°C").toString());
        txt_sunrise.setText("Sunrise: " + Common.convertUnixToHour(weatherResult.getSys().getSunrise()));
        txt_sunset.setText("Sunset: " + Common.convertUnixToHour(weatherResult.getSys().getSunset()));
        ringProgressBar.setProgress(weatherResult.getMain().getHumidity());
        windView.setWindSpeed((float) weatherResult.getWind().getSpeed());
        windView.setPressure(weatherResult.getMain().getPressure());
        windView.setTrendType(TrendType.UP);
        windView.start();
    }

    private void addListener() {
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
                getActivity().finish();

            }
        });
    }

    private void getForecastWeatherInformation() {
        compositeDisposable.add(mService.getForecastWeatherByLatLng(
                String.valueOf(Common.current_location.getLatitude()),
                String.valueOf(Common.current_location.getLongitude()),
                Common.API_KEY,
                "metric")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherForcastResult>() {
                    @Override
                    public void accept(WeatherForcastResult weatherForcastResult) throws Exception {
                        displayForecastWeather(weatherForcastResult);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("ERROR", "" + throwable.getMessage());
                    }
                })
        );
    }

    private void displayForecastWeather(WeatherForcastResult weatherForcastResult) {
        WeatherForecastAdapter adapter = new WeatherForecastAdapter(getContext(), weatherForcastResult);
        recycler_forecast.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void initComponents(View itemView) {
        scrollView = (NestedScrollView) itemView.findViewById(R.id.scrollView);
        txt_temperature = (TextView) itemView.findViewById(R.id.txt_temperature);
        txt_sunrise = (TextView) itemView.findViewById(R.id.txt_sunrise);
        txt_sunset = (TextView) itemView.findViewById(R.id.txt_sunset);
        txt_description = (TextView) itemView.findViewById(R.id.txt_description);
        txt_city_name = (TextView) itemView.findViewById(R.id.txt_city_name);
        rootLayout = (LinearLayout) itemView.findViewById(R.id.root_layout);
        recycler_forecast = (RecyclerView) itemView.findViewById(R.id.recycler_view);
        recycler_forecast.setHasFixedSize(true);
        recycler_forecast.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.HORIZONTAL, false));
        imageButton = (ImageButton) itemView.findViewById(R.id.image_button);
        ringProgressBar = (RingProgressBar) itemView.findViewById(R.id.circle_progress_bar);
        ringProgressBar.setMax(100);
        windView = (WindView) itemView.findViewById(R.id.windView);
        windView.setWindSpeedUnit(" km/h");
        windView.setPressureUnit(" in hpa");
        calendar = Calendar.getInstance();

    }

    private void setBackgroundOnScreen(WeatherResult weatherResult) {
        int current_time = calendar.get(Calendar.HOUR_OF_DAY);
        int sunset = Integer.parseInt(Common.convertUnix(weatherResult.getSys().getSunrise()));
        int sunrise = Integer.parseInt(Common.convertUnix(weatherResult.getSys().getSunset()));
        if (current_time >= sunset && current_time < sunrise) {
            scrollView.setBackgroundResource(R.drawable.after_noon);
        } else {
            scrollView.setBackgroundResource(R.drawable.night);
        }
    }
}