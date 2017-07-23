package my.easycommunity.ui.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import de.greenrobot.event.EventBus;
import java.util.List;
import my.easycommunity.R;
import my.easycommunity.adapter.NewsFragmentAdapter;
import my.easycommunity.base.BaseFragment;
import my.easycommunity.base.BasePresenter;
import my.easycommunity.entity.news.Result;
import my.easycommunity.eventbus.WebViewEvent;
import my.easycommunity.ui.webview.WebActivity;

/**
 * Created by Administrator on 2017/7/16.
 */

public class NewsFragment extends BaseFragment<Result.ResultBean.DataBean> implements NewsView
{
    private String[] strType = new String[]{"top", "keji", "shehui", "guonei", "yule"};
    private NewsPresenterImpl newsPresenter;
    private LinearLayoutManager manager;
    private NewsFragmentAdapter adapter;
    private int type =0;

    public static NewsFragment newInstance(int type) {
        NewsFragment newsFragment = new NewsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        newsFragment.setArguments(bundle);
        return newsFragment;
    }

    @Override
    public int getContentRes() {return R.layout.news_fragment;}

    @Override
    public BasePresenter getPresenter() {
        newsPresenter =new NewsPresenterImpl(this.bindToLifecycle(),this,new NewsInteractorImpl());
        return newsPresenter;
    }

    @Override
    public void setUpView() {
        manager =new LinearLayoutManager(this.getContext());
        error_tv.setOnClickListener(this);
        if(adapter==null){
            adapter =new NewsFragmentAdapter();
        }
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void iniData()
    {
        adapter.setOnItemClickListener(new NewsFragmentAdapter.OnItemClickListener() {
            @Override
            public void getData(int position) {
                newsPresenter.itemOnclickLinster(position);
            }
        });
    }
    @Override
    public Object gettyp() {
        type =this.getArguments().getInt("type");
        return strType[type];
    }
    @Override
    public boolean adapterIsEmpty()
    {
        return adapter !=null && adapter.getItemCount()!=0;
    }
    @Override
    public void setData(List<Result.ResultBean.DataBean> list) {adapter.setData(list);}
    @Override
    public void onItemClickLinster(Result.ResultBean.DataBean newsBean) {
        EventBus.getDefault().postSticky(new WebViewEvent(newsBean.getUrl(),newsBean.getTitle()));
        startActivity(new Intent(getActivity(), WebActivity.class));
    }
}
