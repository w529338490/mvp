package my.easycommunity.ui.news;

import android.content.DialogInterface;
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
import android.widget.ProgressBar;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.components.support.RxFragment;

import java.util.List;
import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import my.easycommunity.MainActivity;
import my.easycommunity.R;
import my.easycommunity.adapter.NewsFragmentAdapter;
import my.easycommunity.entity.news.Result;
import my.easycommunity.eventbus.WebViewEvent;
import my.easycommunity.ui.webview.WebActivity;
import my.easycommunity.utill.ToastUtil;


import static my.easycommunity.R.id.appBarLayout;

/**
 * Created by Administrator on 2017/7/16.
 */

public class NewsFragment extends RxFragment implements NewsView,DialogInterface.OnClickListener
{
    @InjectView(R.id.fresh)
   public TwinklingRefreshLayout twinklingRefreshLayout;

    @InjectView(R.id.news_rcv)
    RecyclerView recyclerView;

    @InjectView(R.id.progress)
    FrameLayout progress;
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
        view = inflater.inflate(R.layout.news_fragment, container, false);
        ButterKnife.inject(this,view);
        setUpView();
        setUpData();
        return view;

    }
    protected void setUpData()
    {

        header = new ProgressLayout(NewsFragment.this.getContext());
        twinklingRefreshLayout.setEnableOverScroll(false);
        twinklingRefreshLayout.setEnableRefresh(true);
        twinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        newsPresenter.getDate(strType[type]);
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
    }

    protected void setUpView()
    {
        type =this.getArguments().getInt("type");
        newsPresenter =new NewsPresenterImpl(this.bindToLifecycle(),this);
        newsPresenter.start();
        newsPresenter.getDate(strType[type]);
        manager =new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(manager);

    }


    @Override
    public void onClick(DialogInterface dialog, int which)
    {

    }

    @Override
    public void showProgress()
    {
        if(progress!=null)
            progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress()
    {
        if(progress!=null)
            progress.setVisibility(View.GONE);
    }

    @Override
    public void setData(List<Result.ResultBean.DataBean> list)
    {
        adapter =new NewsFragmentAdapter(list);
        recyclerView.setAdapter(adapter);
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
    public void setError()
    {

    }

    @Override
    public void addMore()
    {

    }

    @Override
    public void reflssh()
    {

    }

    @Override
    public void onItemClickLinster(Result.ResultBean.DataBean newsBean)
    {

        EventBus.getDefault().postSticky(new WebViewEvent(newsBean.getUrl(),newsBean.getTitle()));
        startActivity(new Intent(NewsFragment.this.getActivity(), WebActivity.class));
    }

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
