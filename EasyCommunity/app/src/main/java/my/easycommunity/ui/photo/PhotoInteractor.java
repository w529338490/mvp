package my.easycommunity.ui.photo;

import java.util.List;

import my.easycommunity.entity.news.Result;
import my.easycommunity.entity.photo.GankPhoto;
import rx.Observable;
import rx.Subscription;

/**
 * Created by Administrator on 2017/7/18.
 */
public interface PhotoInteractor {

    interface  onCompletedLinster{
        void onError();
        void onSuccess(List<GankPhoto.ResultsBean> photoList , Subscription subscription);
        void onAddMoreError();

    }
    void getData( int page,Observable.Transformer  transformer);
}
