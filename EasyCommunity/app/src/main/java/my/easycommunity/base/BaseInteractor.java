package my.easycommunity.base;

import java.util.List;

import my.easycommunity.entity.news.Result;
import rx.Observable;
import rx.Subscription;

/**
 * Created by Administrator on 2017/7/16.
 */

public interface BaseInteractor<T>
{
    interface  onCompletedLinster<T>{
        void onError();
        void onSuccess(List<T> list, Subscription subscription);
        void  onAddMoreError();

    }
    void getData(Object type, Observable.Transformer transformer);

}
