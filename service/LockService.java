package robot.nsk.com.robot_app.service;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.provider.Settings;

import com.orhanobut.logger.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import robot.nsk.com.robot_app.activitys.MainActivity;
import robot.nsk.com.robot_app.activitys.lockNewActivity;
import robot.nsk.com.robot_app.bean.user.ChangeRegulateStatus;
import robot.nsk.com.robot_app.bean.user.PadBindUser;
import robot.nsk.com.robot_app.commom.Config;
import robot.nsk.com.robot_app.net.API;
import robot.nsk.com.robot_app.net.service.UserService;
import robot.nsk.com.robot_app.units.AppDavikActivityMgr;
import robot.nsk.com.robot_app.units.player.Play3;

public class LockService extends Service {
    Map params = new HashMap();
    Map<String, Integer> vodioParams = new HashMap();
    Intent intent1;
    String device_id = "0";

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_USER_PRESENT);
        LockService.this.registerReceiver(mScreenOffReceiver, intentFilter);
        super.onCreate();
        intent1 = new Intent(LockService.this, lockNewActivity.class);
        Config.CACHE.put("progress", -1);
        Config.CACHE.put("plan_id", -1);
        Config.CACHE.put("current_time", -1);
        device_id = Settings.Secure.getString(LockService.this.getContentResolver(), Settings.Secure.ANDROID_ID);
        Config.CACHE.put("device_id", device_id);
        new Thread() {
            @Override
            public void run() {
                super.run();
                for (;;) {
                    try {
                        //上传 本地断网信息
                        checkNativeInfo();
                        //上传信息
                        uploadInfo();
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private void checkNativeInfo() {

        final SharedPreferences sp = getSharedPreferences("ViceInfo", Context.MODE_PRIVATE);
        if (sp.getInt("user_id", -1) != -1) {
            vodioParams.put("user_id", sp.getInt("user_id", -1));
            vodioParams.put("record_id", sp.getInt("record_id", -1));
            vodioParams.put("status", 2);
            vodioParams.put("plan_id", sp.getInt("plan_id", -1));
            vodioParams.put("complete_time", sp.getInt("complete_time", -1));
            API.newInstance().getservice(UserService.class).changeRegulateStatus(vodioParams)
                    .filter(new Predicate<ChangeRegulateStatus>() {
                        @Override
                        public boolean test(ChangeRegulateStatus changeRegulateStatus) throws Exception {
                            return changeRegulateStatus != null && changeRegulateStatus.status == 200;
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(new Consumer<ChangeRegulateStatus>() {
                        @Override
                        public void accept(ChangeRegulateStatus changeRegulateStatus) throws Exception {
                            sp.edit().clear();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Logger.e(throwable.toString());
                        }
                    });
        }
    }

    private void uploadInfo() {
        if (params == null) {
            params = new HashMap();
        }

        params.put("device_id", Config.CACHE.get("device_id"));
        params.put("power", Config.CACHE.get("batteryPower") + "");
        params.put("wifi", "" + Config.CACHE.get("wifiPower"));
        params.put("plan_id", "" + Config.CACHE.get("plan_id"));
        if (Config.CACHE.get("progress") == null) {
            params.put("progress", "" + -1);
        } else {
            params.put("progress", "" + Config.CACHE.get("progress"));
        }

        if (Config.CACHE.get("current_time") == null) {
            params.put("current_time", "" + -1);
        } else {
            params.put("current_time", "" + Config.CACHE.get("current_time"));
        }

  //      Logger.e("=======CACHE========" + Config.CACHE);

        API.newInstance().getservice(UserService.class).getPadBindUser(params)
                .subscribe(new Consumer<PadBindUser>() {
                    @Override
                    public void accept(PadBindUser padBindUser) throws Exception {

                        if (padBindUser != null && padBindUser.status == 201) {
                            if(    Config.CACHE.get("disposable")!=null){
                              Disposable  d = (Disposable) Config.CACHE.get("disposable");
                              d.dispose();
                              d = null ;
                            }
                            if (Config.CACHE.get("player") != null) {
                                Play3 mActivity = (Play3) Config.CACHE.get("player");
                                mActivity.relecs();
                                mActivity = null;
                            }
                            int atLocking;
                            if (Config.CACHE.get("atLocking") != null) {
                                atLocking = (int) Config.CACHE.get("atLocking");
                            } else {
                                atLocking = 0;
                            }
                            Config.CACHE.clear();
                            Config.CACHE.put("progress", -1);
                            Config.CACHE.put("plan_id", -1);
                            Config.CACHE.put("current_time", -1);
                            Config.CACHE.put("device_id", device_id);

                            if(padBindUser.data!=null ){
                                Config.CACHE.put("device_numid",padBindUser.data.device_numid);
                            }

                            if (atLocking == 1) {

                            } else {
                                Intent intent = new Intent();
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setClass(LockService.this, lockNewActivity.class);
                                startActivity(intent);
                            }

                        } else if (padBindUser != null && padBindUser.status == 200 && padBindUser.data != null) {
                            Config.CACHE.put("user", padBindUser);
                            Config.CACHE.put("userName", "" + padBindUser.data.real_name);
                            Config.CACHE.put("user_id", Integer.valueOf(padBindUser.data.user_id));
                            Config.CACHE.put("device_numid",padBindUser.data.device_numid);
                            if(padBindUser.data.user_id == null ){
                                Intent intent = new Intent();
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setClass(LockService.this, lockNewActivity.class);
                                startActivity(intent);
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
           //             Logger.e(throwable.toString());
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterComponent();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    private BroadcastReceiver mScreenOffReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF) || intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                if (intent1 != null) {
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                   startActivity(intent1);
                }
            }
        }
    };

    public void unregisterComponent() {
        if (mScreenOffReceiver != null) {
            LockService.this.unregisterReceiver(mScreenOffReceiver);
        }
    }
}
