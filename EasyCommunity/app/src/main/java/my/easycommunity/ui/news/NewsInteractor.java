package my.easycommunity.ui.news;

import java.util.List;

import my.easycommunity.entity.news.Result;
import rx.Observable;
import rx.Subscription;

/**
 * Created by Administrator on 2017/7/16.
 */

public interface NewsInteractor
{
    interface  onCompletedLinster{
        void onError();
        void onSuccess(List<Result.ResultBean.DataBean> list);

    }
    Subscription getData(String type, Observable.Transformer  transformer, onCompletedLinster linster);

}
