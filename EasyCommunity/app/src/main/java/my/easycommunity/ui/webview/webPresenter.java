package my.easycommunity.ui.webview;

import my.easycommunity.MyBasePresenter;
import my.easycommunity.eventbus.WebViewEvent;

/**
 * Created by Administrator on 2017/7/16.
 */

public interface webPresenter extends MyBasePresenter {
    void checkData(WebViewEvent event);
}
