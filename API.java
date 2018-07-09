package robot.nsk.com.robot_app.net;

import android.util.Log;
import android.widget.Adapter;

import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import robot.nsk.com.robot_app.BuildConfig;
import robot.nsk.com.robot_app.net.service.UserService;

public class API  {
    private static OkHttpClient.Builder builder ;
    private API(){
        if(builder == null ){
            builder = new OkHttpClient.Builder();
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger()
            {
                @Override
                public void log(String message)
                {
                    Logger.d(message);
                }
            });
            loggingInterceptor.setLevel(BuildConfig.DEBUG? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
            builder.addInterceptor(loggingInterceptor);
            //请求 超时 时间为5秒
            builder.connectTimeout(5, TimeUnit.SECONDS);

        }
    }
    private static class SingletonHolder{
        private final static API instance = new API();
        private final static Retrofit.Builder BUILDER = new Retrofit.Builder();
        private  static Retrofit retrofit =   SingletonHolder.BUILDER
                .baseUrl(BaseUrl.BASE_URL_TEST)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();

        private  final  static  Retrofit.Builder DOWNLODER_BUILDER = new Retrofit.Builder();

    }
    public static Retrofit.Builder getDownLoderbulider (){
        //https://audio-shuimian.oss-cn-beijing.aliyuncs.com/testappman/01_A_00.mp3",
        SingletonHolder.DOWNLODER_BUILDER
                .baseUrl("http://192.168.1.199:8080/")
         //      .baseUrl("http://192.168.199.166:8080/")
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        return   SingletonHolder.DOWNLODER_BUILDER;

    }

    public static API newInstance(){
        return SingletonHolder.instance;
    }

    /**
     *
     * @param service   传入的接口名字  eg:UserService。class
     * @param <T>       返回接口类
     * @return
     * 参考  retrofit.create(),泛型 方法使用
     */
    public <T> T getservice(final Class<T> service){
        return  SingletonHolder.retrofit.create(service);
    }
    public  Gson formatGson (){
        return new Gson();
    }

    public void changeIp(String tagIP){
        BaseUrl.setBaseUrlTest(tagIP);
        SingletonHolder.retrofit = null ;
        SingletonHolder.retrofit =  SingletonHolder.BUILDER.baseUrl(BaseUrl.BASE_URL_TEST).build();
    }

}
