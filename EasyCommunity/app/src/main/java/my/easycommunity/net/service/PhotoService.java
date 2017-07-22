package my.easycommunity.net.service;

import my.easycommunity.entity.photo.GankPhoto;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Administrator on 2017/7/21.
 */
public interface PhotoService {
    //获取图片
    @GET("data/福利/5/{page}")
    Observable<GankPhoto> getPhoto(@Path("page") int page);
}
