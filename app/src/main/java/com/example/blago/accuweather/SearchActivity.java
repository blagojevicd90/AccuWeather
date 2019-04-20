package com.example.blago.accuweather;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blago.accuweather.Adapter.SearchActivityAdapter;
import com.example.blago.accuweather.Common.Common;
import com.example.blago.accuweather.Helper.RecyclerItemTouchHelper;
import com.example.blago.accuweather.Helper.RecyclerItemTouchHelperListener;
import com.example.blago.accuweather.Model.WeatherResult;
import com.example.blago.accuweather.Retrofit.OpenWeatherMap;
import com.example.blago.accuweather.Retrofit.RetrofitClient;
import com.example.blago.accuweather.db.DBProvider;
import com.mancj.materialsearchbar.MaterialSearchBar;


import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class SearchActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener {

    CompositeDisposable compositeDisposable;
    OpenWeatherMap mService;
    private ArrayList<WeatherResult> mWeatherResult;
    private AppBarLayout appBarLayout;
    private ImageButton btn_back;
    private RecyclerView recyclerView;
    private NestedScrollView nestedScrollView;
    private SearchActivityAdapter searchActivityAdapter;
    private Button btn_search;
    private MaterialSearchBar searchBar;
    private DBProvider db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        getmWeatherResult();
        initComponents();
        addListener();
        getWeatherInfromationFromDb();

    }

    private void getWeatherInformationByName(String name) {
        compositeDisposable.add(mService.getWeatherByCityName(name,
                Common.API_KEY,
                Common.temp_unit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherResult>() {
                    @Override
                    public void accept(WeatherResult weatherResult) throws Exception {
                        fillWeatherList(weatherResult);
                        db.getmDb().weatherDao().insertAll(weatherResult);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Snackbar snackbar = Snackbar.make(nestedScrollView, "City with that name doesn't exist", Snackbar.LENGTH_LONG);
                        snackbar.setAction("Try again", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                appBarLayout.setVisibility(View.GONE);
                                searchBar.setVisibility(View.VISIBLE);
                            }
                        });
                        snackbar.setActionTextColor(Color.YELLOW);
                        snackbar.show();
                    }
                })
        );
    }

    private void initComponents() {
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        mService = retrofit.create(OpenWeatherMap.class);
        searchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayout.VERTICAL, false));
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        searchActivityAdapter = new SearchActivityAdapter(getApplicationContext(), mWeatherResult);
        db = DBProvider.getInstance(getApplicationContext());
        appBarLayout = (AppBarLayout) findViewById(R.id.appBar);
        btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_search = (Button) findViewById(R.id.btn_search);

        ItemTouchHelper.SimpleCallback itemTouchHelper
                = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recyclerView);

    }


    private void addListener() {
        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                getWeatherInformationByName(text.toString());
                searchBar.setVisibility(View.GONE);
                appBarLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onButtonClicked(int buttonCode) {
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appBarLayout.setVisibility(View.GONE);
                searchBar.setVisibility(View.VISIBLE);
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void fillWeatherList(WeatherResult weatherResult) {
        mWeatherResult.add(weatherResult);
        recyclerView.setAdapter(searchActivityAdapter);
        searchActivityAdapter.notifyDataSetChanged();
    }

    public ArrayList<WeatherResult> getmWeatherResult() {

        if (mWeatherResult == null) {
            mWeatherResult = new ArrayList<>();
        }
        return mWeatherResult;
    }

    private void getWeatherInfromationFromDb() {
        ArrayList<WeatherResult> weather_results = new ArrayList<>();
        WeatherResult weatherResult;
        weather_results.addAll(db.getmDb().weatherDao().getAll());
        for (int i = 0; i < weather_results.size(); i++) {
            weatherResult = weather_results.get(i);
            compositeDisposable.add(mService.getWeatherByCityName(weatherResult.getName(),
                    Common.API_KEY,
                    Common.temp_unit)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<WeatherResult>() {
                        @Override
                        public void accept(WeatherResult weatherResult) throws Exception {
                            fillWeatherList(weatherResult);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Toast.makeText(getApplicationContext(), "" + "You entered wrong name", Toast.LENGTH_LONG).show();
                        }
                    })
            );
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof SearchActivityAdapter.MyViewHolder) {
            String name = mWeatherResult.get(viewHolder.getAdapterPosition()).getName();
            final WeatherResult weatherResult = mWeatherResult.get(viewHolder.getAdapterPosition());
            final int deleteIndex = viewHolder.getAdapterPosition();
            searchActivityAdapter.removeItem(deleteIndex);
            db.getmDb().weatherDao().delete(weatherResult);

            Snackbar snackbar = Snackbar.make(nestedScrollView, "Removed", Snackbar.LENGTH_SHORT);
            snackbar.setAction("Undo", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchActivityAdapter.restoreItem(weatherResult, deleteIndex);
                    db.getmDb().weatherDao().insertAll(weatherResult);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}
