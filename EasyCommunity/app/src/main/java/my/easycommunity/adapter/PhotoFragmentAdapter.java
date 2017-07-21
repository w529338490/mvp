package my.easycommunity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;


import butterknife.ButterKnife;
import butterknife.InjectView;
import my.easycommunity.R;
import my.easycommunity.common.MyApplication;
import my.easycommunity.entity.news.Result;
import my.easycommunity.entity.photo.GankPhoto;

/**
 * Created by Administrator on 2017/7/21.
 */

public class PhotoFragmentAdapter extends RecyclerView.Adapter<PhotoFragmentAdapter.MyHolder>
{

    Context context;
    LayoutInflater inflaters;
    List<String> list;
    List<GankPhoto.ResultsBean> data;

    public PhotoFragmentAdapter()
    {
        this.context = MyApplication.context;
        inflaters = LayoutInflater.from(context);
    }

    public void setData( List<GankPhoto.ResultsBean>data)
    {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        MyHolder holder = null;
        if(holder == null){
            holder = new PhotoFragmentAdapter
                    .MyHolder(inflaters.inflate(R.layout.photo_adapter, parent, false));
        }

        return holder;

    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position)
    {
        Glide.with(context)
                .load(data.get(position).getUrl())
                .override(150,150)
                .centerCrop()
                .into(holder.img);
    }

    @Override
    public int getItemCount()
    {
        return data == null?0:data.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {

        @InjectView(R.id.img)
        ImageView img;
        public MyHolder(View itemView)
        {
            super(itemView);
            ButterKnife.inject(this,itemView);

        }
    }
}
