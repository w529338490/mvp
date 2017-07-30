package my.easycommunity.ui.login;

import java.util.List;

import rx.Subscription;

/**
 * Created by Administrator on 2017/7/18.
 */
public interface LoginInteractor
{
    interface  onCompletedLinster<T>{
        void onError(String error);
        void onSuccess();
    }
    void Login( String username , String pwd );

}
