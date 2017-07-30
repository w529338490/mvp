package my.easycommunity.base;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
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
import butterknife.ButterKnife;
import butterknife.InjectView;
import my.easycommunity.R;
import my.easycommunity.utill.NetWorkUtil;
import my.easycommunity.utill.ProssBarUtil;
import my.easycommunity.utill.ToastUtil;

/**
 * Created by Administrator on 2017/7/23.
 */

public abstract class BaseFragment<T> extends RxFragment implements BaseView<T>,View.OnClickListener
{
    public  View view;
    public BasePresenter presenter;

    public TwinklingRefreshLayout twinklingRefreshLayout;
    public FrameLayout progress;
    public TextView error_tv;

    public RecyclerView recyclerView;
    public boolean fistLoad =false ;
    public ProgressLayout header ;
    public int page=1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        if(getContentRes()!=0){

            view= inflater.inflate(getContentRes(),container,false);
            twinklingRefreshLayout = (TwinklingRefreshLayout) view.findViewById(R.id.fresh);
            progress = (FrameLayout) view.findViewById(R.id.progress);
            error_tv = (TextView) view.findViewById(R.id.error_tv);
            recyclerView = (RecyclerView) view.findViewById(R.id.news_rcv);

            presenter =getPresenter();
            baseInitView();
            baseInitData();
        }

        return view;
    }
    protected void baseInitView(){
        fistLoad=true;
        setUpView();
        addNetData();
        error_tv.setOnClickListener(this);

    };
    private void baseInitData()
    {

        presenter.start();
        header = new ProgressLayout(getContext());
        twinklingRefreshLayout.setEnableOverScroll(false);
        twinklingRefreshLayout.setEnableRefresh(true);
        iniData();
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
                        page ++;
                        addNetData();
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
        ProssBarUtil.hideBar(error_tv);
        ProssBarUtil.showBar(recyclerView);
    }

    @Override
    public void showError()
    {
        ProssBarUtil.hideBar(progress);
        ProssBarUtil.showBar(error_tv);
        ProssBarUtil.hideBar(recyclerView);
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.error_tv:
                fistLoad =false;
                addNetData();
                break;
        }
    }
    public  void addNetData(){
        if ( getContext()!=null && NetWorkUtil.networkCanUse(getContext())) {
            ProssBarUtil.showBar(progress);
            ProssBarUtil.hideBar(error_tv);
            presenter.getDate(gettyp());
        } else {
            ProssBarUtil.hideBar(progress);
            ProssBarUtil.showBar(error_tv);
            if( adapterIsEmpty()){
                ProssBarUtil.hideBar(error_tv);
            }
            if(!fistLoad){
                ToastUtil.show("请检测你的网络！");
            }
        }
    }
    public abstract  int getContentRes();
    public abstract BasePresenter getPresenter();
    public abstract void setUpView();
    public abstract void iniData();
    public abstract Object gettyp();
    public abstract boolean adapterIsEmpty();

}
