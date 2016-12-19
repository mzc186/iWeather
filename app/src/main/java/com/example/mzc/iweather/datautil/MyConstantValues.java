package com.example.mzc.iweather.datautil;

import com.example.mzc.iweather.R;

import java.util.HashMap;

/**
 * Created by MZC on 10/19/2016.
 */
public class MyConstantValues {

    public static HashMap<String,Integer> getAqlColorMap(){
        HashMap<String,Integer> get_aql_color=new HashMap<>();
        get_aql_color.put("优",0xFF15ce15);
        get_aql_color.put("良",0xFFFFD500);
        get_aql_color.put("轻度污染",0xFFFF8C00);
        get_aql_color.put("中度污染",0xFFFF0000);
        get_aql_color.put("重度污染",0XFF800080);
        get_aql_color.put("严重污染",0xFFA52A2A);
        get_aql_color.put("无",0x3C000000);

        return get_aql_color;
    }

    // method for initialization of constant data
    public static HashMap<String,Integer> getWeatherCodeMap(){
        //initialize the weather code data
        HashMap<String,Integer> weatherCodeMap=new HashMap<>();
        weatherCodeMap.put("晴", R.drawable.org3_ww0);
        weatherCodeMap.put("多云",R.drawable.ic_weather_overcast);
        weatherCodeMap.put("少云",R.drawable.org3_ww2);
        weatherCodeMap.put("晴间多云",R.drawable.org3_ww1);
        weatherCodeMap.put("阴",R.drawable.ic_weather_overcast);
        weatherCodeMap.put("有风",R.drawable.breeze);
        weatherCodeMap.put("平静",R.drawable.breeze);
        weatherCodeMap.put("微风",R.drawable.breeze);
        weatherCodeMap.put("和风",R.drawable.breeze);
        weatherCodeMap.put("清风",R.drawable.breeze);
        weatherCodeMap.put("强风/劲风",R.drawable.strong_breeze);
        weatherCodeMap.put("疾风",R.drawable.gale);
        weatherCodeMap.put("大风",R.drawable.gale);
        weatherCodeMap.put("烈风",R.drawable.gale);
        weatherCodeMap.put("风暴",R.drawable.storm);
        weatherCodeMap.put("狂暴风",R.drawable.storm);
        weatherCodeMap.put("飓风",R.drawable.storm);
        weatherCodeMap.put("龙卷风",R.drawable.storm);
        weatherCodeMap.put("热带风暴",R.drawable.storm);
        weatherCodeMap.put("阵雨",R.drawable.org3_ww3);
        weatherCodeMap.put("强阵雨",R.drawable.ic_weather_heavyrain);
        weatherCodeMap.put("雷阵雨",R.drawable.ic_weather_thundershower);
        weatherCodeMap.put("强雷阵雨",R.drawable.ic_weather_thundershower);
        weatherCodeMap.put("强雷阵雨伴有冰雹",R.drawable.org3_ww5);
        weatherCodeMap.put("小雨",R.drawable.org3_ww7);
        weatherCodeMap.put("中雨",R.drawable.org3_ww8);
        weatherCodeMap.put("大雨",R.drawable.org3_ww19);
        weatherCodeMap.put("极端降雨",R.drawable.org3_ww10);
        weatherCodeMap.put("毛毛雨/细雨",R.drawable.org3_ww7);
        weatherCodeMap.put("暴雨",R.drawable.org3_ww19);
        weatherCodeMap.put("大暴雨",R.drawable.org3_ww9);
        weatherCodeMap.put("特大暴雨",R.drawable.org3_ww10);
        weatherCodeMap.put("冻雨",R.drawable.ic_weather_hail);
        weatherCodeMap.put("小雪",R.drawable.org3_ww14);
        weatherCodeMap.put("中雪",R.drawable.org3_ww15);
        weatherCodeMap.put("大雪",R.drawable.org3_ww16);
        weatherCodeMap.put("暴雪",R.drawable.org3_ww17);
        weatherCodeMap.put("雨夹雪",R.drawable.org3_ww6);
        weatherCodeMap.put("雨雪天气",R.drawable.org3_ww6);
        weatherCodeMap.put("阵雨夹雪",R.drawable.org3_ww6);
        weatherCodeMap.put("阵雪",R.drawable.org3_ww15);
        weatherCodeMap.put("薄雾",R.drawable.ic_weather_foggy);
        weatherCodeMap.put("雾",R.drawable.ic_weather_foggy);
        weatherCodeMap.put("霾",R.drawable.ic_weather_mai);
        weatherCodeMap.put("扬沙",R.drawable.org3_ww29);
        weatherCodeMap.put("浮尘",R.drawable.org3_ww36);
        weatherCodeMap.put("火山灰",R.drawable.ic_weather_sand);
        weatherCodeMap.put("沙尘暴",R.drawable.org3_ww45);
        weatherCodeMap.put("强沙尘暴",R.drawable.org3_ww45);
        weatherCodeMap.put("热",R.drawable.hot);
        weatherCodeMap.put("冷",R.drawable.cold);
        weatherCodeMap.put("未知",R.drawable.org3_wna);

        return  weatherCodeMap;
    }

    public static int[] getBackGroundRes(){
        int[] backGroundRes;
        backGroundRes=new int[]{R.drawable.wp0,R.drawable.wp1,R.drawable.wp2,R.drawable.wp3,
                R.drawable.wp4,R.drawable.wp5,R.drawable.wp6,R.drawable.wp7,
                R.drawable.wp8,R.drawable.wp9,R.drawable.wp10,R.drawable.wp11,
                R.drawable.wp12,R.drawable.wp13,R.drawable.wp14,R.drawable.wp15,
                R.drawable.wp16,R.drawable.wp17};
        return backGroundRes;
    }

}
