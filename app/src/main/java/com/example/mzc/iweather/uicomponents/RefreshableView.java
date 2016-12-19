package com.example.mzc.iweather.uicomponents;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.mzc.iweather.R;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by MZC on 10/1/2016.
 */
public class RefreshableView extends LinearLayout implements View.OnTouchListener{
    /**
     * pulling status
     */
    public static final int STATUS_PULL_TO_REFRESH=0;
    /**
     * status of releasing to refresh
     */
    public static final int STATUS_RELEASE_TO_REFRESH=1;
    /**
     * refreshing status
     */
    public static final int STATUS_REFRESHING=2;
    /**
     * status of refresh finished
     */
    public static final int STATUS_REFRESH_FINISHED=3;
    /**
     * the speed of rolling back of pull-header
     */
    public static  int SCROLL_SPEED;
    /**
     * string of last updated time, used to be the key of
     * SharedPreference
     */
    private static final String LAST_UPDATED_AT="last_updated_at";
    /**
     * the interface of pull-header
     */
    private PullToRefreshListener mListener;
    /**
     * preference for storage of last updated time
     */
    private SharedPreferences mSpf;
    /**
     * the header view of pull-header
     */
    private View header;
    /**
     * scrollview that needs to be pulled to refresh
     */
    private ScrollView mScrollView;
    /**
     * the ProgressBar to be showed when refreshing
     */
    private ProgressBar mProgressBar;
    /**
     * the arrow indicating pulling and releasing
     */
    private ImageView arrow;
    /**
     * the description text indicating pulling and releasing
     */
    private TextView description;
    /**
     * text description of last updated time
     */
    private TextView updateAt;
    /**
     * indication image of update status
     */
    private ImageView updateIndicationImage;
    /**
     * the LayoutParams of pulled-header
     */
    private MarginLayoutParams headerLayoutParams;
    /**
     *the id to distinguish last refreshed time
     * under different UI in case of collision
     */
    private String  mCountyCode;
    /**
     * the height of pull-header
     */
    private int hideHeaderHeight;

    /**
     * the current status with STATUS_PULL_TO_REFRESH and
     * STATUS_RELEASE_TO_REFRESH, STATUS_REFRESHING and
     * STATUS_REFRESH_FINISHED
     */
    private int currentStatus=STATUS_REFRESH_FINISHED;
    /**
     * record last status in case repeated actions
     */
    private int lastStatus=currentStatus;
    /**
     * the yCooridate of screen when finger presses down
     */
    private float yDown;
    /**
     * the max distance able to move of user's finger
     * before action judged as scrolling
     */
    private int touchSlop;
    /**
     * flag of loading layout,the initializement
     * of onLayout needs to be loaded only once.
     */
    private boolean loadOnce;
    /**
     * flag indicating whether if is able to pull,
     * it is only allowed to pull when scrollview scrolls
     * to the top
     */
    private boolean ableToPull;


    /**
     * the constructor method of pull-header widget,
     * it will add a header layout at runtime dynamicly.
     */
    public RefreshableView(Context context){
        super(context);
        init(context);
    }
    public RefreshableView(Context context, AttributeSet attrs){
        super(context,attrs);
        init(context);
    }

    public RefreshableView(Context context,AttributeSet attrs,int defStyleAttr){
        super(context,attrs,defStyleAttr);
        init(context);
    }

    /**
     * initialize the widget
     */
    private void init(Context context){
        header= LayoutInflater.from(context).inflate(R.layout.pull_to_refresh,null,false);
        mProgressBar=(ProgressBar)header.findViewById(R.id.progress_bar);
        arrow=(ImageView)header.findViewById(R.id.arrow);
        description=(TextView)header.findViewById(R.id.description);
        updateAt=(TextView)header.findViewById(R.id.updated_at);
        updateIndicationImage=(ImageView)header.findViewById(R.id.update_indication_image);
        touchSlop= ViewConfiguration.get(context).getScaledTouchSlop();
        setOrientation(VERTICAL);
        addView(header,0);
    }

