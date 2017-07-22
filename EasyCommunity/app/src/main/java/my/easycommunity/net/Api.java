package my.easycommunity.net;

import com.orhanobut.logger.Logger;
import java.util.concurrent.TimeUnit;
import my.easycommunity.net.service.NewsService;
import my.easycommunity.net.service.PhotoService;
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
    private PhotoService photoService ;

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
    public <T> T getService( Class cls) {
        String serviceName =cls.getName();
        String url = getBaseUrl(serviceName);
        Retrofit retrofit =new Retrofit.Builder()
                .client(builder.build())
                .baseUrl(url)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(factory)
                .build();
        return (T) retrofit.create(cls);
    }




    private String getBaseUrl(String serviceName)
    {
        String url = "";
        switch (serviceName){
            case "my.easycommunity.net.service.NewsService":
                url= BaseUrl.uri_news;
                break;
            case "my.easycommunity.net.service.PhotoService":
                url=BaseUrl.url_photo;
                break;
        }
        return url;
    }
}
