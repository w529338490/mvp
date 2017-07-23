package my.easycommunity.ui.news;

import com.orhanobut.logger.Logger;
import java.util.ArrayList;
import java.util.List;

import my.easycommunity.base.BaseInteractorImpl;
import my.easycommunity.base.BasePresenterImpl;
import my.easycommunity.base.BaseView;
import my.easycommunity.entity.news.Result;
import rx.Observable;
import rx.Subscription;

/**
 * Created by Administrator on 2017/7/16.
 */

public class NewsPresenterImpl extends BasePresenterImpl<Result.ResultBean.DataBean> implements NewsPresenter,NewsInteractor
{
    NewsInteractor newsInteractor;
    NewsView newsView;

    public NewsPresenterImpl(Observable.Transformer transformer, NewsView newsView, NewsInteractorImpl newsInteractor)
    {
        super(transformer, newsView, newsInteractor);
        this.newsInteractor = newsInteractor;
        this.newsView = newsView ;
    }

}
