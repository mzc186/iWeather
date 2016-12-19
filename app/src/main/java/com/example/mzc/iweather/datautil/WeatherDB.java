package com.example.mzc.iweather.datautil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mzc.iweather.model.City;
import com.example.mzc.iweather.model.County;
import com.example.mzc.iweather.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MZC on 9/3/2016.
 */
public class WeatherDB {
    public static final String DB_NAME="iWeather";
    public static final int VERSION=1;
    private static WeatherDB iWeatherDB;
    private SQLiteDatabase db;

    /**
     * make the construction method private
     */

    private WeatherDB(Context context){
        WeatherDBOpenHelper dbHelper=new WeatherDBOpenHelper(context,DB_NAME,null,VERSION);
        db=dbHelper.getWritableDatabase();
    }

    /**
     * get the instance of the WeatherDB
     */
    public synchronized static WeatherDB getInstance(Context context){
        if(iWeatherDB==null){
            iWeatherDB=new WeatherDB(context);
        }
        return iWeatherDB;
    }

    /**
     * store the instance of Province in the db
     */
    public void saveProvince(Province province){
        if(province!=null){
            ContentValues values=new ContentValues();
            values.put("province_name",province.getProvinceName());
            values.put("province_code",province.getProvinceCode());
            db.insert("Province",null,values);
        }
    }

    /**
     * read the info of the all Provinces of the Country from the db
     */
    public List<Province> loadProvinces(){
        List<Province> list=new ArrayList<Province>();
        Cursor cursor=db.query("Province",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                Province province=new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                list.add(province);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    /**
     * store the instances of City in the db
     */
    public void saveCity(City city){
        if(city!=null){
            ContentValues values=new ContentValues();
            values.put("city_name",city.getCityName());
            values.put("city_code",city.getCityCode());
            values.put("province_id",city.getProvinceId());
            db.insert("City",null,values);
        }
    }

    /**
     * read the info of all cities under a certain province
     */
    public List<City> loadCities(int provinceId){
        List<City> list=new ArrayList<>();
        Cursor cursor=db.query("City",null,"province_id=?",new String[]{String.valueOf(provinceId)},null,null,null);
        if(cursor.moveToFirst()){
            do{
                City city=new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvinceId(provinceId);
                list.add(city);
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        return  list;
    }

    /**
     * save the instance of County to the db
     */
    public void saveCounty(County county){
        if(county!=null){
            ContentValues values=new ContentValues();
            values.put("county_name",county.getCountyName());
            values.put("county_code",county.getCountyCode());
            values.put("city_id",county.getCityId());
            db.insert("County",null,values);
        }
    }

    /**
     * read the info of all counties of a certain city
     */
    public List<County> loadCounties(int cityId){
        List<County> list=new ArrayList<>();
        Cursor cursor=db.query("County",null,"city_id=?",new String[]{String.valueOf(cityId)},null,null,null);
        if(cursor.moveToFirst()){
            do{
                County county=new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
                county.setCityId(cityId);
                list.add(county);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}
