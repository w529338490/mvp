package robot.nsk.com.robot_app.units;

import com.orhanobut.logger.Logger;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import robot.nsk.com.robot_app.bean.voice.SelfRegulations;
import robot.nsk.com.robot_app.commom.Config;
import robot.nsk.com.robot_app.net.API;
import robot.nsk.com.robot_app.net.service.UserService;

/**
 * Created by Administrator on 2018/5/24.
 */

public class RxJavaDownloder {
    private String KEY="yangpengchenshig";
    String fileName ="";
    public RxJavaDownloderLisnter lisnter ;
    private Executor fixExecutor = Executors.newFixedThreadPool(5);
    private AtomicInteger atomicInteger = new AtomicInteger();
    private  long dTime ;
    private  Disposable disposable ;

    public  void  DownLoaderAudioFile(final List<SelfRegulations.DataBean.AudioListBean> audio_list, final int sex, final long startTime) {

            Observable.fromIterable(audio_list)
                    //从新命名，文件,设置成指定 格式
                    .map(new Function<SelfRegulations.DataBean.AudioListBean, String>() {
                        @Override
                        public String apply(SelfRegulations.DataBean.AudioListBean audioListBean) throws Exception {
                            fileName = audioListBean.audio_name + ".aitrip";
                            return  audioListBean.audio_name;

                        }
                    })
                    //判断本地 是否已经 下载过，没有 则下载
                    .filter(new Predicate<String>() {
                        @Override
                        public boolean test(String s) throws Exception {
                            boolean shouldDown = true;
                            File nativeFile = null;
                            if(sex == 0){
                                nativeFile = new File(Config.VIDEO_FILE_MAN + s + ".aitrip");
                            }else {
                                nativeFile = new File(Config.VIDEO_FILE_WOMAN + s + ".aitrip");
                            }
                            shouldDown = !nativeFile.exists();
                            return shouldDown;
                        }
                    })
                    .map(new Function<String, String>() {
                        @Override
                        public String apply(String s) throws Exception {
        //                    return sex == 0 ?  "audioman" + File.separator+ s :  "audiowoman"+ File.separator+ s;
                            return sex == 0 ?  "audioman" + File.separator+ s :  "audiowoman"+ File.separator+ s;
                        }
                    })
                    //判断网络状态
//                    .filter(new Predicate<String>() {
//                        @Override
//                        public boolean test(String s) throws Exception {
//                            if(!NetWorkUtils.isWifiConnected(AppAplication.appContext) && lisnter!= null){
//                                lisnter.netWorkBad();
//                            }
//                            return NetWorkUtils.isWifiConnected(AppAplication.appContext);
//                        }
//                    })
                    .flatMap(new Function<String, ObservableSource<ResponseBody>>() {
                        @Override
                        public ObservableSource<ResponseBody> apply(String s) throws Exception {
                            dTime = System.currentTimeMillis() ;
                            Logger.e(s);
                            Retrofit retrofitDown = API.getDownLoderbulider().build();
                            return retrofitDown.create(UserService.class).downLoder(s);
                        }
                    })
                    //获得 网路请求的音频 inputsteram
                    .map(new Function<ResponseBody, InputStream>() {
                        @Override
                        public InputStream apply(ResponseBody responseBody) throws Exception {
                            responseBody.contentLength();
                            return responseBody.byteStream();
                        }
                    })
                    //写入文件
                    .map(new Function<InputStream, String>() {
                        @Override
                        public String apply(InputStream inputStream) throws Exception {
                            long a = System.currentTimeMillis() - dTime;
                            Logger.e(a +" =======================" + a  +">>>>>>>>>>>>>>>>>" + fileName);
                            return writeToFile(inputStream, fileName,sex);
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            if(lisnter != null){
                                lisnter.stopDown(d);
                            }
                        }

                        @Override
                        public void onNext(String s) {
                            Logger.e(s);
                        }
                        @Override
                        public void onError(Throwable e) {
                            Logger.e(e.toString());
                           if (lisnter != null)
                               lisnter.netWorkBad();
                        }

                        @Override
                        public void onComplete() {
                            Logger.e("onComplete");
                            if(lisnter != null){
                                lisnter.onComplete();
                                long  totaloTime =   startTime - System.currentTimeMillis();
                                Logger.e(totaloTime+"==================================totaloTime");
                            }
                        }
                    });
    }

    public String writeToFile( InputStream inputStream,  String fileName, int sex) throws IOException {
                File fileParent  = null ;
                if(sex == 0){
                    fileParent = new File(Config.VIDEO_FILE_MAN);
                }else {
                    fileParent = new File(Config.VIDEO_FILE_WOMAN);
                }
                int len ;
                if (!fileParent.exists()) {
                    fileParent.mkdirs();
                }
                FileOutputStream fos=null;
                try {
                    byte[] fileReader = new byte[1024];
                    if(fileName.contains("")){
                        fileName = fileName.replace(","");
                    }
                    String videoPath = fileParent + "/" + fileName;
                    File videoFile = new File(videoPath);
                    fos =new FileOutputStream(videoFile);
                    while((len = inputStream.read(fileReader))!=-1){
                        fos.write(fileReader,0,len);
                    }
                    fos.flush();
                    return videoPath;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (fos != null) {
                        fos.close();
                    }
                }
        return "error";
    }

    public void  setRxJavaDownloderLisnter (RxJavaDownloderLisnter lisnter){
        this.lisnter = lisnter ;
    }
    public interface RxJavaDownloderLisnter {
        //下载完成
        void onComplete();
      //停止下载
        void stopDown(Disposable disposable);
        //网络连接失败
      void netWorkBad();

    }

    //将文件转换成Byte数组
    public static byte[] getBytesByFile(String pathStr) {
        File file = new File(pathStr);
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            byte[] data = bos.toByteArray();
            bos.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //将Byte数组转换成文件
    public static void getFileByBytes(byte[] bytes, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {// 判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + "\\" + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //读取中间文件大小
    public  void updata(){
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        byte[] re =new byte[2048];
        int total;
        int len ;
        int per = 0;
        File file = new File(Config.VIDEO_FILE_MAN+"01_A_01.mp3"+".aitrip");
        if(!file.exists()){
            return;
        }
        try {
            inputStream = new FileInputStream(file);
            total =inputStream.available();
            outputStream =new FileOutputStream(file);
            while ((len = inputStream.read(re)) != -1){
                per +=len;
                if(per > 10000){
                    outputStream.write(re,0,len);
                }
            }
            outputStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
