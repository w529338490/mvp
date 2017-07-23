package my.easycommunity.ui.user;

import my.easycommunity.base.BaseInteractorImpl;
import my.easycommunity.base.BasePresenterImpl;
import my.easycommunity.base.BaseView;
import rx.Observable;

/**
 * Created by Administrator on 2017/7/18.
 */
public class UserPresenterImpl  extends BasePresenterImpl implements UserPresenter,UserInteractor
{
    public UserPresenterImpl(Observable.Transformer transformer, BaseView newsView, BaseInteractorImpl newsInteractor)
    {
        super(transformer, newsView, newsInteractor);
    }
}
