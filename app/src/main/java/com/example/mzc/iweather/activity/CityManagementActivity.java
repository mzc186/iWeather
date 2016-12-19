package com.example.mzc.iweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mzc.iweather.R;
import com.example.mzc.iweather.uicomponents.RecyclerAdapter;
import com.example.mzc.iweather.model.RecyclerAdapterDataModle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MZC on 10/19/2016.
 */
public class CityManagementActivity extends Activity{

    private Button button_edit_city;
    private RecyclerAdapter mAdapter;
    private SharedPreferences wnl_spf;
    private List<RecyclerAdapterDataModle> mListData;
    private boolean button_edit_stateflag;
    private boolean activity_flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.citymanagement_activity_layout);
        mListData=new ArrayList<>();
        activity_flag=false;
        wnl_spf =getSharedPreferences("WanNianLi",MODE_PRIVATE);
        initAdapterData();
        initWidgets();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(activity_flag) {
            initAdapterData();
            mAdapter.notifyDataSetChanged();

        }
        activity_flag=true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                break;
            default:
                break;
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

    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        setResult(RESULT_OK,intent);
        finish();
    }

    private void initWidgets(){
        Button button_back=(Button)findViewById(R.id.backbutton_citymanagement_activity);
        button_edit_city=(Button)findViewById(R.id.editbutton_citymanagement_activity);
        Button button_add_city=(Button)findViewById(R.id.addbutton_citymanagement_activity);
        RecyclerView mRecyclerView=(RecyclerView)findViewById(R.id.recyclerview_citymanagement_activity);
        RecyclerView.LayoutManager mLayoutManager=new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //initialize button_back button.
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        mAdapter=new RecyclerAdapter(mListData);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnMyItemClickListener(new RecyclerAdapter.OnMyItemClickListener() {
            @Override
            public void onDeleteButtonClick(View view, int position) {
                if(mListData.size()>1){
                    mListData.remove(position);
                    mAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(CityManagementActivity.this,"至少需要保留一个城市",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onListItemClick(View view) {
                if(button_edit_stateflag) {
                    mAdapter.isEditting=false;
                    mAdapter.notifyDataSetChanged();
                    button_edit_city.setBackgroundResource(R.drawable.ic_action_edit);
                    saveSelectedCounty();
                    button_edit_stateflag = false;
                }
            }

            @Override
            public void onListItemLongClick(View view) {
                mAdapter.isEditting=true;
                mAdapter.notifyDataSetChanged();
                button_edit_city.setBackgroundResource(R.drawable.ic_action_accept);
                button_edit_stateflag=true;
            }
        });

        //initialize button_edit_city button
        button_edit_stateflag=false;
        button_edit_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!button_edit_stateflag){
                    mAdapter.isEditting=true;
                    mAdapter.notifyDataSetChanged();
                    v.setBackgroundResource(R.drawable.ic_action_accept);
                    button_edit_stateflag=true;
                }else{
                    mAdapter.isEditting=false;
                    mAdapter.notifyDataSetChanged();
                    v.setBackgroundResource(R.drawable.ic_action_edit);
                    saveSelectedCounty();
                    button_edit_stateflag=false;
                }
            }
        });

        //initialize  button_add_city button
        button_add_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CityManagementActivity.this,ChooseCityActivity.class);
                startActivityForResult(intent,1);
            }
        });
    }

    private void saveSelectedCounty(){
        SharedPreferences.Editor mEditor=wnl_spf.edit();
        for(int i=0;i<mListData.size();i++){
            mEditor.putString("selectedCountyName"+i,mListData.get(i).getCityName());
            mEditor.putString("selectedCountyCode"+i,mListData.get(i).getCityCode());
            mEditor.apply();
        }
        mEditor.putString("selectedCountyCount",mListData.size()+"").apply();
    }

    private void initAdapterData(){
        //initialize RecyclerView's adapter.
        String tmp;
        mListData.clear();
        int i=Integer.parseInt(wnl_spf.getString("selectedCountyCount","0"));

        for(int j=0;j<i;j++){
            RecyclerAdapterDataModle dataModle=new RecyclerAdapterDataModle();
            tmp=wnl_spf.getString("selectedCountyName"+j,"");
            dataModle.setCityName(tmp);
            tmp=wnl_spf.getString("selectedCountyCode"+j,"");
            dataModle.setCityCode(tmp);

            SharedPreferences mSpf=getSharedPreferences(tmp,MODE_PRIVATE);
            tmp=mSpf.getString("now_cond_txt","晴间多云");
            dataModle.setWeatherText(tmp);
            dataModle.setWeatherCode(MainActivity.weatherCodeMap.get(tmp));
            tmp=mSpf.getString("df_tmp_min_day1","23")+"°~"
                    +mSpf.getString("df_tmp_max_day1","31")+"°";
            dataModle.setTmpRange(tmp);
            mListData.add(dataModle);
        }
    }

}
