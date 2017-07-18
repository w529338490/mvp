package my.easycommunity.ui.webview;

import com.orhanobut.logger.Logger;

import my.easycommunity.eventbus.WebViewEvent;

/**
 * Created by Administrator on 2017/7/16.
 */

public class WebPresenterImpl implements webPresenter,WebInteractor.onCompletedLinster
{
    webView webView;
    WebInteractorImpl webInteractor ;
    WebViewEvent event ;


    public WebPresenterImpl(my.easycommunity.ui.webview.webView webView)
    {
        this.webView = webView;
        webInteractor =new WebInteractorImpl(this);
    }

    @Override
    public void checkData(WebViewEvent event)
    {
        this.event=event;
        webInteractor.checkData(event.getUrl(),event.getTitttle() );
    }

    @Override
    public void onError()
    {

    }
    @Override
    public void onSuccess()
    {
        webView.showData();
        webView.hideProgress();

    }

    @Override
    public void start()
    {
        webView.showProgress();
    }

    @Override
    public void unsubscribe()
    {
        event =null;
        webInteractor =null;
        webView=null;
        Logger.e("unsubscribe"+event);
    }
}
