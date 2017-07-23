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
import my.easycommunity.adapter.NewsFragmentAdapter;
import my.easycommunity.adapter.PhotoFragmentAdapter;
import my.easycommunity.adapter.VideoViewAdapter;
import my.easycommunity.base.BaseFragment;
import my.easycommunity.base.BasePresenter;
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

public class DzVideoFragment extends BaseFragment<Video.DataBean.DataBeans> implements VideoView
{
    private String[] strType = new String[]{"1", "2"};
    VideoViewAdapter  adapter;
    LinearLayoutManager manager;
    DzVideoPresenter dzVideoPresenter;
    int type=0;
    public static DzVideoFragment newInstance(int type) {
        DzVideoFragment newsFragment = new DzVideoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        newsFragment.setArguments(bundle);
        return newsFragment;
    }
    @Override
    public void setData(List<Video.DataBean.DataBeans> listData) {adapter.setData(listData);}

    @Override
    public void onItemClickLinster(Video.DataBean.DataBeans newsBean) {}

    @Override
    public void onPause() {super.onPause();JCVideoPlayer.releaseAllVideos();}

    @Override public int getContentRes() {return R.layout.news_fragment;}

    @Override
    public BasePresenter getPresenter() {
        dzVideoPresenter =new DzVideoPresenterImpl(this.bindToLifecycle(),this,new DzVideoInteractorImpl());
        return dzVideoPresenter;
    }
    @Override
    public void setUpView() {
        manager =new LinearLayoutManager(this.getContext());
        error_tv.setOnClickListener(this);
        if(adapter==null && getContext()!=null){
            adapter =new VideoViewAdapter(getContext());
        }
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void iniData() {}

    @Override
    public Object gettyp() {
        type =this.getArguments().getInt("type");
        return strType[type];
    }
    @Override
    public boolean adapterIsEmpty() {return adapter !=null && adapter.getItemCount()!=0;}
}
