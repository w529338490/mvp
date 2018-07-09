package robot.nsk.com.robot_app.commom;

/**
 * Created by Administrator on 2018/5/20.
 */

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import robot.nsk.com.robot_app.units.ToastUtil;


/**
 * 奔溃线程
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    // 系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private Context mContext;
    //日志 保存 天数
    private  int saveTime = 2;
    // 用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<String, String>();
    // 用于格式化日期,作为日志文件名的一部分
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    /** 保证只有一个CrashHandler实例 */
    private CrashHandler() {
    }

    /** 获取CrashHandler实例 ,单例模式 */
    private static  class CrashHandlerHolder   {
        public static  CrashHandler  INSTANCE =new CrashHandler(); ;
    }
    public static CrashHandler getInstance(){
        return CrashHandlerHolder.INSTANCE;
    }
    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
        infos.put("设备厂商",Build.MANUFACTURER+"");
        infos.put("Device Mode",Build.MODEL);
        infos.put("Android Version ",Build.VERSION.RELEASE );
        infos.put("Android SDK",Build.VERSION.SDK_INT+"");
        autoClear(saveTime);
    }

     public void setSaveTime(int saveTime) {
        this.saveTime = saveTime;
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {

                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息; 否则返回false.
     */
    private boolean handleException(final Throwable ex) {
        if (ex == null)
            return false;
        try {
            collectDeviceInfo(mContext);
            // 保存日志文件
            Observable.create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                    //保存奔溃文件
                    emitter.onNext( saveCrashInfoFile(ex));
                }})
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String path) throws Exception {
                            Looper.prepare();
                            Toast.makeText(mContext,"抱歉，程序出错了，即将退出!!",Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName + "";
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
        }
    }

    /**
     * 保存错误信息到文件中
     * @param ex
     * @return 返回文件名称,便于将文件传送到服务器
     * @throws Exception
     */
    private String saveCrashInfoFile(Throwable ex) throws Exception {
        StringBuffer sb = new StringBuffer();
        try {
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = sDateFormat.format(new Date());
            sb.append("\r\n" + date + "\n");
            for (Map.Entry<String, String> entry : infos.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                sb.append(key + "=" + value + "\n");
            }

            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);
            Throwable cause = ex.getCause();
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
            printWriter.flush();
            printWriter.close();
            String result = writer.toString();
            sb.append(result);
            String fileName = writeFile(sb.toString());
            return fileName;
        } catch (Exception e) {
            sb.append("an error occured while writing file...\r\n");
            writeFile(sb.toString());
        }
        return null;
    }

    private String writeFile(String sb) throws Exception {
        String time = formatter.format(new Date());
        String fileName = "crash-" + time + ".txt";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String path =Config.CRASH_DIR_PATH;
            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();
            FileOutputStream fos = new FileOutputStream(path + fileName, true);
            fos.write(sb.getBytes());
            fos.flush();
            fos.close();
        }
        return fileName;
    }

    /**
     * 文件删除
     * @param day 文件保存天数
     */
    public void autoClear(final int day) {
        final long now = System.currentTimeMillis();
        File crashFile =new File(Config.CRASH_DIR_PATH);

        if(crashFile.exists()){
            Observable.fromArray(new File(Config.CRASH_DIR_PATH).listFiles())
                    //过滤操作，，删除时间大于5 天的日志
                    .map(new Function<File, File>() {
                        @Override
                        public File apply(File file) throws Exception {
                            if(getTime(file.lastModified()) >= day){
                                file.delete();
                            }
                            return file;
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(new Consumer<File>() {
                        @Override
                        public void accept(File file) throws Exception {

                        }
                    });
        }
    }

    public  int getTime(long time) {
        Calendar newCalendar = Calendar.getInstance();
        Calendar oldCalendar = Calendar.getInstance();
        oldCalendar.setTime(new Date(time));
        if(newCalendar.get(Calendar.YEAR) == oldCalendar.get(Calendar.YEAR) &&
                newCalendar.get(Calendar.MONTH) >= oldCalendar.get(Calendar.MONTH )){
            int defDay =newCalendar.get(Calendar.DATE)-oldCalendar.get(Calendar.DATE);
            return defDay;
        }
        return 0;
    }

}
