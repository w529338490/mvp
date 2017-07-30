package my.easycommunity.ui.register;

/**
 * Created by Administrator on 2017/7/18.
 */
public interface RegisterInteractor
{
    interface  onCompletedLinster<T>{
        void onError(String error);
        void onSuccess();
    }
    void Register(String username, String pwd);

}
