package my.easycommunity.utill;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import my.easycommunity.common.MyApplication;

/**
 * Created by Administrator on 2017/7/22.
 */

public class DownLoadUtil
{
    public  static  boolean saveImageToNative(Bitmap bitmap ){
        Context context = MyApplication.context;
        boolean success =false;
        if(bitmap==null){
            ToastUtil.show("保存失败");
            return false;
        }
        String path = null;
        String imageName = System.currentTimeMillis() + ".png";
        String local_file =
                Environment.getExternalStorageDirectory().getAbsolutePath() + "/EasyCommuntity/";
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
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            Uri uri = Uri.fromFile(saveIamge);
            // 通知图库更新
            Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
            if(context!=null){
                context.sendBroadcast(scannerIntent);
            }
            success =true;
            outputStream.flush();
            outputStream.close();
            ToastUtil.show("已经保存在："+saveIamge.getAbsolutePath());

        } catch (IOException e)
        {
            Log.e("cap", "");
            e.printStackTrace();
        }
        return  success;
    }
}
