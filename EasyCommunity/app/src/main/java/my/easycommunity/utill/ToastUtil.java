package my.easycommunity.utill;

import android.content.Context;
import android.widget.Toast;
import com.orhanobut.logger.Logger;
import my.easycommunity.common.MyApplication;

/**
 * Created by Administrator on 2017/3/26.
 */

public class ToastUtil
{
    private Context context;

    public static void show(String str)
    {

        if (str != null && str.trim().length() != 0)
        {
            Toast.makeText(MyApplication.context, str, Toast.LENGTH_SHORT).show();
        } else
        {
            ToastUtil.show("Error>>:  You Showed String is Null!!");
            Logger.e("error for Str>>" + str);
        }

    }
    public static void showLong(String str)
    {
    if (str != null && str.trim().length() != 0)
    {
        Toast.makeText(MyApplication.context, str, Toast.LENGTH_LONG).show();
    } else
    {
        ToastUtil.show("Error>>:  You Showed String is Null!!");
        Logger.e("error for Str>>" + str);
    }
   }
}
