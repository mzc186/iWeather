package com.example.mzc.iweather.uicomponents;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mzc.iweather.R;
import com.example.mzc.iweather.activity.MainActivity;
import com.example.mzc.iweather.displayutil.DisplayUtil;
import com.example.mzc.iweather.datautil.DataDisposalUtil;
import com.example.mzc.iweather.netutil.HttpCallbackListener;
import com.example.mzc.iweather.netutil.HttpUtil;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by MZC on 10/9/2016.
 */
public class MyObjectFragment extends Fragment {
    private SharedPreferences mspf;
    private SharedPreferences wnl_spf;

    private TextView countyName;
    private TextView weekDay;
    private TextView sunDay;
    private TextView moonDay;
    private TextView now_tmp;
    private TextView now_tmp_range;
    private TextView now_air_quality;
    private CardView air_quality_cardView;
    private ImageView now_weather_image;
    private TextView now_weather_txt;
    private TextView now_wind_sc;

    private TextView aqi;
    private TextView pm25;
    private TextView pm10;
    private TextView co;
    private TextView so2;
    private TextView no2;
    private TextView o3;

    private TextView feeling_tmp;
    private TextView relative_humidity;
    private TextView precipitation;
    private TextView air_pressure;
    private TextView visibility;
    private TextView wind_scale;
    private TextView wind_speed;
    private TextView wind_degree;
    private TextView wind_direction;

    private TextView sport_index;
    private TextView flu_index;
    private TextView travel_index;
    private TextView ultraviolet_index;
    private TextView wear_index;

    private TextView df_weekday_0;
    private TextView df_weekday_1;
    private TextView df_weekday_2;
    private TextView df_weekday_3;
    private TextView df_weekday_4;
    private TextView df_weekday_5;
    private TextView df_weekday_6;
    private TextView df_weekday_7;

    private TextView df_date_0;
    private TextView df_date_1;
    private TextView df_date_2;
    private TextView df_date_3;
    private TextView df_date_4;
    private TextView df_date_5;
    private TextView df_date_6;
    private TextView df_date_7;

    private TextView df_cond_day_txt_0;
    private TextView df_cond_day_txt_1;
    private TextView df_cond_day_txt_2;
    private TextView df_cond_day_txt_3;
    private TextView df_cond_day_txt_4;
    private TextView df_cond_day_txt_5;
    private TextView df_cond_day_txt_6;
    private TextView df_cond_day_txt_7;

    private ImageView df_cond_day_image_0;
    private ImageView df_cond_day_image_1;
    private ImageView df_cond_day_image_2;
    private ImageView df_cond_day_image_3;
    private ImageView df_cond_day_image_4;
    private ImageView df_cond_day_image_5;
    private ImageView df_cond_day_image_6;
    private ImageView df_cond_day_image_7;

    private ImageView df_cond_night_image_0;
    private ImageView df_cond_night_image_1;
    private ImageView df_cond_night_image_2;
    private ImageView df_cond_night_image_3;
    private ImageView df_cond_night_image_4;
    private ImageView df_cond_night_image_5;
    private ImageView df_cond_night_image_6;
    private ImageView df_cond_night_image_7;

    private TextView df_cond_night_txt_0;
    private TextView df_cond_night_txt_1;
    private TextView df_cond_night_txt_2;
    private TextView df_cond_night_txt_3;
    private TextView df_cond_night_txt_4;
    private TextView df_cond_night_txt_5;
    private TextView df_cond_night_txt_6;
    private TextView df_cond_night_txt_7;

    private TextView df_wind_direction_0;
    private TextView df_wind_direction_1;
    private TextView df_wind_direction_2;
    private TextView df_wind_direction_3;
    private TextView df_wind_direction_4;
    private TextView df_wind_direction_5;
    private TextView df_wind_direction_6;
    private TextView df_wind_direction_7;

    private TextView df_wind_scale_0;
    private TextView df_wind_scale_1;
    private TextView df_wind_scale_2;
    private TextView df_wind_scale_3;
    private TextView df_wind_scale_4;
    private TextView df_wind_scale_5;
    private TextView df_wind_scale_6;
    private TextView df_wind_scale_7;

    private LinearLayout yestDay_day_layout;
    private LinearLayout yestDay_night_layout;
    private TemperatureTrendView mTempTrendView;

