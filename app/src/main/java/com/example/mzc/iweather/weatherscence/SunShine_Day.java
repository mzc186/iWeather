package com.example.mzc.iweather.weatherscence;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.example.mzc.iweather.R;


/**
 * Created by MZC on 8/5/2016.
 */
public class SunShine_Day extends FrameLayout  {
    private  ImageView birdup_imageview;
    private  ImageView birddown_imageview;
    private  ImageView cloud_imageview;
    private  ImageView sunshine_imageview;
    private  AnimationDrawable birdup_animdraw;
    private  AnimationDrawable birddown_animdraw;
    private  Animation birdup_tweenanim;
    private  Animation cloud_animation;
    private  Animation sunshine_animation;

    public SunShine_Day(Context context) {
        super(context);
        init(context);
    }

    public SunShine_Day(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SunShine_Day(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view=View.inflate(context,R.layout.sunshine_day,null);
        addView(view);
        birdup_imageview = (ImageView) view.findViewById(R.id.birdup_imageview);
        cloud_imageview = (ImageView) view.findViewById(R.id.cloud_imageview);
        sunshine_imageview = (ImageView) view.findViewById(R.id.sunshine_imageview);
        birddown_imageview = (ImageView) view.findViewById(R.id.birddown_imageview);

        birdup_animdraw = (AnimationDrawable) birdup_imageview.getDrawable();
        birdup_tweenanim = AnimationUtils.loadAnimation(context, R.anim.birdup_tweenanimation);
        birddown_animdraw = (AnimationDrawable) birddown_imageview.getDrawable();
        cloud_animation = AnimationUtils.loadAnimation(context, R.anim.cloud_animation);
        sunshine_animation = AnimationUtils.loadAnimation(context, R.anim.sunshine_animation);
        sunshine_animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                sunshine_imageview.clearAnimation();
                sunshine_imageview.startAnimation(sunshine_animation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        startAnim();
    }


    private void startAnim() {
        birdup_imageview.startAnimation(birdup_tweenanim);
        birdup_animdraw.start();
        birddown_imageview.startAnimation(birdup_tweenanim);
        birddown_animdraw.start();
        cloud_imageview.startAnimation(cloud_animation);
        sunshine_imageview.startAnimation(sunshine_animation);
    }


    //cancel the animations when this view is removed in case of leaks of memory
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        birddown_imageview.clearAnimation();
        birdup_imageview.clearAnimation();
        sunshine_imageview.clearAnimation();
        cloud_imageview.clearAnimation();
        Runtime.getRuntime().gc();
    }


}
