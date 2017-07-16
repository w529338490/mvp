package my.easycommunity.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.ButterKnife;
import my.easycommunity.common.MyApplication;

/**
 * Created by Administrator on 2017/7/15.
 */

public abstract class BaseAdapter extends RecyclerView.Adapter<BaseAdapter.Holder>
{

    List<Object> results;
    Context context;
    LayoutInflater inflater;
    private    View contentView;


    public BaseAdapter()
    {
        this.results = setData();
        this.context= MyApplication.context;
        this.inflater=LayoutInflater.from(context);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if(contentView == null){
            contentView = inflater.inflate(setLayoutResourceID(),parent,false);
        }

        ButterKnife.inject(contentView);

        return new Holder(contentView);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position)
    {

    }

    @Override
    public int getItemCount()
    {
        return results.size();
    }

    public class Holder extends RecyclerView.ViewHolder
    {
        public Holder(View itemView)
        {
            super(itemView);

        }
    }

    public  abstract  int setLayoutResourceID();

    public  abstract List<Object> setData();
}
