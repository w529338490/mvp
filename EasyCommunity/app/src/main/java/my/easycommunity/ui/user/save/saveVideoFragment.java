package my.easycommunity.ui.user.save;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.components.support.RxFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import my.easycommunity.R;
import my.easycommunity.adapter.SaveVideoViewAdapter;
import my.easycommunity.common.DaoController;
import my.easycommunity.common.MyApplication;
import my.easycommunity.db.gen.dbTable.SaveVideo;
import my.easycommunity.utill.MyLruChace;
import my.easycommunity.utill.ToastUtil;

/**
 * Created by Administrator on 2017/7/30.
 */

public class saveVideoFragment extends RxFragment
{
    private View view;
    private RecyclerView recyview;
    private List<SaveVideo> saveVideos;
    private SaveVideoViewAdapter adpter;
    private LinearLayoutManager manger;
    private static saveVideoFragment instance;

    public static saveVideoFragment newInstance(){
        if(instance == null){
            instance=new saveVideoFragment();
        }
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if(view==null){
            view=inflater.inflate(R.layout.fragment_savevideo,container,false);
        }

        initView();
        return view;

    }

    private void initView()
    {
        saveVideos=new ArrayList<>();
        DaoController<SaveVideo> videoe=new DaoController<SaveVideo>();
        Logger.e(MyLruChace.Instace().getValue("user")+"===============================");
        saveVideos = videoe.getSigleObject(SaveVideo.class,"username", MyLruChace.Instace().getValue("user"));

        recyview= (RecyclerView) view.findViewById(R.id.recyview);
        manger=new LinearLayoutManager(saveVideoFragment.this.getContext());

        adpter=new SaveVideoViewAdapter(saveVideos,this.getContext());
        recyview.setAdapter(adpter);
        recyview.setLayoutManager(manger);
        adpter.SetDownlodLiner(new SaveVideoViewAdapter.DownlodLiner()
        {
            @Override
            public void down(String path, String tittle)
            {
                DownLodVieo(path,tittle);
            }

            @Override
            public void delet(SaveVideo currentBean,int position)
            {
                Logger.e(currentBean+"");
                MyApplication.getDaoSession().getSaveVideoDao().delete(currentBean);

                DaoController<SaveVideo> daoController =new DaoController<SaveVideo>();
                if(daoController.deleteObject(currentBean)){
                    saveVideos.remove(position);

                }
                adpter.notifyDataSetChanged();

            }
        });
    }
    public void DownLodVieo(String path,String tittle)
    {
        DownTask downTask=new DownTask();
        downTask.execute(path,tittle);

    }
    class DownTask extends AsyncTask<String, Integer, byte[]>
    {
        private ProgressDialog progressDialog;//进度条
        private byte[] current = new byte[2 * 1024];//每次读到的字节数组
        private byte[] total;//下载图片后汇总的字节数组
        private boolean flag; //是否被取消
        private String tittle;
        //下载之前 干么事
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(saveVideoFragment.this.getContext());
            progressDialog.setMessage("正在操作中，请稍候……");
            progressDialog.setMax(100);//进度条最大值
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//水平样式
            progressDialog.setIndeterminate(false);//进度条的动画效果（有动画则无进度值）

            flag = true;
            //退出对话框事件
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    flag = false;
                }
            });
            progressDialog.show();
        }
        //后台执行任务
        @Override
        protected byte[] doInBackground(String... params)
        {
            tittle=params[1];
            try {
                //创建URL对象，用于访问网络资源
                URL url = new URL(params[0]);
                //获得HttpUrlConnection对象
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                //设置超时时间
                http.setConnectTimeout(5000);
                Logger.e( http.getResponseMessage());
                //获得总长度
                int length = http.getContentLength();
                Logger.e(length/1024+"");
                Logger.e("=======", length + "");
                //为total分配空间
                total = new byte[length];

                //开始读取数据
                int pointer = 0;//已用掉的索引
                InputStream is = http.getInputStream();

                int real = is.read(current); //读取当前批次的字节并保存到数组中
                while(flag && real > 0){
                    //如果读取到了字节，则保存到total中
                    for(int i = 0; i < real; i ++){
                        total[pointer + i] = current[i];
                    }
                    //指针向后移
                    pointer += real;
                    //计算进度
                    int progress = (int)((double)pointer / length * 100);//先计算出百分比在转换成整型
                    //更新进度
                    publishProgress(progress, pointer, length);
                    //继续读取下一批
                    real = is.read(current);
                }
                //关闭流对象
                is.close();
                //将获得的所有字节全部返回
                return total;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(byte[] bytes)
        {
            super.onPostExecute(bytes);
            progressDialog.dismiss();
            if(bytes!=null)
            {
                saveCroppedImage(bytes,tittle);
            }else
            {
                ToastUtil.show("下载失败!");
            }


        }

        //更新进度条回调
        @Override
        protected void onProgressUpdate(Integer... values)
        {
            super.onProgressUpdate(values);
//            progressDialog.setMessage(
//                    String.format("已读%d M, 共%d M",//字节为单位
//                            values[1] / 1024 / 1024, values[2] / 1024 / 1024));//将values[1]赋给第一个%d,第二个同理

            progressDialog.setProgress(values[0]);//进度动态提示

        }
    }

    //把下载的 字节数组,写入指定文件
    private void saveCroppedImage(byte[] bytes,String tittle)
    {
        String path = null;
        //System.currentTimeMillis()
        String imageName = tittle + ".mp4";
        String local_file =
                Environment.getExternalStorageDirectory().getAbsolutePath() + "/GitApp/";
        File f = new File(local_file);
        if (!f.exists())
        {
            f.mkdirs();  //mkdirs 创建多级目录,防止上级目录不存在
        }
        path = f.getAbsolutePath() + "/" + imageName;
        File saveIamge = new File(path);

        FileOutputStream outputStream = null;
        try
        {
            outputStream = new FileOutputStream(saveIamge);
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
            ToastUtil.showLong("下载完成,文件在："+saveIamge.getAbsolutePath());
        } catch (IOException e)
        {
            ToastUtil.showLong("下载失败,文件在");
            e.printStackTrace();
        } finally
        {
            try
            {
                if (outputStream != null)
                {
                    outputStream.close();
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

    }
}
