package my.easycommunity.ui.news;

import com.orhanobut.logger.Logger;
import java.util.ArrayList;
import java.util.List;
import my.easycommunity.entity.news.Result;
import rx.Observable;
import rx.Subscription;

/**
 * Created by Administrator on 2017/7/16.
 */

public class NewsPresenterImpl implements NewsPresenter,NewsInteractor.onCompletedLinster
{
    NewsInteractorImpl newsInteractor;
    NewsView newsView;
    Observable.Transformer transformer;
    List<Result.ResultBean.DataBean> listdataBeans =new ArrayList<>();

    private String type;

    private Subscription compositeSubscription ;

    public NewsPresenterImpl(Observable.Transformer transformer, NewsView newsView)
    {
        this.transformer = transformer;
        this.newsView = newsView;
        this.newsInteractor = new NewsInteractorImpl(this);
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
        newsInteractor.getData(type, transformer);

    }



    @Override
    public void stopNetWork()
    {
        if(compositeSubscription!=null && !compositeSubscription.isUnsubscribed()){
            compositeSubscription.unsubscribe();
            Logger.e("compositeSubscription ===================is 取消");
        }
    }

    @Override
    public void itemOnclickLinster(int poition)
    {
        if(listdataBeans.get(poition)!=null){
            newsView.onItemClickLinster(listdataBeans.get(poition));
        }

    }

    @Override
    public void onError() {
        newsView.showError();
    }

    @Override
    public void onSuccess(List<Result.ResultBean.DataBean> list, Subscription subscription) {
        compositeSubscription=subscription;
        if(list!=null){
            listdataBeans = list;
            newsView.hideProgress();
            newsView.setData(list);
        }
    }
}
