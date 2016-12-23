package com.example.mzc.iweather.uicomponents;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.example.mzc.iweather.displayutil.DisplayUtil;

import java.util.ArrayList;

/**
 * Created by MZC on 8/9/2016.
 */
public class TemperatureTrendView extends View {
    private int viewHeight;
    private int viewWidth;
    private int minTemp;
    private int maxTemp;
    private float mSpace;
    
    int[] dx7=new int[7];
    int[] dx8=new int[8];
    int[] tmpArray;

    private ArrayList<Integer> mTopTemp;
    private ArrayList<Integer> mLowTemp;

    private Paint mPoint1Paint;
    private Paint mPoint2Paint;
    private Paint mLinePaint;
    private Paint mTextPaint;
    private float pxScale;
    private float fontScale;
    public TemperatureTrendView(Context context){
        super(context);
        init(context);
    }

    public TemperatureTrendView(Context context, AttributeSet attrs){
        super(context,attrs);
        init(context);
    }

    public TemperatureTrendView(Context context, AttributeSet attr, int defStyleAttr){
        super(context,attr,defStyleAttr);
        init(context);
    }


    private void init(Context context){
        mTopTemp=new ArrayList<>();
        mTopTemp.add(12);
        mTopTemp.add(15);
        mTopTemp.add(11);
        mTopTemp.add(13);
        mTopTemp.add(19);
        mTopTemp.add(21);
        mTopTemp.add(16);
        mLowTemp=new ArrayList<>();
        mLowTemp.add(5);
        mLowTemp.add(6);
        mLowTemp.add(3);
        mLowTemp.add(8);
        mLowTemp.add(11);
        mLowTemp.add(9);
        mLowTemp.add(8);

        pxScale= DisplayUtil.pxScale(context);
        fontScale= DisplayUtil.fontScale(context);

        mPoint1Paint=new Paint();
        mPoint1Paint.setAntiAlias(true);
        mPoint1Paint.setColor(Color.argb(255,205,85,0));

        mPoint2Paint=new Paint();
        mPoint2Paint.setAntiAlias(true);
        mPoint2Paint.setColor(Color.argb(255,64,95,237));

        mLinePaint=new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(5*pxScale/3);
        mLinePaint.setColor(Color.argb(255,239,239,239));

        mTextPaint=new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.argb(255,255,255,255));
        mTextPaint.setTextSize(40*fontScale/3);
        mTextPaint.setTypeface(Typeface.create("",Typeface.NORMAL));
        mTextPaint.setTextAlign(Paint.Align.CENTER);

    }

    private void spaceHeightWidth(){
        minTemp= getMinTemperature(mLowTemp);
        maxTemp= getMaxTemperature(mTopTemp);
        int h= maxTemp-minTemp;
        mSpace=4*(viewHeight-60*pxScale/3)/(5*h);
        
        if(mLowTemp.size()==7){
            dx7[0]=viewWidth/14;
            dx7[1]=viewWidth*3/14;
            dx7[2]=viewWidth*5/14;
            dx7[3]=viewWidth*7/14;
            dx7[4]=viewWidth*9/14;
            dx7[5]=viewWidth*11/14;
            dx7[6]=viewWidth*13/14;

            tmpArray=dx7;
        }
        else if(mLowTemp.size()==8) {
            dx8[0] = viewWidth / 16;
            dx8[1] = viewWidth * 3 / 16;
            dx8[2] = viewWidth * 5 / 16;
            dx8[3] = viewWidth * 7 / 16;
            dx8[4] = viewWidth * 9 / 16;
            dx8[5] = viewWidth * 11 / 16;
            dx8[6] = viewWidth * 13 / 16;
            dx8[7] = viewWidth * 15 / 16;

            tmpArray=dx8;
        }
        
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        viewWidth=getWidth();
        viewHeight=getHeight();
        spaceHeightWidth();

        Paint.FontMetrics fontMetrics=mTextPaint.getFontMetrics();
        float fontHeight=fontMetrics.bottom-fontMetrics.top;

        for(int i=0;i<mTopTemp.size();i++){
            float _hTop=(maxTemp-mTopTemp.get(i))*mSpace + 65*pxScale/3;
            if(i<mTopTemp.size()-1){
                canvas.drawLine(tmpArray[i],_hTop,tmpArray[i+1],(maxTemp-mTopTemp.get(i+1))*mSpace+65*pxScale/3,mLinePaint);
            }

            canvas.drawText(mTopTemp.get(i)+"°",tmpArray[i],_hTop-3*fontHeight/8,mTextPaint);
            canvas.drawCircle(tmpArray[i],_hTop,10*pxScale/3,mPoint1Paint);
        }

        for(int i=0;i<mLowTemp.size();i++){
            float _hLow=(maxTemp-mLowTemp.get(i))*mSpace + 65*pxScale/3;
            if(i<mTopTemp.size()-1){
                canvas.drawLine(tmpArray[i],_hLow,tmpArray[i+1],(maxTemp-mLowTemp.get(i+1))*mSpace+65*pxScale/3,mLinePaint);
            }

            canvas.drawText(mLowTemp.get(i)+"°",tmpArray[i],_hLow+fontHeight,mTextPaint);
            canvas.drawCircle(tmpArray[i],_hLow,10*pxScale/3,mPoint2Paint);
        }
    }


    public  void clearData(){
        mTopTemp.clear();
        mLowTemp.clear();
    }

    public void addTopTemp(String tmp){
        int tt=Integer.parseInt(tmp);
        mTopTemp.add(tt);
    }

    public void addLowTemp(String tmp){
        int lt=Integer.parseInt(tmp);
        mLowTemp.add(lt);
    }

    public void reDraw(){
        invalidate();
    }

    private int getMaxTemperature(ArrayList<Integer> topTemp) {
        int max=topTemp.get(0);
        for(int i=0;i<topTemp.size()-1;i++){
                if(max<topTemp.get(i+1)){
                    max=topTemp.get(i+1);
                }
        }
        return max;
    }

    private int getMinTemperature(ArrayList<Integer> lowTemp){
        int min=lowTemp.get(0);
        for(int i=0;i<lowTemp.size()-1;i++){
            if(min>lowTemp.get(i+1)){
                min=lowTemp.get(i+1);
            }
        }
        return min;
    }
}
