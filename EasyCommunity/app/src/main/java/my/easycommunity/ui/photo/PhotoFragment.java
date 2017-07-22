package my.easycommunity.ui.photo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.components.support.RxFragment;

import java.io.Serializable;
import java.util.List;

import my.easycommunity.R;
import my.easycommunity.adapter.NewsFragmentAdapter;
import my.easycommunity.adapter.PhotoFragmentAdapter;
import my.easycommunity.entity.photo.GankPhoto;
import my.easycommunity.ui.news.NewsFragment;
import my.easycommunity.utill.NetWorkUtil;
import my.easycommunity.utill.ProssBarUtil;
import my.easycommunity.utill.ToastUtil;

/**
 * Created by Administrator on 2017/7/17.
 */
public class PhotoFragment extends RxFragment implements PohotoView ,View.OnClickListener{

    private static PhotoFragment instance;
    private View view;
    @InjectView(R.id.recyclerView) RecyclerView recyclerView;
    @InjectView(R.id.srf) TwinklingRefreshLayout fresh;
    @InjectView(R.id.progress) FrameLayout progress;
    @InjectView(R.id.error_tv)TextView error_tv;
    private GridLayoutManager manager ;
    private PhotoFragmentAdapter  adapter;
    ProgressLayout header ;
    private int page=1;

    public BottomNavigationView tab_bottom;
    private   PhotoPresenterImpl  plmpl;
    private boolean fistLoad =false ;
    
    public static  PhotoFragment newInstance( BottomNavigationView tab_bottom ){
        if(instance == null){
            instance=new PhotoFragment();
            instance.tab_bottom =tab_bottom;
        }
        return instance;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        if(view==null) {
            view = inflater.inflate(R.layout.fragment_pohto, container, false);
            ButterKnife.inject(this,view);
        }
        if(getContext() !=null){
            manager =new GridLayoutManager(getContext(),2);
            recyclerView.setLayoutManager(manager);
        }

        if(adapter==null){
            adapter =new PhotoFragmentAdapter();
        }
        recyclerView.setAdapter(adapter);
        plmpl =new PhotoPresenterImpl(this,this.bindToLifecycle());
        setUpView();
        setUpData();
        return  view;
    }
    private void setUpView()
    {
        header = new ProgressLayout(this.getContext());
        fresh.setHeaderView(header);
        fresh.setEnableOverScroll(false);
        fresh.setEnableRefresh(true);

    }
    @Override
    public void onStart()
    {
        super.onStart();

    }
    @Override
    public void onResume()
    {
        super.onResume();
    }
    private void setUpData()
    {
        plmpl.start();
        addNetData();
        fresh.setOnRefreshListener(new RefreshListenerAdapter() {
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
                        page ++;
                        addNetData();
                        refreshLayout.finishLoadmore();
                    }
                }, 2000);
            }
        });

        adapter.setOnItemClickListener(new PhotoFragmentAdapter.OnItemClickListener()
        {
            @Override
            public void click(View parentView, int position)
            {
                plmpl.itemOnclickLinster(position);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                if(dy<=0){
                    tab_bottom.setVisibility(View.VISIBLE);
                }else {
                    tab_bottom.setVisibility(View.GONE);
                }

            }
        });
    }

    @Override
    public void onItemClickLinster(List<GankPhoto.ResultsBean> photoList, int currentPosition)
    {
        Intent intent =new Intent(this.getActivity(),PhotoDetailActivity.class );
        intent.putExtra("photoList", (Serializable) photoList);
        intent.putExtra("position",currentPosition);
        startActivity(intent);
    }

    @Override
    public void setData(List<GankPhoto.ResultsBean> photoList)
    {
        adapter.setData(photoList);
    }

    @Override
    public void addMoreErroe()
    {
        if(page>1){
            page --;
        }else {
            page=1;
        }

    }

    @Override
    public void showProgress()
    {
        ProssBarUtil.showBar(progress);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.error_tv:
                fistLoad =false;
                addNetData();
                break;
        }
    }
    @Override
    public void hideProgress()
    {
        ProssBarUtil.hideBar(progress);
        ProssBarUtil.hideBar(error_tv);

    }
    public  void addNetData(){

        if ( getContext()!=null && NetWorkUtil.networkCanUse(getContext())) {
            ProssBarUtil.showBar(progress);
            ProssBarUtil.hideBar(error_tv);
            plmpl.getDate(page);
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
    //离开界面时候，停止数据加载
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        Logger.e("setUserVisibleHint"+isVisibleToUser);
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser && isAdded())
        {
            plmpl.stopNetWork();
        }
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
        plmpl.unsubscribe();
        page=1;

    }
}
