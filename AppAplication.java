package robot.nsk.com.robot_app.units;

import android.app.Application;
import android.content.Context;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Map;

import robot.nsk.com.robot_app.BuildConfig;
import robot.nsk.com.robot_app.commom.Config;
import robot.nsk.com.robot_app.commom.CrashHandler;


/**
 * Created by Administrator on 2018/5/18.
 */

public class AppAplication extends Application {

   public  static Context appContext;
    @Override
    public void onCreate() {
        super.onCreate();
       // RxTool.init(this);
        appContext = this;
        BaiDuYuYingUtils.init(this);
        if(BuildConfig.DEBUG){
            Logger.addLogAdapter(new AndroidLogAdapter());
               CrashHandler.getInstance().setSaveTime(3);
               CrashHandler.getInstance().init(this);
        }
    }
}
