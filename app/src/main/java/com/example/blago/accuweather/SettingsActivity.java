package com.example.blago.accuweather;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blago.accuweather.Common.Common;
import com.example.blago.accuweather.db.DBProvider;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    private ImageButton btn_back;
    private RelativeLayout tmp_unit;
    private TextView txt_unit;
    private DBProvider db;
    private ArrayList<Common> common;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);

        initComponents();
        addListener();
    }


    private void initComponents() {
        btn_back = (ImageButton) findViewById(R.id.btn_back);
        tmp_unit = (RelativeLayout) findViewById(R.id.layout_unit);
        txt_unit = (TextView) findViewById(R.id.txt_unit);
        db = DBProvider.getInstance(getApplicationContext());
        common = new ArrayList<>();
        common.addAll(db.getmDb().weatherDao().getCommons());
        if (common.get(0).getTemp_unit().equalsIgnoreCase("imperial")) {
            txt_unit.setText("°F");
        } else {
            txt_unit.setText("°C");
        }
    }

    private void addListener() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTempUnit();
                Intent back = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(back);
                finish();
            }
        });

        tmp_unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (common.get(0).getTemp_unit().equalsIgnoreCase("metric")) {
                    String imperial = "imperial";
                    common.get(0).setTemp_unit(imperial);
                    txt_unit.setText("°F");
                } else {
                    common.get(0).setTemp_unit("metric");
                    txt_unit.setText("°C");
                }
            }
        });
    }

    private void setTempUnit() {
        if (txt_unit.getText().toString().equalsIgnoreCase("°F")) {
            db.getmDb().weatherDao().deleteCommon(common.get(0));
            Common commons = new Common();
            commons.setTemp_unit("imperial");
            db.getmDb().weatherDao().insertCommon(commons);
        } else {
            db.getmDb().weatherDao().deleteCommon(common.get(0));
            Common commons = new Common();
            commons.setTemp_unit("metric");
            db.getmDb().weatherDao().insertCommon(commons);
        }
    }
}
