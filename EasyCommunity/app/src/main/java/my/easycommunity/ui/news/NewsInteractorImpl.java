package my.easycommunity.ui.news;

import com.orhanobut.logger.Logger;

import my.easycommunity.base.BaseInteractorImpl;
import my.easycommunity.entity.news.Result;
import my.easycommunity.net.Api;
import my.easycommunity.net.service.NewsService;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/7/16.
 */

public class NewsInteractorImpl extends BaseInteractorImpl<Result.ResultBean.DataBean> implements NewsInteractor
{

    private void netData(String type, Observable.Transformer transformer)
    {
        NewsService service = Api.getInstance().<NewsService>getService(NewsService.class);
        Observable<Result> news= service.getNews(type);
        compositeSubscription= news.subscribeOn(Schedulers.io())//指定获取数据在io子线程
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
                            linster.onSuccess(result.getResult().getData(),compositeSubscription);
                        }else {
                            linster.onError();
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
    }

    @Override
    public void getData(Object type, Observable.Transformer transformer)
    {
        netData((String) type,transformer);
    }
}
