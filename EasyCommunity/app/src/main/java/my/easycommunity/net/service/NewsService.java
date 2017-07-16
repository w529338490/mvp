package my.easycommunity.net.service;

import my.easycommunity.entity.news.Result;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2017/7/15.
 */

public interface NewsService
{
    @GET("index?key=9e05423f7ac6acf6d0dce3425c4ea9fe")
    Observable<Result> getNews(@Query("type") String type);
}
