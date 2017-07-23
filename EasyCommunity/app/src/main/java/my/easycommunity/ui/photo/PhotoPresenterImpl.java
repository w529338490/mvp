package my.easycommunity.ui.photo;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import my.easycommunity.base.BaseInteractorImpl;
import my.easycommunity.base.BasePresenterImpl;
import my.easycommunity.base.BaseView;
import my.easycommunity.entity.news.Result;
import my.easycommunity.entity.photo.GankPhoto;
import rx.Observable;
import rx.Subscription;

/**
 * Created by Administrator on 2017/7/18.
 */
public class PhotoPresenterImpl extends BasePresenterImpl<GankPhoto.ResultsBean> implements PhotoPresenter ,PhotoInteractor{

    PhotoInteractor photoInteractor;
    PohotoView pohotoView ;
    Observable.Transformer transformer;

    public PhotoPresenterImpl(Observable.Transformer transformer, PohotoView pohotoView, PhotoInteractorImpl photoInteractor)
    {
        super(transformer, pohotoView, photoInteractor);
        this.transformer =transformer;
        this.pohotoView=pohotoView;
        this.photoInteractor =photoInteractor;
    }

    @Override
    public void onItemClickLinster(List<GankPhoto.ResultsBean> photoList, int currentPosition)
    {
        if(listdataBeans!=null){
            pohotoView.onItemClickLinster(photoList ,currentPosition);
        }
    }
}
