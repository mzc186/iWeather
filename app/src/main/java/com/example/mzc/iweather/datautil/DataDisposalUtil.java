package com.example.mzc.iweather.datautil;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.mzc.iweather.datautil.WeatherDB;
import com.example.mzc.iweather.model.City;
import com.example.mzc.iweather.model.County;
import com.example.mzc.iweather.model.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by MZC on 9/4/2016.
 */
public class DataDisposalUtil {
    /**
     * analyze and dispose the data of Provinces returned from sever
     */
    public  static boolean handleProvincesResponse(WeatherDB weatherDB,String response){
        if(!TextUtils.isEmpty(response)){
            String[] allProvinces=response.split(",");
            if(allProvinces!=null&&allProvinces.length>0){
                for(String p:allProvinces){
                    String[] array=p.split("\\|");
                    Province province=new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1].trim());
                    //save the data analyzed to the table of Province
                    weatherDB.saveProvince(province);
                }
                return true;
            }

        }
        return false;
    }

    /**
     * analyze and dispose the data of Cities returned from server
     */
    public static boolean handleCitiesResponse(WeatherDB weatherDB,String response,int provinceId){
        if(!TextUtils.isEmpty(response)){
            String[] allCities=response.split(",");
            if(allCities!=null&&allCities.length>0){
                for(String c:allCities){
                    String[] array=c.split("\\|");
                    City city=new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1].trim());
                    city.setProvinceId(provinceId);
                    //save the data analyzed to the table of City
                    weatherDB.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * analyze and dispose the data of Counties returned from server
     */
    public static boolean handleCountiesResponse(WeatherDB weatherDB,String response,int cityId){
        if(!TextUtils.isEmpty(response)){
            String[] allCounties=response.split(",");
            if(allCounties!=null&&allCounties.length>0){
                for(String c:allCounties){
                    String[] array=c.split("\\|");
                    County county=new County();
                    county.setCountyCode(array[0]);
                    county.setCountyName(array[1].trim());
                    county.setCityId(cityId);
                    //save the data analyzed to the table of County
                    weatherDB.saveCounty(county);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * decode the Json data returned from the server,save the data decoded to local storage.
     */
    public static void handleWeatherDataResponse(Context context, String response,String countyCode){
        SharedPreferences sharedPreferences=context.getSharedPreferences(countyCode,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        try{
            JSONObject jO=new JSONObject(response);
            JSONArray jA=jO.getJSONArray("HeWeather data service 3.0");
            JSONObject jsonObject=jA.getJSONObject(0);

            //read aqi data
            try {
                JSONObject aqi = jsonObject.getJSONObject("aqi").getJSONObject("city");
                String aqi_aqi = aqi.getString("aqi");
                String aqi_co = aqi.getString("co");
                String aqi_no2 = aqi.getString("no2");
                String aqi_o3 = aqi.getString("o3");
                String aqi_pm10 = aqi.getString("pm10");
                String aqi_pm25 = aqi.getString("pm25");
                String aqi_qlty = aqi.getString("qlty");
                String aqi_so2 = aqi.getString("so2");

                //save aqi data
                editor.putString("aqi_aqi", aqi_aqi);
                editor.putString("aqi_co", aqi_co);
                editor.putString("aqi_no2", aqi_no2);
                editor.putString("aqi_o3", aqi_o3);
                editor.putString("aqi_pm10", aqi_pm10);
                editor.putString("aqi_pm25", aqi_pm25);
                editor.putString("aqi_qlty", aqi_qlty);
                editor.putString("aqi_so2", aqi_so2);
                editor.apply();
            }catch (JSONException e){
                e.printStackTrace();
                Log.d("DataDisposalUtil","解析aqi数据失败");
            }

            //read the basicInfo of the County
            try{
                JSONObject basic=jsonObject.getJSONObject("basic");
                String city=basic.getString("city");
                String country=basic.getString("cnty");
                String cityId=basic.getString("id");
                String latitude=basic.getString("lat");
                String longitude=basic.getString("lon");
                JSONObject update=basic.getJSONObject("update");
                String updatedTime=update.getString("loc");

            //save the data of basicInfo
                editor.putString("city",city);
                editor.putString("country",country);
                editor.putString("cityId",cityId);
                editor.putString("latitude",latitude);
                editor.putString("longitude",longitude);
                editor.putString("updatedTime",updatedTime);
                editor.apply();
            }catch (JSONException e){
                e.printStackTrace();
                Log.d("DataDisposalUtil","解析基础数据失败");
            }


            //deal with the data of daily forecast
            try{
            JSONArray daily_forecast_array=jsonObject.getJSONArray("daily_forecast");
            for(int i=0;i<7;i++) {
                JSONObject daily_forecast_jo = daily_forecast_array.getJSONObject(i);
                String df_sr = daily_forecast_jo.getJSONObject("astro").getString("sr");
                String df_ss = daily_forecast_jo.getJSONObject("astro").getString("ss");
                String df_cond_code_d = daily_forecast_jo.getJSONObject("cond").getString("code_d");
                String df_cond_code_n = daily_forecast_jo.getJSONObject("cond").getString("code_n");
                String df_cond_txt_d = daily_forecast_jo.getJSONObject("cond").getString("txt_d");
                String df_cond_txt_n = daily_forecast_jo.getJSONObject("cond").getString("txt_n");
                String df_date = daily_forecast_jo.getString("date");
                String df_hum = daily_forecast_jo.getString("hum");
                String df_pcpn = daily_forecast_jo.getString("pcpn");
                String df_pop = daily_forecast_jo.getString("pop");
                String df_pres = daily_forecast_jo.getString("pres");
                String df_tmp_max = daily_forecast_jo.getJSONObject("tmp").getString("max");
                String df_tmp_min = daily_forecast_jo.getJSONObject("tmp").getString("min");
                String df_vis = daily_forecast_jo.getString("vis");
                String df_wind_deg = daily_forecast_jo.getJSONObject("wind").getString("deg");
                String df_wind_dir = daily_forecast_jo.getJSONObject("wind").getString("dir");
                String df_wind_sc = daily_forecast_jo.getJSONObject("wind").getString("sc");
                String df_wind_spd = daily_forecast_jo.getJSONObject("wind").getString("spd");

                int j=i+1;
                editor.putString("df_sr_day"+j,df_sr);
                editor.putString("df_ss_day"+j,df_ss);
                editor.putString("df_conditionCode_d_day"+j,df_cond_code_d);
                editor.putString("df_conditionCode_n_day"+j,df_cond_code_n);
                editor.putString("df_conditionText_d_day"+j,df_cond_txt_d);
                editor.putString("df_conditionText_n_day"+j,df_cond_txt_n);
                editor.putString("df_date_day"+j,df_date);
                editor.putString("df_hum_day"+j,df_hum);
                editor.putString("df_pcpn_day"+j,df_pcpn);
                editor.putString("df_pop_day"+j,df_pop);
                editor.putString("df_pres_day"+j,df_pres);
                editor.putString("df_tmp_max_day"+j,df_tmp_max);
                editor.putString("df_tmp_min_day"+j,df_tmp_min);
                editor.putString("df_vis_day"+j,df_vis);
                editor.putString("df_wind_deg_day"+j,df_wind_deg);
                editor.putString("df_wind_dir_day"+j,df_wind_dir);
                editor.putString("df_wind_sc_day"+j,df_wind_sc);
                editor.putString("df_wind_spd_day"+j,df_wind_spd);
                editor.apply();
            }}catch (JSONException e){
                e.printStackTrace();
                Log.d("DataDisposalUtil","解析7天预报数据失败");
            }


            //deal with the weather data for now
            try{
                JSONObject now_jo=jsonObject.getJSONObject("now");
                String now_cond_code=now_jo.getJSONObject("cond").getString("code");
                String now_cond_txt=now_jo.getJSONObject("cond").getString("txt");
                String now_fl=now_jo.getString("fl");
                String now_hum=now_jo.getString("hum");
                String now_pcpn=now_jo.getString("pcpn");
                String now_pres=now_jo.getString("pres");
                String now_tmp=now_jo.getString("tmp");
                String now_vis=now_jo.getString("vis");
                String now_wind_deg=now_jo.getJSONObject("wind").getString("deg");
                String now_wind_dir=now_jo.getJSONObject("wind").getString("dir");
                String now_wind_sc=now_jo.getJSONObject("wind").getString("sc");
                String now_wind_spd=now_jo.getJSONObject("wind").getString("spd");

                editor.putString("now_cond_code",now_cond_code);
                editor.putString("now_cond_txt",now_cond_txt);
                editor.putString("now_fl",now_fl);
                editor.putString("now_hum",now_hum);
                editor.putString("now_pcpn",now_pcpn);
                editor.putString("now_pres",now_pres);
                editor.putString("now_tmp",now_tmp);
                editor.putString("now_vis",now_vis);
                editor.putString("now_wind_deg",now_wind_deg);
                editor.putString("now_wind_dir",now_wind_dir);
                editor.putString("now_wind_sc",now_wind_sc);
                editor.putString("now_wind_spd",now_wind_spd);
                editor.apply();
            }
            catch(JSONException e){
                e.printStackTrace();
                Log.d("DataDisposalUtil","解析当前天气数据失败");
            }

            //deal with the suggestion data for today
            try{
            JSONObject suggestion_jo=jsonObject.getJSONObject("suggestion");
            String suggestion_comf_brf=suggestion_jo.getJSONObject("comf").getString("brf");
            String suggestion_comf_txt=suggestion_jo.getJSONObject("comf").getString("txt");
            String suggestion_cw_brf=suggestion_jo.getJSONObject("cw").getString("brf");
            String suggestion_cw_txt=suggestion_jo.getJSONObject("cw").getString("txt");
            String suggestion_drsg_brf=suggestion_jo.getJSONObject("drsg").getString("brf");
            String suggestion_drsg_txt=suggestion_jo.getJSONObject("drsg").getString("txt");
            String suggestion_flu_brf=suggestion_jo.getJSONObject("flu").getString("brf");
            String suggestion_flu_txt=suggestion_jo.getJSONObject("flu").getString("txt");
            String suggestion_sport_brf=suggestion_jo.getJSONObject("sport").getString("brf");
            String suggestion_sport_txt=suggestion_jo.getJSONObject("sport").getString("txt");
            String suggestion_trav_brf=suggestion_jo.getJSONObject("trav").getString("brf");
            String suggestion_trav_txt=suggestion_jo.getJSONObject("trav").getString("txt");
            String suggestion_uv_brf=suggestion_jo.getJSONObject("uv").getString("brf");
            String suggestion_uv_txt=suggestion_jo.getJSONObject("uv").getString("txt");

            editor.putString("suggestion_comf_brf",suggestion_comf_brf);
            editor.putString("suggestion_comf_txt",suggestion_comf_txt);
            editor.putString("suggestion_cw_brf",suggestion_cw_brf);
            editor.putString("suggestion_cw_txt",suggestion_cw_txt);
            editor.putString("suggestion_drsg_brf",suggestion_drsg_brf);
            editor.putString("suggestion_drsg_txt",suggestion_drsg_txt);
            editor.putString("suggestion_flu_brf",suggestion_flu_brf);
            editor.putString("suggestion_flu_txt",suggestion_flu_txt);
            editor.putString("suggestion_sport_brf",suggestion_sport_brf);
            editor.putString("suggestion_sport_txt",suggestion_sport_txt);
            editor.putString("suggestion_trav_brf",suggestion_trav_brf);
            editor.putString("suggestion_trav_txt",suggestion_trav_txt);
            editor.putString("suggestion_uv_brf",suggestion_uv_brf);
            editor.putString("suggestion_uv_txt",suggestion_uv_txt);
            editor.apply();}
            catch (JSONException e){
                e.printStackTrace();
                Log.d("DataDisposalUtil","解析建议指数数据失败");
            }


            //deal with the data of hourly forecast
            JSONArray hf_array;
            JSONObject hf_jo;
            try {
                hf_array = jsonObject.getJSONArray("hourly_forecast");
                for (int i = 0; i < 3; i++) {
                    hf_jo = hf_array.getJSONObject(i);
                    String hf_date = hf_jo.getString("date");
                    String hf_hum = hf_jo.getString("hum");
                    String hf_pop = hf_jo.getString("pop");
                    String hf_pres = hf_jo.getString("pres");
                    String hf_tmp = hf_jo.getString("tmp");
                    String hf_wind_deg = hf_jo.getJSONObject("wind").getString("deg");
                    String hf_wind_dir = hf_jo.getJSONObject("wind").getString("dir");
                    String hf_wind_sc = hf_jo.getJSONObject("wind").getString("sc");
                    String hf_wind_spd = hf_jo.getJSONObject("wind").getString("spd");

                    editor.putString("hf_date_" + i, hf_date);
                    editor.putString("hf_hum_" + i, hf_hum);
                    editor.putString("hf_pop_" + i, hf_pop);
                    editor.putString("hf_pres_" + i, hf_pres);
                    editor.putString("hf_tmp_" + i, hf_tmp);
                    editor.putString("hf_wind_deg_" + i, hf_wind_deg);
                    editor.putString("hf_wind_dir_" + i, hf_wind_dir);
                    editor.putString("hf_wind_sc_" + i, hf_wind_sc);
                    editor.putString("hf_wind_spd_" + i, hf_wind_spd);
                    editor.apply();
                }
            }catch(JSONException e){
                 e.printStackTrace();
                }

            //read and deal with the status data
            try{
            String status=jsonObject.getString("status");
            editor.putString("status",status);
            editor.apply();}
            catch (JSONException e){
                e.printStackTrace();
                Log.d("DataDisposalUtil","解析状态数据失败");
            }

        } catch (JSONException e){
            e.printStackTrace();
            Log.d("DataDisposalUtil","全部天气数据解析失败");
        }
    }
}
