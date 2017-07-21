package my.easycommunity.utill;

import android.text.TextUtils;
import android.util.LruCache;

/**
 * Created by Administrator on 2017/7/21.
 */
public class MyLruChace  {

    private LruCache<String ,String> lruCache ;
    private  static  MyLruChace myLruChace;


    public static MyLruChace Instace(){
        if(myLruChace ==null){
            myLruChace=new MyLruChace();
        }
        return  myLruChace;
    }
    public MyLruChace() {
        this.lruCache = new LruCache<String, String>(1000){
            @Override
            protected int sizeOf(String key, String value) {
                return value.getBytes().length;
            }
        };
    }

    public  void sava(String key,String value){
        if(TextUtils.isEmpty(key)){
            ToastUtil.show("Key shouled not null!");
            return;
        }
        lruCache.put(key,value);
    }

    public  String  getValue(String key){
        if(TextUtils.isEmpty(key)){
            ToastUtil.show("Key shouled not null!");
            return "";
        }
       return lruCache.get(key);
    }
}
