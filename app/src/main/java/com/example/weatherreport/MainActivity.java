package com.example.weatherreport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    final String APP_ID = "7ffe5a23cbd3c066c4f313559e71f8ef";
    final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";

    final long MIN_TIME = 5000;
    final float MIN_DISTANCE = 1000;
    final int REQUEST_CODE = 101;

    String Location_Provider = LocationManager.GPS_PROVIDER;

    TextView nameOfCity, weatherState, temperature;
    ImageView weatherIcon;
    Button cityFinder;

    LocationManager mLocationManager;
    LocationListener mLocationListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nameOfCity=(TextView) findViewById(R.id.cityName);
        weatherState=(TextView)findViewById(R.id.weatherCondition);
        temperature=(TextView)findViewById(R.id.temperature);
        weatherIcon=(ImageView) findViewById(R.id.weatherIcon);
        cityFinder=(Button) findViewById(R.id.searchCityFinder);
    }
    //onResume() is created to check if the app is just started then it will show the weather for current location otherwise
    // it will show the weather according to the user entered city in cityFinder activity
    @Override
    protected void onResume() {
        super.onResume();
        Intent intent=getIntent();
        String city=intent.getStringExtra("City");
        if(city!=null)
        {
            getWeatherForNewCity(city);
        }
        else {
            getWeatherForCurrentLocation();
        }
    }
    //to get the weather of the city entered in the edit text in cityFinder Activity
    private void getWeatherForNewCity(String city)
    {
        RequestParams params=new RequestParams();
        params.put("q",city);
        params.put("appID",APP_ID);
        letsDoSomeNetworking(params);
    }
    //to get the weather for current location
    private void getWeatherForCurrentLocation() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                String latitude = String.valueOf(location.getLatitude());
                String longitude = String.valueOf(location.getLongitude());
                RequestParams params=new RequestParams();
                params.put("lat",latitude);
                params.put("lon",longitude);
                params.put("appID",APP_ID);
                letsDoSomeNetworking(params);
            }

        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;

        }
        mLocationManager.requestLocationUpdates(Location_Provider, MIN_TIME, MIN_DISTANCE, mLocationListener);

    }
    //If the app is run for first time then on opening the app it will ask for location access permission for the app
    //so that app will show the weather for current location
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getWeatherForCurrentLocation();
            } else {
                Log.d("Climate","Permission denied");
            }
        }

    }
    //to get the json format data  from the server to the app after parsing the data

    private void letsDoSomeNetworking(RequestParams params){
        AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
        asyncHttpClient.get(WEATHER_URL,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);
                weatherData weatherD=weatherData.fromJson(response);
                updateUI(weatherD);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
//it will set all the details that we want to show on the screen
    private void updateUI(weatherData weather){
        temperature.setText(weather.getTemperature());
        nameOfCity.setText(weather.getCity());
        weatherState.setText(weather.getWeatherType());
        int resourceID=getResources().getIdentifier(weather.getIcon(),"drawable",getPackageName());
        weatherIcon.setImageResource(resourceID);
    }
// below code:if we will close the app and we will reopen the app then it will show the location for current location
    @Override
    protected void onPause() {
        super.onPause();
        if(mLocationManager!=null){
            mLocationManager.removeUpdates(mLocationListener);
        }
    }
   //it is a button which is used to go to the cityFinder activity from the MainActivity
    public void city_Find(View view) {
        Intent intent=new Intent(MainActivity.this,cityFinder.class);
        startActivity(intent);
    }
}