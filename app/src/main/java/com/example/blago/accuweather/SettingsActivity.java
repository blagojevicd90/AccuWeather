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

public class SettingsActivity extends AppCompatActivity {

    private ImageButton btn_back;
    private RelativeLayout tmp_unit;
    private TextView txt_unit;

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
        if (Common.temp_unit.equalsIgnoreCase("imperial")) {
            txt_unit.setText("°F");
        } else {
            txt_unit.setText("°C");
        }
    }

    private void addListener() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(back);
                finish();
            }
        });

        tmp_unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.temp_unit.equalsIgnoreCase("metric")) {
                    String imperial = "imperial";
                    Common.temp_unit = imperial;
                    txt_unit.setText("°F");
                } else {
                    Common.temp_unit = "metric";
                    txt_unit.setText("°C");
                }
            }
        });
    }
}
