package com.example.mzc.iweather.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mzc.iweather.R;
import com.example.mzc.iweather.datautil.WeatherDB;
import com.example.mzc.iweather.model.City;
import com.example.mzc.iweather.model.County;
import com.example.mzc.iweather.model.Province;
import com.example.mzc.iweather.datautil.DataDisposalUtil;
import com.example.mzc.iweather.netutil.HttpCallbackListener;
import com.example.mzc.iweather.netutil.HttpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MZC on 9/6/2016.
 */
public class ChooseCityActivity extends Activity {
    public final static int LEVEL_PROVINCE=0;
    public final static int LEVEL_CITY=1;
    public final static int LEVEL_COUNTY=2;

    private ProgressDialog progressDialog;
    private TextView titleText;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private WeatherDB weatherDB;
    private List<String> dataList=new ArrayList<>();

    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;

    private Province selectedProvince;
    private City selectedCity;
    private County selectedCounty;
    private int currentLevel;
    private int selectedCountyCount;
    private boolean selected_flag;
    private SharedPreferences wnl_spf;
    private Button mBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_area);
        wnl_spf=getSharedPreferences("WanNianLi",MODE_PRIVATE);
        listView=(ListView)findViewById(R.id.list_view);
        titleText=(TextView)findViewById(R.id.title_text);
        mBackButton=(Button)findViewById(R.id.backbutton_choosearea_activity);
        adapter=new ArrayAdapter<String>(this,R.layout.custom_list_item_1,dataList);
        listView.setAdapter(adapter);
        weatherDB=WeatherDB.getInstance(getApplicationContext());
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(currentLevel==LEVEL_PROVINCE){
                    selectedProvince=provinceList.get(position);
                    queryCities();
                }
                else if(currentLevel==LEVEL_CITY){
                    selectedCity=cityList.get(position);
                    queryCounties();
                }
                else if (currentLevel==LEVEL_COUNTY){
                    selected_flag=false;
                    selectedCounty=countyList.get(position);
                    String tmpStr=wnl_spf.getString("selectedCountyCount","0");
                    selectedCountyCount=Integer.parseInt(tmpStr);
                    SharedPreferences.Editor mEditor=wnl_spf.edit();
                    tmpStr=selectedCounty.getCountyCode();

                    if(selectedCountyCount!=0){
                        for(int i=0;i<selectedCountyCount;i++){
                            if(tmpStr.equals(wnl_spf.getString("selectedCountyCode"+i,""))){
                                selected_flag=true;
                                Toast.makeText(ChooseCityActivity.this,"该城市已经存在",Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                    }

                    if(!selected_flag){
                        mEditor.putString("selectedCountyCode"+selectedCountyCount,tmpStr);
                        tmpStr=selectedCounty.getCountyName();
                        mEditor.putString("selectedCountyName"+selectedCountyCount,tmpStr);

                        String httpUrl = "http://apis.baidu.com/heweather/weather/free";
                        String urlPara=httpUrl+"?city="+tmpStr;
                        //read the weather data from the server,then save it to local storage，
                        HttpUtil.sendHttpRequest(urlPara, true, new HttpCallbackListener() {
                            @Override
                            public void onFinish(String response) {
                                DataDisposalUtil.handleWeatherDataResponse(ChooseCityActivity.this,
                                        response,selectedCounty.getCountyCode());
                            }
                            @Override
                            public void onError(Exception e) {
                                e.printStackTrace();
                            }
                        });

                        sleep(500);
                        selectedCountyCount++;
                        mEditor.putString("selectedCountyCount",""+selectedCountyCount);
                        mEditor.apply();
                        Intent  intent=new Intent();
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                }
            }
        });
        queryProvinces();//load the data of Provinces
    }


    private void sleep(int sleepTime){
        try{
            Thread.sleep(sleepTime);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * query all provinces of the country firstly in database, if the result returned is null,
     * then query from the server.
     */
    private void queryProvinces(){
        provinceList=weatherDB.loadProvinces();
        if(provinceList.size()>0){
            dataList.clear();
            for(Province province:provinceList){
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText("中国");
            currentLevel=LEVEL_PROVINCE;
        }
        else {
            queryFromServer(null,"province");
        }
    }

    /**
     * query all the cities of the Province selected, query from the dataBase first,
     * if no result returned, then query from the server.
     */
    private void queryCities(){
        cityList=weatherDB.loadCities(selectedProvince.getId());
        if(cityList.size()>0){
            dataList.clear();
            for(City city:cityList){
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedProvince.getProvinceName());
            currentLevel=LEVEL_CITY;
        }else{
            queryFromServer(selectedProvince.getProvinceCode(),"city");
        }
    }

    /**
     * query all the counties of the City selected, query from the dataBase first,
     * if no result returned, then query from the server.
     */
    private void queryCounties(){
        countyList=weatherDB.loadCounties(selectedCity.getId());
        if(countyList.size()>0){
            dataList.clear();
            for(County county:countyList){
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedCity.getCityName());
            currentLevel=LEVEL_COUNTY;
        }
        else{
            queryFromServer(selectedCity.getCityCode(),"county");
        }
    }

    /**
     * query the specified data according to the code and type requested
     */
    private void queryFromServer(final String code,final String type){
        String address;
        if(!TextUtils.isEmpty(code)){
            address="http://www.weather.com.cn/data/list3/city"+code+".xml";
        }
        else{
            address="http://www.weather.com.cn/data/list3/city.xml";
        }
        showProgressDialog();
        HttpUtil.sendHttpRequest(address,false,new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                boolean result=false;
                if("province".equals(type)){
                    result= DataDisposalUtil.handleProvincesResponse(weatherDB,response);
                }
                else if("city".equals(type)){
                    result=DataDisposalUtil.handleCitiesResponse(weatherDB,response,selectedProvince.getId());
                }
                else if("county".equals(type)){
                    result=DataDisposalUtil.handleCountiesResponse(weatherDB,response,selectedCity.getId());
                }
                if(result){
                    /**
                     *return to the main thread to deal with the issue through the method
                     *  runOnUiThread()
                     */
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        if("province".equals(type)){
                            queryProvinces();
                        }
                        else if("city".equals(type)){
                            queryCities();
                        }
                        else if("county".equals(type)){
                            queryCounties();
                        }

                    }
                });
                }
            }

            @Override
            public void onError(Exception e) {
                /**
                 *return to the main thread to deal with the issue through the method
                 *  runOnUiThread()
                 */
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(ChooseCityActivity.this,"数据加载失败，请检查网络连接",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    /**
     * show the Progress Dialog
     */
    private void showProgressDialog(){
        if(progressDialog==null){
            progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * close the Progress Dialog
     */
    private void closeProgressDialog(){
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }


    /**
     * capture the back button, back to the cityList or provinceList according to
     * the currently Level
     */
    @Override
    public void onBackPressed() {
        if(currentLevel==LEVEL_COUNTY){
            queryCities();
        }
        else if(currentLevel==LEVEL_CITY){
            queryProvinces();
        }
        else {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Runtime.getRuntime().gc();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Runtime.getRuntime().gc();
    }
}
