package my.easycommunity.ui.video.dzfragment;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import my.easycommunity.entity.news.Result;
import my.easycommunity.entity.photo.GankPhoto;
import my.easycommunity.entity.video.Video;
import my.easycommunity.net.Api;
import my.easycommunity.net.service.NewsService;
import my.easycommunity.net.service.VideoSerVice;
import my.easycommunity.ui.photo.PhotoInteractor;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/7/18.
 */
public class DzVideoInteractorImpl implements DzVideoInteractor
{
    DzVideoInteractor.onCompletedLinster linster;
    private Subscription compositeSubscription;
    List<Video.DataBean.DataBeans> listData = new ArrayList();

    public DzVideoInteractorImpl(DzVideoInteractor.onCompletedLinster linster)
    {
        this.linster = linster;
    }
    @Override
    public void getData(int page, Observable.Transformer transformer)
    {
           getNetWorkData(transformer);
    }

    private void getNetWorkData(Observable.Transformer transformer)
    {
        VideoSerVice service = Api.getInstance().<VideoSerVice>getService(VideoSerVice.class);
        Observable<Video> news= service.getVideo();
        compositeSubscription= news.subscribeOn(Schedulers.io())//指定获取数据在io子线程
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(transformer)
                .subscribe(new Action1<Video>()
                {
                    @Override
                    public void call(Video result)
                    {
                        if (result.getMessage().equals("success"))
                        {
                            listData = result.data.data;
                            linster.onSuccess(listData, compositeSubscription);
                        } else
                        {
                            linster.onError();
                        }
                    }
                }, new Action1<Throwable>()
                {
                    @Override
                    public void call(Throwable throwable)
                    {
                        linster.onError();
                        Logger.e(throwable.getMessage());
                    }
                });
    }

}
