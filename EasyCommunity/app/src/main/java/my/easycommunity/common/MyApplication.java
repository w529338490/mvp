package my.easycommunity.common;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

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
    @Override
    public void onCreate()
    {
        super.onCreate();
        context =getApplicationContext();

        //初始化数据库
        initDataBase();
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
