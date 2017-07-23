package my.easycommunity.common;

import android.app.Application;
import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Administrator on 2017/7/15.
 */

public class MyApplication extends Application
{
    public static Context context;

    @Override
    public void onCreate()
    {
        super.onCreate();
        context =getApplicationContext();
        //初始化Realm 数据库
        Realm.init(this);

        //数据库配置  使用默认配置
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);
    }
}
