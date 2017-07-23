package my.easycommunity.ui.video.dzfragment;

import java.util.ArrayList;
import java.util.List;

import my.easycommunity.base.BaseInteractorImpl;
import my.easycommunity.base.BasePresenterImpl;
import my.easycommunity.base.BaseView;
import my.easycommunity.entity.photo.GankPhoto;
import my.easycommunity.entity.video.Video;
import my.easycommunity.ui.news.NewsInteractor;
import my.easycommunity.ui.news.NewsView;
import my.easycommunity.ui.photo.PhotoInteractorImpl;
import my.easycommunity.ui.photo.PohotoView;
import rx.Observable;
import rx.Subscription;

/**
 * Created by Administrator on 2017/7/18.
 */
public class DzVideoPresenterImpl extends BasePresenterImpl<Video.DataBean.DataBeans> implements DzVideoPresenter,DzVideoInteractor
{
    DzVideoInteractor videoInteractor;
    VideoView videoView;

    public DzVideoPresenterImpl(Observable.Transformer transformer, VideoView videoView, DzVideoInteractorImpl videoInteractor)
    {
        super(transformer, videoView, videoInteractor);
        this.videoInteractor = videoInteractor;
        this.videoView = videoView ;
    }
}
