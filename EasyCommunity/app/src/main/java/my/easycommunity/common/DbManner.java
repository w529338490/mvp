package my.easycommunity.common;

import com.orhanobut.logger.Logger;

import my.easycommunity.db.gen.DaoMaster;
import my.easycommunity.db.gen.DaoSession;

/**
 * Created by Administrator on 2017/7/25.
 * 数据库管理类
 */

public class DbManner
{
    private  DaoSession daoSession;
    private static  DaoMaster.DevOpenHelper helper ;

    /**
     * 关闭数据库
     */
    public void closeDataBase(){
        closeDaoSession();
    }

    public void closeDaoSession(){
        if ( daoSession != null){
            Logger.e(daoSession +"=========================");
            daoSession.clear();
            daoSession = null;
        }
        if(helper !=null){
            helper.close();
            helper=null;
        }
    }
}
