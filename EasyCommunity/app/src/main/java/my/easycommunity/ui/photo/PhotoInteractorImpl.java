package my.easycommunity.ui.photo;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import my.easycommunity.base.BaseInteractorImpl;
import my.easycommunity.entity.photo.GankPhoto;
import my.easycommunity.net.Api;
import my.easycommunity.net.service.PhotoService;
import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.R.id.list;

/**
 * Created by Administrator on 2017/7/18.
 */
public class PhotoInteractorImpl extends BaseInteractorImpl<GankPhoto.ResultsBean> implements PhotoInteractor {
    List<GankPhoto.ResultsBean>list ;
    @Override
    public void getData(Object type, Observable.Transformer transformer)
    {
        netData( (int)type,transformer );
    }

    private void netData(final int page , final Observable.Transformer transformer)
    {

        PhotoService service = Api.getInstance().<PhotoService>getService(PhotoService.class);
        compositeSubscription =service.getPhoto(page)
                .subscribeOn(Schedulers.io())
                .compose(transformer)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<GankPhoto>()
                {
                    @Override
                    public void call(final GankPhoto gankPhoto)
                    {
                        if(gankPhoto.isError() == false && gankPhoto.getResults() != null)
                        {
                            if(page==1){
                                list= gankPhoto.getResults();
                                linster.onSuccess(gankPhoto.getResults(),compositeSubscription);
                            }
                            else {
                                Observable.from(gankPhoto.getResults())
                                        .compose(transformer)
                                        .subscribe(new Action1<GankPhoto.ResultsBean>()
                                {
                                    @Override
                                    public void call(GankPhoto.ResultsBean resultsBean)
                                    {
                                            list.add(resultsBean);
                                    }
                                }, new Action1<Throwable>()
                                {
                                    @Override
                                    public void call(Throwable throwable)
                                    {
                                        Logger.e(throwable.getMessage());
                                    }
                                });
                                linster.onSuccess(list,compositeSubscription);
                            }
                        }
                        else if(gankPhoto.isError() == false && gankPhoto.getResults() ==null){
                            linster.onAddMoreError();
                        }
                        else {
                            linster.onError();
                        }

                    }

                }, new Action1<Throwable>()
                {
                    @Override
                    public void call(Throwable throwable)
                    {
                        linster.onError();
                        Logger.e(throwable.toString());
                    }
                });


    }


}
