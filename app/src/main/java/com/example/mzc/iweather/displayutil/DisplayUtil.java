package com.example.mzc.iweather.displayutil;

import android.content.Context;

/**
 * Created by MZC on 8/21/2016.
 */
public class DisplayUtil {

    public static float px2dip(Context context,float pxValue){
        final float scale=context.getResources().getDisplayMetrics().density;
        return  pxValue/scale;
    }

    public static float dip2px(Context context,float dipValue){
        final float scale=context.getResources().getDisplayMetrics().density;
        return dipValue*scale;
    }

    public static float px2sp(Context context,float pxValue){
        final float fontScale=context.getResources().getDisplayMetrics().scaledDensity;
        return  pxValue/fontScale;
    }

    public static float sp2px(Context context,float spValue){
        final float fontScale=context.getResources().getDisplayMetrics().scaledDensity;
        return spValue*fontScale;
    }

    public static float pxScale(Context context){
        return context.getResources().getDisplayMetrics().density;
    }

    public static float fontScale(Context context){
        return context.getResources().getDisplayMetrics().scaledDensity;
    }
}
