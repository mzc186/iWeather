package com.example.mzc.iweather.model;

/**
 * Created by MZC on 10/19/2016.
 */
public class RecyclerAdapterDataModle {
    private String cityName;
    private String cityCode;
    private int weatherCode;
    private String weatherText;
    private String tmpRange;

    public void setCityName(String cityName){
        this.cityName=cityName;
    }
    public String getCityName(){
        return this.cityName;
    }

    public void setCityCode(String cityCode){
        this.cityCode=cityCode;
    }
    public String getCityCode(){
        return this.cityCode;
    }

    public void setWeatherCode(int weatherCode){
        this.weatherCode=weatherCode;
    }
    public int getWeatherCode(){
        return this.weatherCode;
    }

    public void setWeatherText(String weatherText){
        this.weatherText=weatherText;
    }
    public String getWeatherText(){
        return this.weatherText;
    }

    public void setTmpRange(String tmpRange){
        this.tmpRange=tmpRange;
    }
    public String getTmpRange(){
        return this.tmpRange;
    }
}
