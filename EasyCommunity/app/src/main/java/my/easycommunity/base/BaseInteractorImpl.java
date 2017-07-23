package my.easycommunity.base;

import rx.Subscription;

/**
 * Created by Administrator on 2017/7/16.
 */

public  abstract class BaseInteractorImpl<T> implements BaseInteractor<T>
{
    public Subscription compositeSubscription;
    public onCompletedLinster<T> linster ;

}
