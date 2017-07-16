package my.easycommunity.ui.webview;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;

/**
 * Created by Administrator on 2017/7/16.
 */

public class WebInteractorImpl  implements  WebInteractor
{

    private onCompletedLinster linster ;

    public WebInteractorImpl(onCompletedLinster linster)
    {
        this.linster = linster;
    }

    @Override
    public void checkData(String url, String tittle)
    {
        if(!TextUtils.isEmpty(url) && !TextUtils.isEmpty(tittle)){
            linster.onSuccess();

        }else {
            linster.onError();

        }

    }
}
