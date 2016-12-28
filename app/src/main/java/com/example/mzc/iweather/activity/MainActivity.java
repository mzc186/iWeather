package com.example.mzc.iweather.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.example.mzc.iweather.R;
import com.example.mzc.iweather.datautil.LunarCalendar;
import com.example.mzc.iweather.datautil.MyConstantValues;
import com.example.mzc.iweather.displayutil.DisplayUtil;
import com.example.mzc.iweather.uicomponents.MyFragmentStatePagerAdapter;
import com.example.mzc.iweather.uicomponents.MyViewPager;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends FragmentActivity implements ViewSwitcher.ViewFactory{
    public static HashMap<String,Integer> weatherCodeMap;
    public static HashMap<String,Integer> get_aql_color;
    /**
     * 提供一个数组给每一个fragment做标记，程序开启的时候，每一个fragment默认
     * 都需要刷新一次。
     */
    public String[] ffc_flag;
    private SharedPreferences wnl_spf;
    private String[] weekDayGroup;
    private MyFragmentStatePagerAdapter mfspa;

    private ImageSwitcher imageSwitcher;
    private DrawerLayout mDrawerWidget;
    private View mDrawerView;
    private View mContentView;
    private MyViewPager myViewPager;
    private TextView mTitleText;
    private Button button_settings;
    private boolean drawer_flag;
    private Random randomNum;
    private int[] backGroundRes;
    private int lastPageNum;
    private int nowPageNum;
    private LinearLayout ll_dot_group;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBasicData();
        initWidgets();
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
    public View makeView() {
        ImageView imageView=new ImageView(this);
        FrameLayout.LayoutParams lp=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(lp);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageResource(backGroundRes[randomNum.nextInt(18)]);
        return imageView;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case 1:
                if(resultCode==RESULT_OK) {
                    int i = Integer.parseInt(wnl_spf.getString("selectedCountyCount", "0"));
                    ffc_flag = new String[i];
                    for (int j = 0; j < i; j++) {
                        ffc_flag[j] = "false";
                    }
                    resetAdapterDataAndCurrentItem();
                }
                break;
            case 2:
                if(resultCode==RESULT_OK) {
                    resetAdapterDataAndCurrentItem();
                }
            default:
                break;
        }
    }

    private void resetAdapterDataAndCurrentItem(){
        mfspa.notifyDataSetChanged();
        initDotGroup();
        int itemCount=Integer.parseInt(wnl_spf.getString("selectedCountyCount", "0"));
        if(nowPageNum<itemCount){
            myViewPager.setCurrentItem(nowPageNum);
            mTitleText.setText(wnl_spf.getString("selectedCountyName"+nowPageNum,""));
            ll_dot_group.getChildAt(nowPageNum).setEnabled(true);
        }else {
            itemCount--;
            lastPageNum=nowPageNum=itemCount;
            myViewPager.setCurrentItem(itemCount);
            mTitleText.setText(wnl_spf.getString("selectedCountyName"+itemCount,""));
            ll_dot_group.getChildAt(itemCount).setEnabled(true);
        }
    }

    private void initWidgets(){
        imageSwitcher=(ImageSwitcher)findViewById(R.id.image_switcher);
        imageSwitcher.setFactory(this);
        AlphaAnimation inAnimation=new AlphaAnimation(0,1);
        inAnimation.setDuration(1000);
        AlphaAnimation outAnimation=new AlphaAnimation(1,0);
        inAnimation.setDuration(1000);
        imageSwitcher.setInAnimation(inAnimation);
        imageSwitcher.setOutAnimation(outAnimation);

        mDrawerWidget=(DrawerLayout)findViewById(R.id.drawer_widget);
        mDrawerWidget.setScrimColor(0x00ffffff);
        mDrawerView=findViewById(R.id.drawer_layout);
        mDrawerWidget.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                button_settings.setAlpha(1-slideOffset);
                mContentView.setTranslationX(mDrawerView.getWidth()*slideOffset);
            }
            @Override
            public void onDrawerOpened(View drawerView) {

            }
            @Override
            public void onDrawerClosed(View drawerView) {
                button_settings.setAlpha(1);
                if(drawer_flag){
                    Intent intent=new Intent(MainActivity.this,CityManagementActivity.class);
                    startActivityForResult(intent,1);
                    drawer_flag=false;
                }
            }
            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        mContentView=findViewById(R.id.content_view);
        myViewPager=(MyViewPager)findViewById(R.id.my_viewpager);
        mTitleText=(TextView)findViewById(R.id.county_name);
        mTitleText.setText(wnl_spf.getString("selectedCountyName0",""));
        button_settings=(Button)findViewById(R.id.button_settings);
        button_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerWidget.openDrawer(GravityCompat.START);
            }
        });
        View manageCityView=mDrawerView.findViewById(R.id.managecityview_indrawerlayout);
        manageCityView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer_flag=true;
                mDrawerWidget.closeDrawer(GravityCompat.START);
            }
        });
        View exitActivityView=mDrawerView.findViewById(R.id.exitview_indrawerlayout);
        exitActivityView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mfspa=new MyFragmentStatePagerAdapter(getSupportFragmentManager(),this);
        myViewPager.setAdapter(mfspa);
        myViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                String mCountyName = wnl_spf.getString("selectedCountyName" + position, "");
                mTitleText.setText(mCountyName);
                lastPageNum=nowPageNum;
                nowPageNum=position;
                View dotView=ll_dot_group.getChildAt(lastPageNum);
                dotView.setEnabled(false);
                dotView=ll_dot_group.getChildAt(position);
                dotView.setEnabled(true);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
                if(state==ViewPager.SCROLL_STATE_IDLE&&nowPageNum!=lastPageNum){
                    imageSwitcher.setImageResource(backGroundRes[randomNum.nextInt(18)]);
                    if(nowPageNum==0||nowPageNum==mfspa.getCount()-1){
                        lastPageNum=nowPageNum;
                    }
                }
            }
        }
        );

        ll_dot_group=(LinearLayout)findViewById(R.id.ll_dot_group);
        initDotGroup();
        if(!wnl_spf.getString("selectedCountyCount", "0").equals("0")){
            ll_dot_group.getChildAt(0).setEnabled(true);
        }
    }

    
    //initialize independent basic data
    private void initBasicData(){
        //initialize the weekDayGroup
        get_aql_color=MyConstantValues.getAqlColorMap();
        weatherCodeMap=MyConstantValues.getWeatherCodeMap();
        backGroundRes=MyConstantValues.getBackGroundRes();
        randomNum=new Random(System.currentTimeMillis());
        weekDayGroup=new String[]{"周一","周二","周三","周四","周五","周六","周日"};
        wnl_spf=getSharedPreferences("WanNianLi",MODE_PRIVATE);
        initDate();
        int i=Integer.parseInt(wnl_spf.getString("selectedCountyCount","0"));
        if(i>0){
            ffc_flag=new String[i];
            for(int j=0;j<i;j++){
                ffc_flag[j]="true";
            }
        }else{
            ffc_flag=new String[]{"false"};
            Intent intent=new Intent(this,ChooseCityActivity.class);
            startActivityForResult(intent,2);
        }
    }


    //initialize date 
    public void initDate(){
        Date dt = new Date();
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.CHINA);
        String tmp=df.format(dt);
        String[] tmpArray=tmp.split("年");
        int solarYear=Integer.parseInt(tmpArray[0]);
        tmp=tmpArray[0]+"-";
        tmpArray=tmpArray[1].split("月");
        int solarMonth=Integer.parseInt(tmpArray[0]);
        tmp=tmp+tmpArray[0]+"-";
        tmpArray=tmpArray[1].split("日");
        int solarDay=Integer.parseInt(tmpArray[0]);
        tmp=tmp+tmpArray[0];
        String date_flag=wnl_spf.getString("date_flag","");
        //to ensure the call of method dateTransformation() no more than
        //once a day.
        if(!date_flag.equals(tmp)){
            date_flag=tmp;
            SharedPreferences.Editor wnl_editor=wnl_spf.edit();
            wnl_editor.putString("date_flag",date_flag);
            String nongLi= LunarCalendar.solarToLunar(solarYear,solarMonth,solarDay);
            wnl_editor.putString("nongLi",nongLi);
            df=DateFormat.getDateInstance(DateFormat.FULL, Locale.CHINA);
            tmp=df.format(dt);
            tmpArray=tmp.split("星期");
            wnl_editor.putString("weekDay","周"+tmpArray[1]);
            wnl_editor.apply();
            weekDayTransformation();
        }
    }


    private void weekDayTransformation(){
        String wkd=wnl_spf.getString("weekDay","");
        int wkd_flag;
        for(int i=0;i<7;i++){
            if(wkd.equals(weekDayGroup[i])){
                wkd_flag=i;
                for(int j=0;j<7;j++){
                    if(wkd_flag>=7){
                        wkd_flag=0;
                    }
                    wkd=weekDayGroup[wkd_flag];
                    j++;
                    wnl_spf.edit().putString("df_weekDay_day"+j,wkd).apply();
                    j--;
                    wkd_flag++;
                }
                break;
            }
        }
    }

    private void initDotGroup(){
        ll_dot_group.removeAllViews();
        String tmp=wnl_spf.getString("selectedCountyCount", "0");
        int cityCount=Integer.parseInt(tmp);
        float pxScale= DisplayUtil.pxScale(this);
        int lp_wh=(int)(pxScale*6);
        for(int i=0;i<cityCount;i++){
            View dotView=new View(getApplicationContext());
            dotView.setBackgroundResource(R.drawable.dot_selector);
            dotView.setEnabled(false);
            LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(lp_wh,lp_wh);
            if(i!=0){
                lp.leftMargin=lp_wh;
            }
            dotView.setLayoutParams(lp);
            ll_dot_group.addView(dotView);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}