    /**
     * to do some key initializement actions,such as
     * offset the pull-header to be hidden, set listener for
     * scrollview
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(changed&&!loadOnce){
            hideHeaderHeight=-header.getHeight();
            headerLayoutParams=(MarginLayoutParams) header.getLayoutParams();
            headerLayoutParams.topMargin=hideHeaderHeight;
            header.setLayoutParams(headerLayoutParams);
            mScrollView=(ScrollView)getChildAt(1);
            mScrollView.setOnTouchListener(this);
            loadOnce=true;
        }
    }

    /**
     * This is called when scrollview is touched. It contains different kinds of
     * logic dealing with pull to refresh.
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        setIsAbleToPull(event);
        if(ableToPull){
            switch(event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    yDown=event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float yMove=event.getRawY();
                    int distance=(int)(yMove-yDown);
                    //if the view is scrolling down and the pull header is hidden,
                    //block the pull down event.
                    if(distance<=0&&headerLayoutParams.topMargin<=hideHeaderHeight)
                    {
                        return false;
                    }
                    if(distance<=touchSlop){
                        return false;
                    }
                    if(currentStatus!=STATUS_REFRESHING){
                        if(headerLayoutParams.topMargin>0){
                            currentStatus=STATUS_RELEASE_TO_REFRESH;
                        }
                        else{
                            currentStatus=STATUS_PULL_TO_REFRESH;
                        }
                        //offset the topMargin of the pull-header to
                        //realize the effect of pulling
                        headerLayoutParams.topMargin=(distance/2)+hideHeaderHeight;
                        header.setLayoutParams(headerLayoutParams);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                default:
                    if(currentStatus==STATUS_RELEASE_TO_REFRESH){
                        //if the status is STATUS_RELEASE_TO_REFRESH when release finger,
                        //invoke the RefreshingTask().execute() method to refresh.
                        new RefreshingTask().execute();
                    }
                    else if(currentStatus==STATUS_PULL_TO_REFRESH){
                        //if the status is STATUS_PULL_TO_REFRESH when release finger,
                        //invoke the HideHeaderTask().execute() method to hide header.
                        new HideHeaderTask().execute();
                    }
                    break;
            }
            //remember to refresh the data of pull-to-refresh header
            if(currentStatus==STATUS_PULL_TO_REFRESH||currentStatus==STATUS_RELEASE_TO_REFRESH){
                updateHeaderView();
                //it is now in the status of STATUS_PULL_TO_REFRESH or STATUS_RELEASE_TO_REFRESH,
                //make the scrollview being not focusable,or the scrollview will be selected all
                //the time.
                lastStatus=currentStatus;
                //it is now in the status of STATUS_PULL_TO_REFRESH or STATUS_RELEASE_TO_REFRESH,
                //return true to block the event of scrollment of scrollveiw.
                return true;
            }
        }
        return false;
    }

    /**
     * 给下拉刷新控件注册一个监听器
     * @param listener  监听器的实现
     */
    public void setOnRefreshListener(PullToRefreshListener listener){
        this.mListener=listener;
    }

