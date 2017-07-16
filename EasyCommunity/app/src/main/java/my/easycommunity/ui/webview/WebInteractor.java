package my.easycommunity.ui.webview;


/**
 * Created by Administrator on 2017/7/16.
 */

public interface WebInteractor
{
    interface  onCompletedLinster{
        void onError();
        void onSuccess();

    }
    void checkData(String url,String tittle );
}
