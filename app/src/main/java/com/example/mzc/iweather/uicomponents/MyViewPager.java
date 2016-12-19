package com.example.mzc.iweather.uicomponents;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by MZC on 10/8/2016.
 */
public class MyViewPager extends ViewPager{
    public MyViewPager(Context context){
        super(context);
    }
    public MyViewPager(Context context,AttributeSet attrs){
        super(context,attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }
}
