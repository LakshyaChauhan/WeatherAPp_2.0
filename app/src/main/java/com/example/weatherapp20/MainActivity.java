package com.example.weatherapp20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout homerl;
    private ProgressBar pub;
    private ImageView backIV, serachIV,iconIV;
    private TextView cityName, temp,condition;
    private TextInputEditText editText;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homerl = findViewById(R.id.deltaRelative);
        pub = findViewById(R.id.ProgressBar);
        backIV = findViewById(R.id.Image);
        serachIV = findViewById(R.id.Search);
        iconIV = findViewById(R.id.IVicon);
        cityName = findViewById(R.id.TextCityname);
        temp = findViewById(R.id.TVTemp);
        condition = findViewById(R.id.TVCondition);
        editText = findViewById(R.id.TIEdtCityNAme);
        recyclerView = findViewById(R.id.RVWeather);

    }
}