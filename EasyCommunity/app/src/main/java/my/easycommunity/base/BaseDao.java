package my.easycommunity.base;

import com.orhanobut.logger.Logger;

import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import my.easycommunity.common.MyApplication;
import my.easycommunity.db.gen.DaoMaster;
import my.easycommunity.db.gen.DaoSession;
import my.easycommunity.db.gen.UserDao;

/**
 * Created by Administrator on 2017/7/25.
 */

public class BaseDao<T>
{

    private   DaoSession daoSession;
    private   DaoMaster.DevOpenHelper helper ;
    public BaseDao()
    {
        daoSession = MyApplication.getDaoSession();
        helper=MyApplication.getHelper();
    }

    /**
     * 插入单个对象
     * @param object
     * @return
     */
    public boolean insertObject(T object){
        boolean flag = false;
        try {
          daoSession.insert(object);
            flag =true;
        } catch (Exception e) {
            Logger.e( e.toString());
        }
        return flag;
    }

    /**
     * 插入多个对象，并开启新的线程
     * @param objects
     * @return
     */
    public boolean insertMultObject(final List<T> objects){
        boolean flag = false;
        if (null == objects || objects.isEmpty()){
            return false;
        }
        try {
            daoSession.runInTx(new Runnable() {
                @Override
                public void run() {
                    for (T object : objects) {
                        daoSession.insertOrReplace(object);
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            Logger.e(e.toString());

        }finally {
//            manager.CloseDataBase();
        }
        return flag;
    }

    /**
     * 以对象形式进行数据修改
     * 其中必须要知道对象的主键ID
     * @param object
     * @return
     */
    public boolean  updateObject(T object){
        boolean flag = false;
        if (null == object){
            return flag;
        }
        try {
            daoSession.update(object);
            flag = true;
        } catch (Exception e) {
            Logger.e(e.toString());
        }

        return flag;
    }

    /**
     * 删除某个对象
     * @param object
     * @return
     */
    public boolean deleteObject(T object){
        boolean flag = false;
        if (null == object){
            return flag;
        }
        try {
            daoSession.delete(object);
            flag = true;
        } catch (Exception e) {
            Logger.e(e.toString());
        }
        return flag;
    }

    /**
     * 查询某个对象是否存在
     * @param
     * @return
     */
    public boolean isExitObject(String type,Class object ,String params){
        try {
            QueryBuilder qb = daoSession.getDao(object).queryBuilder();
           return daoSession.getDao(object).queryRaw(type, params).get(0)!=null ;

        } catch (Exception e) {
            Logger.e(e.toString());
        }
        return  false;

    }
    /**
     * 查询全部数据
     */
    public  List<T> queryAll(Class cls) {
        List<T> list =new ArrayList<>();
        try {
            list=  daoSession.getDao(cls).loadAll();
        } catch (Exception e) {
            Logger.e(e.toString());
        }
        return list;
    }
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
