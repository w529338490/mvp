package my.easycommunity.ui.news;

import java.util.ArrayList;
import java.util.List;

import my.easycommunity.adapter.NewsFragmentAdapter;
import my.easycommunity.entity.news.Result;
import my.easycommunity.utill.ToastUtil;
import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2017/7/16.
 */

public class NewsPresenterImpl implements NewsPresenter
{
    NewsInteractorImpl newsInteractor;
    NewsView newsView;
    Observable.Transformer transformer;
    List<Result.ResultBean.DataBean> listdataBeans =new ArrayList<>();

    private String type;

    private List<Subscription>  compositeSubscription =new ArrayList<>();

    public NewsPresenterImpl(Observable.Transformer transformer, NewsView newsView)
    {
        this.transformer = transformer;
        this.newsView = newsView;
        this.newsInteractor = new NewsInteractorImpl(new NewsFragmentAdapter.OnItemClickListener()
        {
            @Override
            public void getData(int position)
            {

            }
        });

    }

    @Override
    public void start()
    {
        newsView.showProgress();
    }

    @Override
    public void unsubscribe()
    {
        stopNetWork();
    }

    @Override
    public void getDate(String type)
    {
        this.type =type;
          Subscription subscription = newsInteractor.getData(type, transformer, new NewsInteractor.onCompletedLinster()
        {
            @Override
            public void onError()
            {
                newsView.setError();
            }
            @Override
            public void onSuccess(List<Result.ResultBean.DataBean> list)
            {
                if(list!=null){
                    listdataBeans = list;
                    newsView.hideProgress();
                    newsView.setData(list);

                }

            }
        });
        compositeSubscription.add(subscription);
    }

    @Override
    public void addMore(int pageNum)
    {

    }

    @Override
    public void stopNetWork()
    {
        if(compositeSubscription!=null){
            for(Subscription subscription :compositeSubscription){
                subscription.unsubscribe();
            }
        }
    }

    @Override
    public void itemOnclickLinster(int poition)
    {
        if(listdataBeans.get(poition)!=null){
            newsView.onItemClickLinster(listdataBeans.get(poition));
        }

    }
}
