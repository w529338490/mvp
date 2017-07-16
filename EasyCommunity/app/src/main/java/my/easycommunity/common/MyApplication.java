package my.easycommunity.common;

import android.app.Application;
import android.content.Context;

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
    }
}
