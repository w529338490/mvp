package my.easycommunity.ui.video.dzfragment;

import java.util.List;

import my.easycommunity.entity.photo.GankPhoto;
import my.easycommunity.entity.video.Video;
import rx.Observable;
import rx.Subscription;

/**
 * Created by Administrator on 2017/7/18.
 */
public interface DzVideoInteractor
{

    interface  onCompletedLinster{
        void onError();
        void onSuccess(List<Video.DataBean.DataBeans> listData , Subscription subscription);


    }
    void getData(int page, Observable.Transformer  transformer);
}
