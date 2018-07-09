package robot.nsk.com.robot_app.commom;

/**
 * Created by Administrator on 2018/5/20.
 */

import android.os.Environment;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import robot.nsk.com.robot_app.units.AppAplication;

/**
 * 通用全局字段配置
 * eg:微信key,支付宝 key
 */

public class Config {

    //服务器
    public final static String BASE_URL = "http://192.168.199.197/index.php/";
    //奔溃日志 本地文件夹
    public final static String CRASH_DIR_PATH = Environment.getExternalStorageDirectory() + File.separator + "AppCrash" + File.separator;

    //公用缓存 Map;
    public final static Map CACHE = new HashMap();

    //音频文件 保存 路径
    public final  static  String VIDEO_FILE_MAN = AppAplication.appContext.getFilesDir().getAbsolutePath()+ File.separator+"man"+ File.separator;
    public final  static  String VIDEO_FILE_WOMAN = AppAplication.appContext.getFilesDir().getAbsolutePath()+ File.separator+"woman" + File.separator;

}