package my.easycommunity.ui.news;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.trello.rxlifecycle.components.support.RxFragment;
import de.greenrobot.event.EventBus;
import java.util.List;
import my.easycommunity.R;
import my.easycommunity.adapter.NewsFragmentAdapter;
import my.easycommunity.entity.news.Result;
import my.easycommunity.eventbus.WebViewEvent;
import my.easycommunity.ui.webview.WebActivity;
import my.easycommunity.utill.NetWorkUtil;
import my.easycommunity.utill.ProssBarUtil;
import my.easycommunity.utill.ToastUtil;

/**
 * Created by Administrator on 2017/7/16.
 */

public class NewsFragment extends RxFragment implements NewsView,View.OnClickListener
{
    @InjectView(R.id.fresh) public TwinklingRefreshLayout twinklingRefreshLayout;
    @InjectView(R.id.news_rcv) RecyclerView recyclerView;
    @InjectView(R.id.progress) FrameLayout progress;
    @InjectView(R.id.error_tv)TextView error_tv;

    private LinearLayoutManager  manager;
    private  NewsPresenter newsPresenter;
    private NewsFragmentAdapter adapter;
    ProgressLayout header ;
    private String[] strType = new String[]{"top", "keji", "shehui", "guonei", "yule"};
    private int type = 0;
    private View view;
    public static NewsFragment newInstance(int type,AppBarLayout appBarLayout)
    {
        NewsFragment newsFragment = new NewsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        newsFragment.setArguments(bundle);
        return newsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if(view ==null){
            view = inflater.inflate(R.layout.news_fragment, container, false);
            ButterKnife.inject(this,view);
        }
        setUpView();
        setUpData();
        return view;

    }
    protected void setUpView()
    {
        newsPresenter =new NewsPresenterImpl(this.bindToLifecycle(),this);
        type =this.getArguments().getInt("type");
        newsPresenter.start();
        addNetData();
        manager =new LinearLayoutManager(this.getContext());
        error_tv.setOnClickListener(this);
    }
    protected void setUpData()
    {
        header = new ProgressLayout(NewsFragment.this.getContext());
        twinklingRefreshLayout.setEnableOverScroll(false);
        twinklingRefreshLayout.setEnableRefresh(true);

        if(adapter==null){
            adapter =new NewsFragmentAdapter();
        }
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        twinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        addNetData();
                        refreshLayout.finishRefreshing();
                    }
                }, 2000);
            }
            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishLoadmore();
                    }
                }, 2000);
            }
        });
        adapter.setOnItemClickListener(new NewsFragmentAdapter.OnItemClickListener()
        {
            @Override
            public void getData(int position)
            {
                newsPresenter.itemOnclickLinster(position);
            }
        });
    }
    @Override
    public void showProgress()
    {
        ProssBarUtil.showBar(progress);
    }
    @Override
    public void hideProgress()
    {
        ProssBarUtil.hideBar(progress);
        ProssBarUtil.hideBar(error_tv);
    }
    @Override
    public void setData(List<Result.ResultBean.DataBean> list)
    {
        adapter.setData(list);
    }

    @Override
    public void showError() {
        ProssBarUtil.hideBar(progress);
        ProssBarUtil.showBar(error_tv);
    }

    @Override
    public void addMore()
    {

    }

    @Override
    public void onItemClickLinster(Result.ResultBean.DataBean newsBean)
    {
        EventBus.getDefault().postSticky(new WebViewEvent(newsBean.getUrl(),newsBean.getTitle()));
        startActivity(new Intent(NewsFragment.this.getActivity(), WebActivity.class));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.error_tv:
                     addNetData();
                break;
        }
    }
    //加载前，先判断网络

    public  void addNetData(){
        if ( getContext()!=null && NetWorkUtil.networkCanUse(getContext())) {
            ProssBarUtil.showBar(progress);
            ProssBarUtil.hideBar(error_tv);
            newsPresenter.getDate(strType[type]);
        } else {
            ProssBarUtil.hideBar(progress);
            ProssBarUtil.showBar(error_tv);
            if( adapter !=null && adapter.getItemCount()!=0){
                ProssBarUtil.hideBar(error_tv);
            }

            ToastUtil.show("请检测你的网络！");
        }
    }

    //离开界面时候，停止数据加载
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser && isAdded())
        {
            newsPresenter.stopNetWork();
        }
    }
}