    /**
     * 当所有的刷新逻辑完成以后，记着调用一下
     */
    public void finishRefreshing(){
        mProgressBar.setVisibility(View.INVISIBLE);
        if(mSpf.getString("update_flag","false").equals("true")) {
            Date dt = new Date();
            DateFormat df = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.CHINA);
            mSpf.edit().putString(LAST_UPDATED_AT, df.format(dt)).apply();
            description.setText("数据更新成功");
            updateIndicationImage.setImageResource(R.drawable.update_success);
            updateIndicationImage.setVisibility(View.VISIBLE);
            refreshUpdatedAtValue();
        }
        else{
            description.setText("数据更新失败");
            updateIndicationImage.setImageResource(R.drawable.update_fail);
            updateIndicationImage.setVisibility(View.VISIBLE);
        }
        new HideHeaderTask().execute();
    }

    /**
     * 根据当前scrollview的滚动状态来设定{@link #ableToPull}
     * 的值，该方法每次都需要在onTouch中的第一个执行，这样可以判断出当前应该是
     * 滚动scrollview还是进行下拉
     *
     * @param event
     */
    private void setIsAbleToPull(MotionEvent event){
        if(mScrollView.getScrollY()<=0){
            if(!ableToPull){
                yDown=event.getRawY();
            }
            //如果content的上边缘距离父布局值为0，就说明scrollview的content
            //滚动到了最顶部，此时应该允许下拉刷新.
            ableToPull=true;
        }else {
            if(headerLayoutParams.topMargin!=hideHeaderHeight){
                headerLayoutParams.topMargin=hideHeaderHeight;
                header.setLayoutParams(headerLayoutParams);
            }
            ableToPull=false;
        }
    }

    /**
     * 更新下拉头中的信息
     */
    private void updateHeaderView(){
        if(lastStatus!=currentStatus){
            if(currentStatus==STATUS_PULL_TO_REFRESH){
                if(lastStatus==STATUS_REFRESH_FINISHED) {
                    refreshUpdatedAtValue();
                }
                description.setText(getResources().getString(R.string.pull_to_refresh));
                arrow.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
                rotateArrow();
            } else if(currentStatus==STATUS_RELEASE_TO_REFRESH){
                description.setText(getResources().getString(R.string.release_to_refresh));
                arrow.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
                rotateArrow();
            }else if(currentStatus==STATUS_REFRESHING){
                description.setText(getResources().getString(R.string.refreshing));
                mProgressBar.setVisibility(View.VISIBLE);
                arrow.clearAnimation();
                arrow.setVisibility(View.GONE);
            }

        }
    }

    /**
     * 根据当前的状态来旋转箭头
     */
    private void rotateArrow(){
        float pivotX=arrow.getWidth()/2f;
        float pivotY=arrow.getHeight()/2f;
        float fromDegrees=0f;
        float toDegrees=0f;
        if(currentStatus==STATUS_PULL_TO_REFRESH){
            fromDegrees=180f;
            toDegrees=360f;
        }else if(currentStatus==STATUS_RELEASE_TO_REFRESH){
            fromDegrees=0f;
            toDegrees=180f;
        }
        RotateAnimation animation=new RotateAnimation(fromDegrees,toDegrees,pivotX,pivotY);
        animation.setDuration(200);
        animation.setFillAfter(true);
        arrow.startAnimation(animation);

    }

    /**
     * 刷新下拉头中上次更新时间的文字描述.
     */
    private void refreshUpdatedAtValue(){
        String tmp=mSpf.getString(LAST_UPDATED_AT,"");
        if("".equals(tmp)){
            updateAt.setText("暂未更新过");
        }
        else{
            tmp="上次更新于："+tmp;
            updateAt.setText(tmp);
        }

    }

    /**
     * 正在刷新的任务，在此任务中会去回调注册进来的下拉刷新监听器
     */
    class RefreshingTask extends AsyncTask<Void,Integer,Void>{
        @Override
        protected Void doInBackground(Void... params) {
            currentStatus=STATUS_REFRESHING;
            int topMargin=headerLayoutParams.topMargin;
            SCROLL_SPEED=-topMargin/3;
            while(true){
                SCROLL_SPEED+=-5;
                topMargin=topMargin+SCROLL_SPEED;
                if(topMargin<=0){
                    break;
                }
                publishProgress(topMargin);
                sleep(1);
            }
            publishProgress(0);
            if(mListener!=null){
                mListener.onRefresh();
                sleep(500);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            updateHeaderView();
            headerLayoutParams.topMargin=values[0];
            header.setLayoutParams(headerLayoutParams);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            finishRefreshing();
        }
    }

    /**
     * 隐藏下拉头的任务，当未进行下拉刷新或下拉刷新完成后，此任务将会使下拉头
     * 重新隐藏
     */
    class HideHeaderTask extends AsyncTask<Void,Integer,Integer>{
        @Override
        protected Integer doInBackground(Void... params) {
            if(currentStatus!=STATUS_PULL_TO_REFRESH){
                sleep(500);
            }
            int topMargin=headerLayoutParams.topMargin;
            SCROLL_SPEED=-40;
            while(true){
                SCROLL_SPEED+=-5;
                topMargin=topMargin+SCROLL_SPEED;
                if(topMargin<=hideHeaderHeight){
                    topMargin=hideHeaderHeight;
                    break;
                }
                publishProgress(topMargin);
                sleep(1);
            }
            return topMargin;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            headerLayoutParams.topMargin=values[0];
            header.setLayoutParams(headerLayoutParams);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            headerLayoutParams.topMargin=integer;
            header.setLayoutParams(headerLayoutParams);
            updateIndicationImage.setVisibility(View.GONE);
            lastStatus=currentStatus=STATUS_REFRESH_FINISHED;
        }
    }

    /**
     * 使当前线程睡眠制定的毫秒数
     * @param time
     * 指定当前线程睡眠多久，以毫秒为单位
     */
    private void sleep(int time){
        try{
            Thread.sleep(time);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    /**
     * 下拉刷新的监听器，使用下拉刷新的地方应该注册此监听器来获取刷新回调
     */
    public interface PullToRefreshListener{
        void onRefresh();
    }

    /**
     * 更新与该refreshableView对应的mCountyCode,SharedPreference
     */
    public void setData(String countyCode){
        mCountyCode=countyCode;
        mSpf=null;
        mSpf=getContext().getSharedPreferences(mCountyCode,Context.MODE_PRIVATE);
    }
}
