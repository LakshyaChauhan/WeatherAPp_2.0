package com.example.weatherapp20;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout homerl;
    private ProgressBar pub;
   ImageView backIV, serachIV,iconIV;
    private TextView cityName, temp,condition;
    private TextInputEditText editText;
    RecyclerView recyclerView;

    private ArrayList<WeatherRvModel> rvModelArrayList;
    private WeatherRvAdapter weatherRvAdapter;

   LocationManager locationManager;

    String ctName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

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

        rvModelArrayList = new ArrayList<>();
        weatherRvAdapter = new WeatherRvAdapter(this,rvModelArrayList);

        recyclerView.setAdapter(weatherRvAdapter);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        checkPermission();

        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

//        ctName = getCityName(location.getLongitude(),location.getLatitude());

        getWeatherInfo("chandigarh");

        serachIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = editText.toString();
                if(city.isEmpty()){
                    Toast.makeText(MainActivity.this,"Please enter city Name", Toast.LENGTH_SHORT).show();
                }else{
                    cityName.setText(city);
                    getWeatherInfo(city);
                }
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1 ){
            if(grantResults.length>0 & grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permission Granted", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"Permission not Granted", Toast.LENGTH_SHORT).show();
                finish();
            }

        }

    }

    private String getCityName(double longitude, double latitude){

        String cityName = "Not Found";
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(longitude,latitude,10);

            for(Address adr : addresses){
                if(adr != null){
                    String city = adr.getLocality();
                    if(city!= null && !city.equals("")){
                        cityName = city;
                    }else {
                        Log.d("Tag","City Not Found..");
                        Toast.makeText(this,"User city Not Found... ",Toast.LENGTH_SHORT).show();
                    }
                }
            }

        }catch (IOException e) {
            e.printStackTrace();
        }
    return  cityName;
    }

    private void getWeatherInfo(String ctName){
        String url = "http://api.weatherapi.com/v1/forecast.json?key=1b4525c5a438400b8c4141149233005&q="+cityName+"&days=1&aqi=yes&alerts=yes";

        cityName.setText(ctName);

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                pub.setVisibility(View.GONE);
                homerl.setVisibility(View.VISIBLE);

                rvModelArrayList.clear();

                try {
                    String temperature = response.getJSONObject("current").getString("temp_c");
                    temp.setText(temperature.concat(" °C"));
                    String conditionIcon = response.getJSONObject("current").getJSONObject("condition").getString("icon");
                    Picasso.get().load("http:".concat(conditionIcon)).into(iconIV);
                    String cond = response.getJSONObject("current").getJSONObject("condition").getString("text");
                    condition.setText(cond);

                    int isDay = response.getJSONObject("current").getInt("is_day");

                    if(isDay == 1){
                        //morning
                        Picasso.get().load("https://th.bing.com/th/id/OIP.HTyLHugVHnKgzLIjG4mwEAHaF7?pid=ImgDet&rs=1").into(backIV);
                    }else {
                        //night
                        Picasso.get().load("https://jooinn.com/images/night-sky-17.jpg").into(backIV);
                    }

                    JSONObject forecasstobj = response.getJSONObject("forecast");
                    JSONObject forecast = forecasstobj.getJSONArray("forecastday").getJSONObject(0);
                    JSONArray hour = forecast.getJSONArray("hour");

                    for(int i=0;i< hour.length();i++){
                        JSONObject hourObj = hour.getJSONObject(i);
                        String time = hourObj.getString("time");
                        String temper = hourObj.getString("temp_c");
                        String img = hourObj.getJSONObject("condition").getString("icon");
                        String wind =  hourObj.getString("wind_kph");
                        rvModelArrayList.add(new WeatherRvModel(temper,time,img,wind));
                    }
                    weatherRvAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"Please enter valid city name.",Toast.LENGTH_SHORT).show();

            }
        });

        requestQueue.add(jsonObjectRequest);

    }

    private void checkPermission(){
        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&  ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)  {
            ActivityCompat.requestPermissions(MainActivity.this,new String[] {Manifest.permission.ACCESS_COARSE_LOCATION ,Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
    }
}