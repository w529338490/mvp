package my.easycommunity.ui.video.dzfragment;

import java.util.ArrayList;
import java.util.List;

import my.easycommunity.entity.photo.GankPhoto;
import my.easycommunity.entity.video.Video;
import my.easycommunity.ui.photo.PhotoInteractorImpl;
import my.easycommunity.ui.photo.PohotoView;
import rx.Observable;
import rx.Subscription;

/**
 * Created by Administrator on 2017/7/18.
 */
public class DzVideoPresenterImpl implements DzVideoPresenter,DzVideoInteractor.onCompletedLinster
{
    DzVideoInteractor.onCompletedLinster linster;
    DzVideoInteractorImpl dzVideoInteractor;
    VideoView videoView ;
    Observable.Transformer transformer;
    List<Video.DataBean.DataBeans> listData =new ArrayList<>();

    private Subscription compositeSubscription ;

    public DzVideoPresenterImpl( VideoView videoView ,Observable.Transformer transformer )
    {
        this.videoView = videoView;
        dzVideoInteractor =new DzVideoInteractorImpl(this);
        this.transformer =transformer ;
    }


    @Override
    public void start()
    {
        videoView.showProgress();
    }

    @Override
    public void onError()
    {
        videoView.hideProgress();
    }

    @Override
    public void getDate(int page)
    {
        dzVideoInteractor.getData(page,transformer);
    }

    @Override
    public void stopNetWork()
    {

        if(compositeSubscription!=null && !compositeSubscription.isUnsubscribed()){
            compositeSubscription.unsubscribe();
        }
    }

    @Override
    public void itemOnclickLinster(int position)
    {

    }

    @Override
    public void onSuccess(List<Video.DataBean.DataBeans> listData, Subscription subscription)
    {

        compositeSubscription=subscription;
        if(listData!=null){
            this.listData = listData;
            videoView.hideProgress();
            videoView.setData(listData);
        }
    }
    @Override
    public void unsubscribe()
    {
        stopNetWork();
    }

}
