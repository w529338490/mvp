package my.easycommunity.ui.news;

import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.util.concurrent.TimeUnit;

import my.easycommunity.adapter.NewsFragmentAdapter;
import my.easycommunity.entity.news.Result;
import my.easycommunity.net.Api;
import my.easycommunity.net.service.NewsService;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2017/7/16.
 */

public class NewsInteractorImpl implements NewsInteractor
{

    public Subscription compositeSubscription;
    NewsFragmentAdapter.OnItemClickListener listener;

    public NewsInteractorImpl(NewsFragmentAdapter.OnItemClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public  Subscription getData(String type, Observable.Transformer transformer, final onCompletedLinster linster)
    {
        return netData(type, transformer, linster);
    }

    private Subscription netData(String type, Observable.Transformer transformer, final onCompletedLinster linster)
    {
        NewsService service = Api.getInstance().getNewsService();
        Observable<Result> news= service.getNews(type);
        Subscription subscription = news.subscribeOn(Schedulers.io())//指定获取数据在io子线程
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(transformer)
                .subscribe(new Action1<Result>()
                {
                    @Override
                    public void call(Result result)
                    {
                        if (result.getError_code() ==0 &&result.getResult() !=null)
                        {
                            linster.onSuccess(result.getResult().getData());
                        }

                    }
                }, new Action1<Throwable>()
                {
                    @Override
                    public void call(Throwable throwable)
                    {
                        Logger.e(throwable.getMessage());
                        linster.onError();
                    }
                });
        this.compositeSubscription =subscription;
        return subscription;
    }

}
