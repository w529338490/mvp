package my.easycommunity.net;

import com.orhanobut.logger.Logger;

import java.util.concurrent.TimeUnit;

import my.easycommunity.net.service.NewsService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 * Created by Administrator on 2017/7/15.
 */

public class Api
{
    private  static  Api instance;
    private static OkHttpClient.Builder builder;
    //retrofit2初始化GsonConverterFactory，转化Json 数据
    private Converter.Factory factory = GsonConverterFactory.create();
    //services；
    private NewsService newsService;

    //单列
    public static  Api getInstance(){

        if(instance == null){
            instance =new Api();
        }
        return  instance;
    }

    public Api()
    {
        builder = new OkHttpClient.Builder();
        //打印请求
        final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger()
        {
            @Override
            public void log(String message)
            {
                Logger.d(message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        builder.addInterceptor(loggingInterceptor);
        //请求 超时 时间为5秒
        builder.connectTimeout(5, TimeUnit.SECONDS);
    }


    //新闻接口
    public  NewsService getNewsService (){
        Retrofit retrofit =new Retrofit.Builder()
                .client(builder.build())
                .baseUrl(BaseUrl.uri_news)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(factory)
                .build();
        newsService =retrofit.create(NewsService.class);
        return  newsService;

    }
}
