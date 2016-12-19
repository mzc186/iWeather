package com.example.mzc.iweather.uicomponents;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mzc.iweather.R;
import com.example.mzc.iweather.model.RecyclerAdapterDataModle;

import java.util.List;

/**
 * Created by MZC on 10/19/2016.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<RecyclerAdapterDataModle> mData;
    private OnMyItemClickListener mListener;
    public boolean isEditting;


    public RecyclerAdapter(List<RecyclerAdapterDataModle> data){
        mData=data;

    }

    public interface OnMyItemClickListener {
        void onDeleteButtonClick(View view, int position);
        void onListItemClick(View view);
        void onListItemLongClick(View view);
    }

    public void setOnMyItemClickListener(OnMyItemClickListener mListener){
        this.mListener=mListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener,View.OnClickListener{

        private View view;
         ViewHolder(View itemView){
            super(itemView);
            view=itemView;
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            Button mButton=(Button)view.findViewById(R.id.button_delete_city);
            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onDeleteButtonClick(v,getAdapterPosition());
                }
            });
        }

        @Override
        public void onClick(View v) {
            mListener.onListItemClick(v);
        }

        @Override
        public boolean onLongClick(View v) {
                mListener.onListItemLongClick(v);
            return true;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_listitem_1,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TextView mCityName=(TextView)holder.view.findViewById(R.id.listitem_city_name);
        ImageView mWeatherImage=(ImageView)holder.view.findViewById(R.id.listitem_weather_image);
        TextView mTmpRange=(TextView)holder.view.findViewById(R.id.listitem_tmp_range);
        Button mButton=(Button)holder.view.findViewById(R.id.button_delete_city);
        if(isEditting){
            mButton.setVisibility(View.VISIBLE);
        }else{
            mButton.setVisibility(View.GONE);
        }
        mCityName.setText(mData.get(position).getCityName());
        mWeatherImage.setImageResource(mData.get(position).getWeatherCode());
        mTmpRange.setText(mData.get(position).getTmpRange());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}
