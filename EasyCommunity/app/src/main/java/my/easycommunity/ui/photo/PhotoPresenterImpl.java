package my.easycommunity.ui.photo;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import my.easycommunity.entity.news.Result;
import my.easycommunity.entity.photo.GankPhoto;
import rx.Observable;
import rx.Subscription;

/**
 * Created by Administrator on 2017/7/18.
 */
public class PhotoPresenterImpl implements PhotoPresenter ,PhotoInteractor.onCompletedLinster{

    PhotoInteractorImpl photoInteractor;
    PohotoView pohotoView ;
    Observable.Transformer transformer;
    List<GankPhoto.ResultsBean> listdataBeans =new ArrayList<>();

    private Subscription compositeSubscription ;

    public PhotoPresenterImpl(PohotoView pohotoView,Observable.Transformer transformer)
    {
        this.pohotoView = pohotoView;
        photoInteractor =new PhotoInteractorImpl(this);
        this.transformer =transformer ;
    }


    @Override
    public void start()
    {
        pohotoView.showProgress();
    }

    @Override
    public void onError()
    {
        pohotoView.hideProgress();
    }

    @Override
    public void onSuccess(List<GankPhoto.ResultsBean> photoList, Subscription subscription)
    {
        compositeSubscription=subscription;
        if(photoList!=null){
            listdataBeans = photoList;
            pohotoView.hideProgress();
            pohotoView.setData(photoList);
        }
    }

    @Override
    public void onAddMoreError()
    {

        pohotoView.addMoreErroe();
    }

    @Override
    public void getDate(int page)
    {
        photoInteractor.getData(page,transformer);
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
        if(listdataBeans.get(position)!=null){
            pohotoView.onItemClickLinster(listdataBeans, position);
        }
    }

    @Override
    public void unsubscribe()
    {
        stopNetWork();
    }
}
