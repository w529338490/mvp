package my.easycommunity.common;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.common.UmLog;
import com.umeng.message.entity.UMessage;

import org.greenrobot.greendao.query.QueryBuilder;

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

    public static final String UPDATE_STATUS_ACTION = "com.umeng.message.example.action.UPDATE_STATUS";

    @Override
    public void onCreate()
    {
        super.onCreate();
        context =getApplicationContext();

        //初始化数据库
        initDataBase();

        //初始化友盟推送
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setDebugMode(false);
        mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SERVER); //声音
        mPushAgent.setNotificationPlayLights(MsgConstant.NOTIFICATION_PLAY_SERVER);//呼吸灯
        mPushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SERVER);//振动
        //sdk开启通知声音
        mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);
        //注册推送服务，每次调用register方法都会回调该接口

        /**
         * 自定义行为的回调处理，参考文档：高级功能-通知的展示及提醒-自定义通知打开动作
         * UmengNotificationClickHandler是在BroadcastReceiver中被调用，故
         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         * */
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
                Logger.e(msg +"=============UMessage=============================");
            }

        };

        mPushAgent.setNotificationClickHandler(notificationClickHandler);

        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                Logger.e(deviceToken +"=============deviceToken=============================");
                sendBroadcast(new Intent(UPDATE_STATUS_ACTION));
            }

            @Override
            public void onFailure(String s, String s1) {
                Logger.e("onFailure===================" +s);

            }
        });
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
