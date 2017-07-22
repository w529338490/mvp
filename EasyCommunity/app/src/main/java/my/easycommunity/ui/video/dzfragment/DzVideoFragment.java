package my.easycommunity.ui.video.dzfragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.trello.rxlifecycle.components.support.RxFragment;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import my.easycommunity.R;
import my.easycommunity.adapter.PhotoFragmentAdapter;
import my.easycommunity.adapter.VideoViewAdapter;
import my.easycommunity.entity.photo.GankPhoto;
import my.easycommunity.entity.video.Video;
import my.easycommunity.ui.news.NewsFragment;
import my.easycommunity.ui.video.VideoFragmet;
import my.easycommunity.utill.NetWorkUtil;
import my.easycommunity.utill.ProssBarUtil;
import my.easycommunity.utill.ToastUtil;

import static my.easycommunity.R.id.fresh;

/**
 * Created by Administrator on 2017/7/22.
 */

public class DzVideoFragment extends RxFragment implements VideoView
{


    @InjectView(fresh) public TwinklingRefreshLayout twinklingRefreshLayout;
    @InjectView(R.id.news_rcv)
    RecyclerView recyclerView;
    @InjectView(R.id.progress)
    FrameLayout progress;
    @InjectView(R.id.error_tv)TextView error_tv;
    private String[] strType = new String[]{"1", "2"};
    private static DzVideoFragment instance;
    private View view;
    VideoViewAdapter  adapter;
    LinearLayoutManager manager;
    ProgressLayout header ;

    private boolean fistLoad =false ;

    private  int page =1;
    DzVideoPresenterImpl dzVideoPresenter;
    public static DzVideoFragment newInstance(int type)
    {
        DzVideoFragment newsFragment = new DzVideoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        newsFragment.setArguments(bundle);
        return newsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if(view ==null){
            view = inflater.inflate(R.layout.dzvideo, container, false);
            ButterKnife.inject(this,view);
        }
        dzVideoPresenter =new DzVideoPresenterImpl(this,this.bindToLifecycle());
        if(getContext() !=null){
            manager =new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(manager);
        }

        if(adapter==null && this.getContext()!=null){
            adapter =new VideoViewAdapter(this.getContext());
        }
        recyclerView.setAdapter(adapter);
        setUpView();
        setUpData();
        return view;
    }

    private void setUpView()
    {
        header = new ProgressLayout(this.getContext());
        twinklingRefreshLayout.setHeaderView(header);
        twinklingRefreshLayout.setEnableOverScroll(false);
        twinklingRefreshLayout.setEnableRefresh(true);
    }


    private void setUpData()
    {

        dzVideoPresenter.start();
        addNetData();
        twinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fistLoad=false;
                        page=1;
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
                        fistLoad=false;
                        refreshLayout.finishLoadmore();
                    }
                }, 2000);
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
    }

    @Override
    public void setData(List<Video.DataBean.DataBeans> listData)
    {
        adapter.setData(listData);
    }


    @Override
    public void onPause()
    {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    public  void addNetData(){

        if ( getContext()!=null && NetWorkUtil.networkCanUse(getContext())) {
            ProssBarUtil.showBar(progress);
            ProssBarUtil.hideBar(error_tv);
            dzVideoPresenter.getDate(page);
        } else {
            ProssBarUtil.hideBar(progress);
            ProssBarUtil.showBar(error_tv);
            if( adapter !=null && adapter.getItemCount()!=0){
                ProssBarUtil.hideBar(error_tv);
            }
            if(!fistLoad){
                ToastUtil.show("请检测你的网络！");
            }
        }
    }
}
