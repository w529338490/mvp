package my.easycommunity.common;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.widget.RemoteViews;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import my.easycommunity.BuildConfig;
import my.easycommunity.R;
import my.easycommunity.db.gen.DaoMaster;
import my.easycommunity.db.gen.DaoSession;

/**
 * Created by Administrator on 2017/7/15.
 */

public class MyApplication extends Application
{
    public static Context context;
    private static DaoSession daoSession;
    private static  DaoMaster.DevOpenHelper helper ;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
    @Override
    public void onCreate()
    {
        super.onCreate();
        MultiDex.install(this);
        //设置 日志模式 debug模式下打印 日志
        LogLevel logLevel = BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE;
        Logger.init().logLevel(logLevel);
        context =getApplicationContext();
        //初始化数据库
        initDataBase();
        //初始化Umeng
        initUmengPush();
    }
    private void initUmengPush() {
        //初始化友盟推送
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setDebugMode(false);
        PushAgent.getInstance(context).setDisplayNotificationNumber(10);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                Logger.e(deviceToken +"=============deviceToken=============================");
            }
            @Override
            public void onFailure(String s, String s1) {}
        });
        /**
         * 自定义行为的回调处理，参考文档：高级功能-通知的展示及提醒-自定义通知打开动作
         * UmengNotificationClickHandler是在BroadcastReceiver中被调用，故
         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         * */
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            // 发送消息来自 自定义行为
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                if ("go_custom".equals(msg.after_open)) {
                    Logger.e("=====================msg====="+ msg.extra.get("activity"));
                    JsonObject jsonObject = new Gson().fromJson(msg.custom, JsonObject.class);
                    //Intent intent = new Intent(context, WebActivity.class);
                    ////必须加入 Intent.FLAG_ACTIVITY_NEW_TASK
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //startActivity(intent);
                } else {
                    launchApp(context, msg);
                }
            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);

        UmengMessageHandler messageHandler = new UmengMessageHandler(){
            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {
                new Handler(getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        // 对于自定义消息，PushSDK默认只统计送达。若开发者需要统计点击和忽略，则需手动调用统计方法。
                        boolean isClickOrDismissed = true;
                        if(isClickOrDismissed) {
                            //自定义消息的点击统计
                            UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
                        } else {
                            //自定义消息的忽略统计
                            UTrack.getInstance(getApplicationContext()).trackMsgDismissed(msg);
                        }
                        Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
                    }
                });
            }
            @Override
            public Notification getNotification(Context context, UMessage msg) {
                switch (msg.builder_id) {
                    case 1:
                        Notification.Builder builder = new Notification.Builder(context);
                        RemoteViews myNotificationView = new RemoteViews(context.getPackageName(),
                                R.layout.notification_view);
                        myNotificationView.setTextViewText(R.id.notification_title, msg.title);
                        myNotificationView.setTextViewText(R.id.notification_text, msg.text);
                        myNotificationView.setImageViewBitmap(R.id.notification_large_icon,
                                getLargeIcon(context, msg));
                        builder.setContent(myNotificationView)
                                .setSmallIcon(getSmallIconId(context, msg))
                                .setTicker(msg.ticker)
                                .setAutoCancel(true);
                        Notification mNotification = builder.build();
                        //由于Android v4包的bug，在2.3及以下系统，Builder创建出来的Notification，并没有设置RemoteView，故需要添加此代码
                        mNotification.contentView = myNotificationView;
                        return builder.getNotification();
                    default:
                        //默认为0，若填写的builder_id并不存在，也使用默认。
                        return super.getNotification(context, msg);
                }
            }
        };
        mPushAgent.setMessageHandler(messageHandler);

    }
    public  void initDataBase()
    {
        //创建数据库Ecommunity.db"
         helper = new DaoMaster.DevOpenHelper(this, "Ecommunity.db", null);
        //获取可写数据库
        SQLiteDatabase db = helper.getWritableDatabase();
        //获取数据库对象
        DaoMaster daoMaster = new DaoMaster(db);
        //获取Dao对象管理者
        daoSession = daoMaster.newSession();

    }
    public static DaoSession getDaoSession (){
        return  daoSession;
    }
    public static DaoMaster.DevOpenHelper getHelper (){
        return  helper;
    }

}
