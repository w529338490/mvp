package my.easycommunity.common;

import com.orhanobut.logger.Logger;
import java.util.ArrayList;
import java.util.List;
import my.easycommunity.db.gen.DaoMaster;
import my.easycommunity.db.gen.DaoSession;

/**
 * Created by Administrator on 2017/7/25.
 * 数据库控制类，封装 对数据库的部分 增， 删， 改， 查方法，方便调用；
 */

public class DaoController<T>
{

    private   DaoSession daoSession;
    private   DaoMaster.DevOpenHelper helper ;
    public DaoController()
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
     * @param object  所调用的 表 名
     * @param type    表的属性 （eg：User表中的 姓名 name 属性）
     * @param params   查询的条件，类型需要匹配 type，
     * @return   true　表中存在 params 的 一条或者多条数据， false表中不存在 改条数据
     */
    public boolean isExitObject(Class object ,String type ,String params){
        try {
            if(daoSession.getDao(object).queryRaw("where "+type+" like ? ", params).size() > 0 ){
                return  true;
            }else {
                return false;
            }
        } catch (Exception e) {
            Logger.e(e.toString());
        }
        return  false;
    }

    /**
     * 查询某一个对象
     */
    public List<T> getSigleObject(Class object ,String type ,String params){
        List<T> list = null;
        try {
            list=  daoSession.getDao(object).queryRaw("where "+type+" like ? ", params);

        } catch (Exception e) {
            Logger.e(e.toString());
        }
        return  list;
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
     * onDestroy 中调用
     */
    public void closeDaoSession(){
        if ( daoSession != null){
            Logger.e(daoSession +"========================helper="+helper);
            daoSession.clear();
            daoSession = null;
        }
        if(helper !=null){
            helper.close();
            helper=null;
        }
    }
}