    private MainActivity mActivity;
    private String mCountyCode;
    private String mCountyName;
    private int mPosition;

    private View mFragmentView;
    private RefreshableView mRefreshableView;

    /************************************split line***************************************/
    public MyObjectFragment(){
    }

    public void initFragment(MainActivity activity,int position){
        mPosition=position;
        mActivity=activity;
        wnl_spf=mActivity.getSharedPreferences("WanNianLi",Context.MODE_PRIVATE);
        mCountyName=wnl_spf.getString("selectedCountyName"+position,"");
        mCountyCode=wnl_spf.getString("selectedCountyCode"+position,"");
        mspf=mActivity.getSharedPreferences(mCountyCode, Context.MODE_PRIVATE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(mFragmentView==null){
            mFragmentView=inflater.inflate(R.layout.fragment_layout_1,container,false);
            initWidgets(mFragmentView);
            showWeather();
            refreshData();
        }else{
            if(mActivity.ffc_flag[mPosition].equals("true")) {
                //因为连接网速络刷新数据需要少量的时间，所以会造成一定的滞后。
                //程序每次重新开启的时候如果不先调用一下showWeather()方法，
                // 则程序会先将xml文件中定义的静态数据显示出来，而那些静态数据都是不真实的，
                //这会给用户造成不好的体验。因此宜将上一次更新完的数据先显示出来，等数据更新完再刷新显示一遍。
                showWeather();
                refreshData();
            }else{
                showWeather();
            }
        }
        return mFragmentView;
    }


    //initialize the widget and basic data
    private void initWidgets(View view){
        weekDay=(TextView)view.findViewById(R.id.week_day);
        sunDay=(TextView)view.findViewById(R.id.sun_day);
        moonDay=(TextView)view.findViewById(R.id.moon_day);
        now_tmp=(TextView)view.findViewById(R.id.now_tmp);
        now_tmp_range=(TextView)view.findViewById(R.id.now_tmp_range);
        now_air_quality=(TextView)view.findViewById(R.id.air_quality);
        air_quality_cardView=(CardView)view.findViewById(R.id.air_quality_cardView);
        now_weather_image=(ImageView)view.findViewById(R.id.now_weather_image);
        now_weather_txt=(TextView)view.findViewById(R.id.now_weather_txt);
        now_wind_sc=(TextView)view.findViewById(R.id.now_wind_sc);

        aqi=(TextView)view.findViewById(R.id.aqi);
        pm25=(TextView)view.findViewById(R.id.pm25);
        pm10=(TextView)view.findViewById(R.id.pm10);
        co=(TextView)view.findViewById(R.id.co);
        so2=(TextView)view.findViewById(R.id.so2);
        no2=(TextView)view.findViewById(R.id.no2);
        o3=(TextView)view.findViewById(R.id.o3);

        feeling_tmp=(TextView)view.findViewById(R.id.feeling_tmp);
        relative_humidity=(TextView)view.findViewById(R.id.relative_humidity);
        precipitation=(TextView)view.findViewById(R.id.precipitation);
        air_pressure=(TextView)view.findViewById(R.id.air_pressure);
        visibility=(TextView)view.findViewById(R.id.visibility);
        wind_scale=(TextView)view.findViewById(R.id.wind_scale);
        wind_speed=(TextView)view.findViewById(R.id.wind_speed);
        wind_degree=(TextView)view.findViewById(R.id.wind_degree);
        wind_direction=(TextView)view.findViewById(R.id.wind_orientation);

        sport_index=(TextView)view.findViewById(R.id.sport_index);
        flu_index=(TextView)view.findViewById(R.id.flu_index);
        travel_index=(TextView)view.findViewById(R.id.travel_index);
        ultraviolet_index=(TextView)view.findViewById(R.id.ultraviolet_index);
        wear_index=(TextView)view.findViewById(R.id.wear_index);

        df_weekday_0=(TextView)view.findViewById(R.id.df_weekday_0);
        df_weekday_1=(TextView)view.findViewById(R.id.df_weekday_1);
        df_weekday_2=(TextView)view.findViewById(R.id.df_weekday_2);
        df_weekday_3=(TextView)view.findViewById(R.id.df_weekday_3);
        df_weekday_4=(TextView)view.findViewById(R.id.df_weekday_4);
        df_weekday_5=(TextView)view.findViewById(R.id.df_weekday_5);
        df_weekday_6=(TextView)view.findViewById(R.id.df_weekday_6);
        df_weekday_7=(TextView)view.findViewById(R.id.df_weekday_7);

        df_date_0=(TextView)view.findViewById(R.id.df_date_0);
        df_date_1=(TextView)view.findViewById(R.id.df_date_1);
        df_date_2=(TextView)view.findViewById(R.id.df_date_2);
        df_date_3=(TextView)view.findViewById(R.id.df_date_3);
        df_date_4=(TextView)view.findViewById(R.id.df_date_4);
        df_date_5=(TextView)view.findViewById(R.id.df_date_5);
        df_date_6=(TextView)view.findViewById(R.id.df_date_6);
        df_date_7=(TextView)view.findViewById(R.id.df_date_7);

        df_cond_day_txt_0=(TextView)view.findViewById(R.id.df_cond_day_txt_0);
        df_cond_day_txt_1=(TextView)view.findViewById(R.id.df_cond_day_txt_1);
        df_cond_day_txt_2=(TextView)view.findViewById(R.id.df_cond_day_txt_2);
        df_cond_day_txt_3=(TextView)view.findViewById(R.id.df_cond_day_txt_3);
        df_cond_day_txt_4=(TextView)view.findViewById(R.id.df_cond_day_txt_4);
        df_cond_day_txt_5=(TextView)view.findViewById(R.id.df_cond_day_txt_5);
        df_cond_day_txt_6=(TextView)view.findViewById(R.id.df_cond_day_txt_6);
        df_cond_day_txt_7=(TextView)view.findViewById(R.id.df_cond_day_txt_7);

        df_cond_day_image_0=(ImageView)view.findViewById(R.id.df_cond_day_image_0);
        df_cond_day_image_1=(ImageView)view.findViewById(R.id.df_cond_day_image_1);
        df_cond_day_image_2=(ImageView)view.findViewById(R.id.df_cond_day_image_2);
        df_cond_day_image_3=(ImageView)view.findViewById(R.id.df_cond_day_image_3);
        df_cond_day_image_4=(ImageView)view.findViewById(R.id.df_cond_day_image_4);
        df_cond_day_image_5=(ImageView)view.findViewById(R.id.df_cond_day_image_5);
        df_cond_day_image_6=(ImageView)view.findViewById(R.id.df_cond_day_image_6);
        df_cond_day_image_7=(ImageView)view.findViewById(R.id.df_cond_day_image_7);

        df_cond_night_image_0=(ImageView)view.findViewById(R.id.df_cond_night_image_0);
        df_cond_night_image_1=(ImageView)view.findViewById(R.id.df_cond_night_image_1);
        df_cond_night_image_2=(ImageView)view.findViewById(R.id.df_cond_night_image_2);
        df_cond_night_image_3=(ImageView)view.findViewById(R.id.df_cond_night_image_3);
        df_cond_night_image_4=(ImageView)view.findViewById(R.id.df_cond_night_image_4);
        df_cond_night_image_5=(ImageView)view.findViewById(R.id.df_cond_night_image_5);
        df_cond_night_image_6=(ImageView)view.findViewById(R.id.df_cond_night_image_6);
        df_cond_night_image_7=(ImageView)view.findViewById(R.id.df_cond_night_image_7);

        df_cond_night_txt_0=(TextView)view.findViewById(R.id.df_cond_night_txt_0);
        df_cond_night_txt_1=(TextView)view.findViewById(R.id.df_cond_night_txt_1);
        df_cond_night_txt_2=(TextView)view.findViewById(R.id.df_cond_night_txt_2);
        df_cond_night_txt_3=(TextView)view.findViewById(R.id.df_cond_night_txt_3);
        df_cond_night_txt_4=(TextView)view.findViewById(R.id.df_cond_night_txt_4);
        df_cond_night_txt_5=(TextView)view.findViewById(R.id.df_cond_night_txt_5);
        df_cond_night_txt_6=(TextView)view.findViewById(R.id.df_cond_night_txt_6);
        df_cond_night_txt_7=(TextView)view.findViewById(R.id.df_cond_night_txt_7);

        df_wind_direction_0=(TextView)view.findViewById(R.id.df_wind_direction_0);
        df_wind_direction_1=(TextView)view.findViewById(R.id.df_wind_direction_1);
        df_wind_direction_2=(TextView)view.findViewById(R.id.df_wind_direction_2);
        df_wind_direction_3=(TextView)view.findViewById(R.id.df_wind_direction_3);
        df_wind_direction_4=(TextView)view.findViewById(R.id.df_wind_direction_4);
        df_wind_direction_5=(TextView)view.findViewById(R.id.df_wind_direction_5);
        df_wind_direction_6=(TextView)view.findViewById(R.id.df_wind_direction_6);
        df_wind_direction_7=(TextView)view.findViewById(R.id.df_wind_direction_7);

        df_wind_scale_0=(TextView)view.findViewById(R.id.df_wind_scale_0);
        df_wind_scale_1=(TextView)view.findViewById(R.id.df_wind_scale_1);
        df_wind_scale_2=(TextView)view.findViewById(R.id.df_wind_scale_2);
        df_wind_scale_3=(TextView)view.findViewById(R.id.df_wind_scale_3);
        df_wind_scale_4=(TextView)view.findViewById(R.id.df_wind_scale_4);
        df_wind_scale_5=(TextView)view.findViewById(R.id.df_wind_scale_5);
        df_wind_scale_6=(TextView)view.findViewById(R.id.df_wind_scale_6);
        df_wind_scale_7=(TextView)view.findViewById(R.id.df_wind_scale_7);

        yestDay_day_layout=(LinearLayout)view.findViewById(R.id.yestDay_day_layout);
        yestDay_night_layout=(LinearLayout)view.findViewById(R.id.yestDay_night_layout);
        mTempTrendView=(TemperatureTrendView)view.findViewById(R.id.daily_forecast_tmp_trend_view);

        mRefreshableView=(RefreshableView) view.findViewById(R.id.refreshable_view_layout);
        mRefreshableView.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
    }


    public void refreshData(){
        mActivity.initDate();
        String httpUrl = "http://apis.baidu.com/heweather/weather/free";
        String urlPara=httpUrl+"?city="+mCountyName;

        //read the weather data from the server,then save it to local storage，
        // and then show the weather Info.
        HttpUtil.sendHttpRequest(urlPara, true, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                DataDisposalUtil.handleWeatherDataResponse(mActivity,response,mCountyCode);
                mspf.edit().putString("update_flag","true").apply();
                //save weather data of today for use on tomorrow
                saveDataForYestday();
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showWeather();
                        if(mActivity.ffc_flag[mPosition].equals("true")){
                            Date dt = new Date();
                            DateFormat df = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.CHINA);
                            mspf.edit().putString("last_updated_at", df.format(dt)).apply();
                            Toast.makeText(mActivity,mCountyName+"天气数据更新成功",Toast.LENGTH_SHORT).show();
                            mActivity.ffc_flag[mPosition]="false";
                        }

                    }
                });

            }

            @Override
            public void onError(Exception e) {
                mspf.edit().putString("update_flag","false").apply();
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String dateString=mspf.getString("df_date_day1","");
                        if(!dateString.equals("")){
                            showWeather();
                        }
                        if(mActivity.ffc_flag[mPosition].equals("true")){
                            Toast.makeText(mActivity,mCountyName+"天气数据更新失败，\n请检查网络连接",
                                    Toast.LENGTH_LONG).show();
                            mActivity.ffc_flag[mPosition]="false";
                        }

                    }
                });

            }
        });

    }


    //save weather data of today for use on tomorrow
    private void saveDataForYestday(){
        String yestDay_date;
        String transitionDay_date=mspf.getString("transitionDay_date","");
        String today_date=mspf.getString("df_date_day1","");
            SharedPreferences.Editor mspf_editor=mspf.edit();
            yestDay_date=transitionDay_date;
            transitionDay_date=today_date;
            mspf_editor.putString("yestDay_date",yestDay_date);
            mspf_editor.putString("transitionDay_date",transitionDay_date);

            String yestDay_weekDay=mspf.getString("transitionDay_weekDay","");
            String transitionDay_weekDay=wnl_spf.getString("df_weekDay_day1","");
            mspf_editor.putString("yestDay_weekDay",yestDay_weekDay);
            mspf_editor.putString("transitionDay_weekDay",transitionDay_weekDay);

            String yestDay_dCond_txt=mspf.getString("transitionDay_dCond_txt","");
            String transitionDay_dCond_txt=mspf.getString("df_conditionText_d_day1","");
            mspf_editor.putString("yestDay_dCond_txt",yestDay_dCond_txt);
            mspf_editor.putString("transitionDay_dCond_txt",transitionDay_dCond_txt);

            String yestDay_nCond_txt=mspf.getString("transitionDay_nCond_txt","");
            String transitionDay_nCond_txt=mspf.getString("df_conditionText_n_day1","");
            mspf_editor.putString("yestDay_nCond_txt",yestDay_nCond_txt);
            mspf_editor.putString("transitionDay_nCond_txt",transitionDay_nCond_txt);

            String yestDay_wind_dir=mspf.getString("transitionDay_wind_dir","");
            String transitionDay_wind_dir=mspf.getString("df_wind_dir_day1","");
            mspf_editor.putString("yestDay_wind_dir",yestDay_wind_dir);
            mspf_editor.putString("transitionDay_wind_dir",transitionDay_wind_dir);

            String yestDay_wind_scale=mspf.getString("transitionDay_wind_scale","");
            String transitionDay_wind_scale=mspf.getString("df_wind_sc_day1","");
            mspf_editor.putString("yestDay_wind_scale",yestDay_wind_scale);
            mspf_editor.putString("transitionDay_wind_scale",transitionDay_wind_scale);

            String yestDay_tmp_max=mspf.getString("transitionDay_tmp_max","");
            String transitionDay_tmp_max=mspf.getString("df_tmp_max_day1","");
            mspf_editor.putString("yestDay_tmp_max",yestDay_tmp_max);
            mspf_editor.putString("transitionDay_tmp_max",transitionDay_tmp_max);

            String yestDay_tmp_min=mspf.getString("transitionDay_tmp_min","");
            String transitionDay_tmp_min=mspf.getString("df_tmp_min_day1","");
            mspf_editor.putString("yestDay_tmp_min",yestDay_tmp_min);
            mspf_editor.putString("transitionDay_tmp_min",transitionDay_tmp_min);

            mspf_editor.apply();
    }
    
    //show the weather Info on the screen

    public void showWeather(){
        //refresh mCountyCode bound to RefreshableView
        mRefreshableView.setData(mCountyCode);
        
        //show weather Info
        String tmpString;
        String[] tmpStrArray;
        
        tmpString="今天   "+wnl_spf.getString("weekDay","周日");
        weekDay.setText(tmpString);
        tmpString=wnl_spf.getString("date_flag","");
        if(!"".equals(tmpString)){
            tmpStrArray=tmpString.split("-");
            tmpString=tmpStrArray[1]+"月"+tmpStrArray[2]+"日";
            sunDay.setText(tmpString);
        }
        tmpString=wnl_spf.getString("nongLi","十二月二十八");
        moonDay.setText(tmpString);

        tmpString=mspf.getString("now_tmp","26")+"°";
        now_tmp.setText(tmpString);
        tmpString=mspf.getString("df_tmp_min_day1","23")+"°~"
                +mspf.getString("df_tmp_max_day1","31")+"°";
        now_tmp_range.setText(tmpString);
        tmpString=mspf.getString("aqi_qlty","无");
        now_air_quality.setText(tmpString);
        air_quality_cardView.setCardBackgroundColor(MainActivity.get_aql_color.get(tmpString));
        tmpString=mspf.getString("now_cond_txt","未知");
        now_weather_image.setImageResource(MainActivity.weatherCodeMap.get(tmpString));
        now_weather_txt.setText(tmpString);
        tmpString=mspf.getString("now_wind_sc","？");
        if(!"微风".equals(tmpString)){
            tmpString=tmpString+"级";
        }
        now_wind_sc.setText(tmpString);

        tmpString=mspf.getString("aqi_aqi","无");
        aqi.setText(tmpString);
        tmpString=mspf.getString("aqi_pm25","无");
        pm25.setText(tmpString);
        tmpString=mspf.getString("aqi_pm10","无");
        pm10.setText(tmpString);
        tmpString=mspf.getString("aqi_co","无");
        co.setText(tmpString);
        tmpString=mspf.getString("aqi_so2","无");
        so2.setText(tmpString);
        tmpString=mspf.getString("aqi_no2","无");
        no2.setText(tmpString);
        tmpString=mspf.getString("aqi_o3","无");
        o3.setText(tmpString);

        tmpString=mspf.getString("now_fl","无");
        feeling_tmp.setText(tmpString);
        tmpString=mspf.getString("now_hum","无");
        relative_humidity.setText(tmpString);
        tmpString=mspf.getString("now_pcpn","无");
        precipitation.setText(tmpString);
        tmpString=mspf.getString("now_pres","无");
        air_pressure.setText(tmpString);
        tmpString=mspf.getString("now_vis","无");
        visibility.setText(tmpString);
        tmpString=mspf.getString("now_wind_sc","无");
        wind_scale.setText(tmpString);
        tmpString=mspf.getString("now_wind_spd","无");
        wind_speed.setText(tmpString);
        tmpString=mspf.getString("now_wind_deg","无");
        wind_degree.setText(tmpString);
        tmpString=mspf.getString("now_wind_dir","无");
        wind_direction.setText(tmpString);

        tmpString="运动指数："+mspf.getString("suggestion_sport_brf","无");
        sport_index.setText(tmpString);
        tmpString="感冒指数："+mspf.getString("suggestion_flu_brf","无");
        flu_index.setText(tmpString);
        tmpString="旅游指数："+mspf.getString("suggestion_trav_brf","无");
        travel_index.setText(tmpString);
        tmpString="紫外线指数："+mspf.getString("suggestion_uv_brf","无");
        ultraviolet_index.setText(tmpString);
        tmpString="穿衣指数："+mspf.getString("suggestion_drsg_brf","无");
        wear_index.setText(tmpString);

        mTempTrendView.clearData();
        tmpString=mspf.getString("yestDay_date","");
        if(!TextUtils.isEmpty(tmpString)){
            tmpStrArray=tmpString.split("-");
            tmpString=tmpStrArray[1]+"/"+tmpStrArray[2];
            df_date_0.setText(tmpString);

            tmpString=mspf.getString("yestDay_dCond_txt","未知");
            df_cond_day_txt_0.setText(tmpString);
            df_cond_day_image_0.setImageResource(MainActivity.weatherCodeMap.get(tmpString));

            tmpString=mspf.getString("yestDay_nCond_txt","未知");
            df_cond_night_txt_0.setText(tmpString);
            df_cond_night_image_0.setImageResource(MainActivity.weatherCodeMap.get(tmpString));

            tmpString=mspf.getString("yestDay_wind_dir","无");
            df_wind_direction_0.setText(tmpString);
            tmpString=mspf.getString("yestDay_wind_scale","无");
            if(!"微风".equals(tmpString)){
                tmpString=tmpString+"级";
            }
            df_wind_scale_0.setText(tmpString);

            tmpString=mspf.getString("yestDay_tmp_max","");
            mTempTrendView.addTopTemp(tmpString);
            tmpString=mspf.getString("yestDay_tmp_min","");
            mTempTrendView.addLowTemp(tmpString);
            yestDay_day_layout.setVisibility(View.VISIBLE);
            yestDay_night_layout.setVisibility(View.VISIBLE);
            float pxScale= DisplayUtil.pxScale(mActivity);
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams((int)(432*pxScale),(int)(150*pxScale));
            mTempTrendView.setLayoutParams(params);

        }

        tmpString=mspf.getString("df_date_day1","");
        tmpStrArray=tmpString.split("-");
        tmpString=tmpStrArray[1]+"/"+tmpStrArray[2];
        df_date_1.setText(tmpString);
        tmpString=mspf.getString("df_conditionText_d_day1","未知");
        df_cond_day_txt_1.setText(tmpString);
        df_cond_day_image_1.setImageResource(MainActivity.weatherCodeMap.get(tmpString));
        tmpString=mspf.getString("df_conditionText_n_day1","未知");
        df_cond_night_txt_1.setText(tmpString);
        df_cond_night_image_1.setImageResource(MainActivity.weatherCodeMap.get(tmpString));
        tmpString=mspf.getString("df_wind_dir_day1","");
        df_wind_direction_1.setText(tmpString);
        tmpString=mspf.getString("df_wind_sc_day1","");
        if(!"微风".equals(tmpString)){
            tmpString=tmpString+"级";
        }
        df_wind_scale_1.setText(tmpString);
        tmpString=mspf.getString("df_tmp_max_day1","");
        mTempTrendView.addTopTemp(tmpString);
        tmpString=mspf.getString("df_tmp_min_day1","");
        mTempTrendView.addLowTemp(tmpString);

        tmpString=wnl_spf.getString("df_weekDay_day2","");
        df_weekday_2.setText(tmpString);
        tmpString=mspf.getString("df_date_day2","");
        tmpStrArray=tmpString.split("-");
        tmpString=tmpStrArray[1]+"/"+tmpStrArray[2];
        df_date_2.setText(tmpString);
        tmpString=mspf.getString("df_conditionText_d_day2","未知");
        df_cond_day_txt_2.setText(tmpString);
        df_cond_day_image_2.setImageResource(MainActivity.weatherCodeMap.get(tmpString));
        tmpString=mspf.getString("df_conditionText_n_day2","未知");
        df_cond_night_txt_2.setText(tmpString);
        df_cond_night_image_2.setImageResource(MainActivity.weatherCodeMap.get(tmpString));
        tmpString=mspf.getString("df_wind_dir_day2","");
        df_wind_direction_2.setText(tmpString);
        tmpString=mspf.getString("df_wind_sc_day2","");
        if(!"微风".equals(tmpString)){
            tmpString=tmpString+"级";
        }
        df_wind_scale_2.setText(tmpString);
        tmpString=mspf.getString("df_tmp_max_day2","");
        mTempTrendView.addTopTemp(tmpString);
        tmpString=mspf.getString("df_tmp_min_day2","");
        mTempTrendView.addLowTemp(tmpString);

        tmpString=wnl_spf.getString("df_weekDay_day3","");
        df_weekday_3.setText(tmpString);
        tmpString=mspf.getString("df_date_day3","");
        tmpStrArray=tmpString.split("-");
        tmpString=tmpStrArray[1]+"/"+tmpStrArray[2];
        df_date_3.setText(tmpString);
        tmpString=mspf.getString("df_conditionText_d_day3","未知");
        df_cond_day_txt_3.setText(tmpString);
        df_cond_day_image_3.setImageResource(MainActivity.weatherCodeMap.get(tmpString));
        tmpString=mspf.getString("df_conditionText_n_day3","未知");
        df_cond_night_txt_3.setText(tmpString);
        df_cond_night_image_3.setImageResource(MainActivity.weatherCodeMap.get(tmpString));
        tmpString=mspf.getString("df_wind_dir_day3","");
        df_wind_direction_3.setText(tmpString);
        tmpString=mspf.getString("df_wind_sc_day3","");
        if(!"微风".equals(tmpString)){
            tmpString=tmpString+"级";
        }
        df_wind_scale_3.setText(tmpString);
        tmpString=mspf.getString("df_tmp_max_day3","");
        mTempTrendView.addTopTemp(tmpString);
        tmpString=mspf.getString("df_tmp_min_day3","");
        mTempTrendView.addLowTemp(tmpString);

        tmpString=wnl_spf.getString("df_weekDay_day4","");
        df_weekday_4.setText(tmpString);
        tmpString=mspf.getString("df_date_day4","");
        tmpStrArray=tmpString.split("-");
        tmpString=tmpStrArray[1]+"/"+tmpStrArray[2];
        df_date_4.setText(tmpString);
        tmpString=mspf.getString("df_conditionText_d_day4","未知");
        df_cond_day_txt_4.setText(tmpString);
        df_cond_day_image_4.setImageResource(MainActivity.weatherCodeMap.get(tmpString));
        tmpString=mspf.getString("df_conditionText_n_day4","未知");
        df_cond_night_txt_4.setText(tmpString);
        df_cond_night_image_4.setImageResource(MainActivity.weatherCodeMap.get(tmpString));
        tmpString=mspf.getString("df_wind_dir_day4","");
        df_wind_direction_4.setText(tmpString);
        tmpString=mspf.getString("df_wind_sc_day4","");
        if(!"微风".equals(tmpString)){
            tmpString=tmpString+"级";
        }
        df_wind_scale_4.setText(tmpString);
        tmpString=mspf.getString("df_tmp_max_day4","");
        mTempTrendView.addTopTemp(tmpString);
        tmpString=mspf.getString("df_tmp_min_day4","");
        mTempTrendView.addLowTemp(tmpString);

        tmpString=wnl_spf.getString("df_weekDay_day5","");
        df_weekday_5.setText(tmpString);
        tmpString=mspf.getString("df_date_day5","");
        tmpStrArray=tmpString.split("-");
        tmpString=tmpStrArray[1]+"/"+tmpStrArray[2];
        df_date_5.setText(tmpString);
        tmpString=mspf.getString("df_conditionText_d_day5","未知");
        df_cond_day_txt_5.setText(tmpString);
        df_cond_day_image_5.setImageResource(MainActivity.weatherCodeMap.get(tmpString));
        tmpString=mspf.getString("df_conditionText_n_day5","未知");
        df_cond_night_txt_5.setText(tmpString);
        df_cond_night_image_5.setImageResource(MainActivity.weatherCodeMap.get(tmpString));
        tmpString=mspf.getString("df_wind_dir_day5","");
        df_wind_direction_5.setText(tmpString);
        tmpString=mspf.getString("df_wind_sc_day5","");
        if(!"微风".equals(tmpString)){
            tmpString=tmpString+"级";
        }
        df_wind_scale_5.setText(tmpString);
        tmpString=mspf.getString("df_tmp_max_day5","");
        mTempTrendView.addTopTemp(tmpString);
        tmpString=mspf.getString("df_tmp_min_day5","");
        mTempTrendView.addLowTemp(tmpString);

        tmpString=wnl_spf.getString("df_weekDay_day6","");
        df_weekday_6.setText(tmpString);
        tmpString=mspf.getString("df_date_day6","");
        tmpStrArray=tmpString.split("-");
        tmpString=tmpStrArray[1]+"/"+tmpStrArray[2];
        df_date_6.setText(tmpString);
        tmpString=mspf.getString("df_conditionText_d_day6","未知");
        df_cond_day_txt_6.setText(tmpString);
        df_cond_day_image_6.setImageResource(MainActivity.weatherCodeMap.get(tmpString));
        tmpString=mspf.getString("df_conditionText_n_day6","未知");
        df_cond_night_txt_6.setText(tmpString);
        df_cond_night_image_6.setImageResource(MainActivity.weatherCodeMap.get(tmpString));
        tmpString=mspf.getString("df_wind_dir_day6","");
        df_wind_direction_6.setText(tmpString);
        tmpString=mspf.getString("df_wind_sc_day6","");
        if(!"微风".equals(tmpString)){
            tmpString=tmpString+"级";
        }
        df_wind_scale_6.setText(tmpString);
        tmpString=mspf.getString("df_tmp_max_day6","");
        mTempTrendView.addTopTemp(tmpString);
        tmpString=mspf.getString("df_tmp_min_day6","");
        mTempTrendView.addLowTemp(tmpString);

        tmpString=wnl_spf.getString("df_weekDay_day7","");
        df_weekday_7.setText(tmpString);
        tmpString=mspf.getString("df_date_day7","");
        tmpStrArray=tmpString.split("-");
        tmpString=tmpStrArray[1]+"/"+tmpStrArray[2];
        df_date_7.setText(tmpString);
        tmpString=mspf.getString("df_conditionText_d_day7","未知");
        df_cond_day_txt_7.setText(tmpString);
        df_cond_day_image_7.setImageResource(MainActivity.weatherCodeMap.get(tmpString));
        tmpString=mspf.getString("df_conditionText_n_day7","未知");
        df_cond_night_txt_7.setText(tmpString);
        df_cond_night_image_7.setImageResource(MainActivity.weatherCodeMap.get(tmpString));
        tmpString=mspf.getString("df_wind_dir_day7","");
        df_wind_direction_7.setText(tmpString);
        tmpString=mspf.getString("df_wind_sc_day7","");
        if(!"微风".equals(tmpString)){
            tmpString=tmpString+"级";
        }
        df_wind_scale_7.setText(tmpString);
        tmpString=mspf.getString("df_tmp_max_day7","");
        mTempTrendView.addTopTemp(tmpString);
        tmpString=mspf.getString("df_tmp_min_day7","");
        mTempTrendView.addLowTemp(tmpString);
        mTempTrendView.reDraw();
    }

}

