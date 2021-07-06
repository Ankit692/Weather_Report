package com.example.weatherreport;

import org.json.JSONException;
import org.json.JSONObject;
// below for fetching the data from the server
public class weatherData {
 private  String mTemperature,mIcon,mCity,mWeatherType;
 private int mCondition;
 //paring json data from the server
 public static weatherData fromJson(JSONObject jsonObject){
     weatherData weatherD=new weatherData();
     try {
         weatherD.mCity=jsonObject.getString("name");
         weatherD.mCondition=jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
         weatherD.mWeatherType=jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
         weatherD.mIcon=updateWeatherIcon(weatherD.mCondition);
         double tempResult=jsonObject.getJSONObject("main").getDouble("temp")-273.15;
         int roundVal=(int)Math.rint(tempResult);
         weatherD.mTemperature=Integer.toString(roundVal);
         return weatherD;
     } catch (JSONException e) {
         e.printStackTrace();
         return null;
     }

 }

//to update the weather icon
 private static String updateWeatherIcon(int condition){
     if(condition>=0 && condition<300){
         return "tstorm1";
     }
     else if(condition>=300 && condition<500){
         return "light_rain";
     }
    else if(condition>=500 && condition<600){
         return "shower3";
     }
     else if(condition>=600 && condition<=700){
         return "snow4";
     }
    else if(condition>=701 && condition<=771){
         return "fog";
     }
     else if(condition>=772 && condition<800){
         return "overcast";
     }
     else if(condition==800){
         return "sunny";
     }
     else if(condition>=801 &&condition<=804){
         return "cloudy2";
     }
     else if(condition>=900 && condition<=902){
         return "tstorm3";
     }
     else if(condition==903){
         return "snow5";
     }
     else if(condition==904){
         return "sunny";
     }
     else if(condition>=905 &&condition<=1000){
         return "tstorm3";
     }
     return "dunno";
 }

    public String getTemperature() {
        return mTemperature+"°C";
    }

    public String getIcon() {
        return mIcon;
    }

    public String getCity() {
        return mCity;
    }

    public String getWeatherType() {
        return mWeatherType;
    }
}
