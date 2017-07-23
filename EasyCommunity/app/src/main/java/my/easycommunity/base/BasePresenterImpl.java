package my.easycommunity.base;

import java.util.ArrayList;
import java.util.List;

import my.easycommunity.base.BaseInteractorImpl;
import my.easycommunity.base.BaseInteractor;
import my.easycommunity.base.BasePresenter;
import my.easycommunity.base.BaseView;
import rx.Observable;
import rx.Subscription;

/**
 * Created by Administrator on 2017/7/16.
 */

public class BasePresenterImpl<T > implements BasePresenter,BaseInteractor.onCompletedLinster<T>
{
    BaseInteractorImpl newsInteractor ;
    BaseView newsView;
    public Observable.Transformer transformer;
    public   List<T> listdataBeans =new ArrayList<>();

    public Object type;

    public Subscription compositeSubscription ;
    public  int position ;

    public BasePresenterImpl(Observable.Transformer transformer, BaseView newsView , BaseInteractorImpl newsInteractor)
    {
        this.transformer = transformer;
        this.newsView =  newsView;
        this.newsInteractor= newsInteractor;
        this.newsInteractor.linster=this;
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
    public void onError() {
        newsView.showError();
    }

    @Override
    public void onSuccess(List<T> list, Subscription subscription)
    {
        compositeSubscription=subscription;
        if(list!=null){
            listdataBeans = list;
            newsView.hideProgress();
            newsView.setData(list);
        }
    }

    @Override
    public void onAddMoreError()
    {
        newsView.addMoreErroe();
    }

    @Override
    public void getDate(Object type)
    {
        this.type =  type;
        newsInteractor.getData(type, transformer);

    }

    @Override
    public void stopNetWork()
    {
        if(compositeSubscription!=null && !compositeSubscription.isUnsubscribed()){
            compositeSubscription.unsubscribe();
        }
    }

    @Override
    public void itemOnclickLinster(int poition)
    {
        position=poition;
        if(listdataBeans.get(poition)!=null){

            newsView.onItemClickLinster(listdataBeans.get(poition));
        }

    }

}
