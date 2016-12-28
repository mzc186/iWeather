package com.example.mzc.iweather.uicomponents;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.mzc.iweather.activity.MainActivity;

import java.util.ArrayDeque;
import java.util.Queue;


/**
 * Created by MZC on 10/9/2016.
 */
public class MyFragmentStatePagerAdapter extends CustomPagerAdapterInterface {
    private MainActivity mActivity;
    private SharedPreferences mSpf;
    /**提供一个fragment的缓存队列，因为FragmentStatePagerAdapter虽然默认最多只保留3个fragment,
      但其实每次切换页面都会销毁1个fragment和重新创建另一个fragment,但是我们这边还可以再优化一下，
      因为我们各个fragment的UI都是一样的，因此在destroyItem方法中
     我们调用super.destroyItem方法只是解除和相应fragmentManager的关联，仍把这个fragment的引用
     添加回我们的缓存队列，达到复用的目的，从而使页面切换更流畅。fragment个数大于等于4的时候，
     缓存个数就要是4个，至于为什么缓存个数为什么要4个，这是
     通过实验得出来的，如果缓存个数只为3个，会出现一些很难解决的问题。
     */
    private Queue<MyObjectFragment> fragmentQueue;

    public MyFragmentStatePagerAdapter(FragmentManager fm, MainActivity activity){
        super(fm);
        mActivity=activity;
        mSpf=mActivity.getSharedPreferences("WanNianLi",Context.MODE_PRIVATE);
        fragmentQueue=new ArrayDeque<>();
        for(int i=0;i<4;i++){
            MyObjectFragment mof=new MyObjectFragment();
            fragmentQueue.add(mof);
        }

    }


    @Override
    public Fragment getItem(int position) {
        MyObjectFragment mof;
        mof=fragmentQueue.poll();
        mof.initFragment(mActivity,position);
        return mof;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
            //for reason of better performance, save the fragment that is to be
            //destroyed, and reuse it in getItem() method since the fragment here
            //is reusable. In addition, remember to judge if the view to be returned
            //is null in method onCreateView() of MyObjectFragment, because every time
            //when getItem() method is called, the lifecycle of fragment returned by
            //getItem() will start over again in this case.
        if(fragmentQueue!=null){
            MyObjectFragment fg=(MyObjectFragment)object;
            fragmentQueue.add(fg);
        }
        super.destroyItem(container,position,object);
    }

    @Override
    public int getCount() {
        String tmpStr=mSpf.getString("selectedCountyCount","0");
        return Integer.parseInt(tmpStr);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
