package my.easycommunity.utill;

import android.content.Context;
import android.widget.Toast;
import my.easycommunity.common.MyApplication;

/**
 * Created by Administrator on 2017/3/26.
 */

public class ToastUtil
{
    private Context context;

    public static void show(Object str)
    {
        Toast.makeText(MyApplication.context, str+"",Toast.LENGTH_SHORT).show();
    }
    public static void showLong(Object str)
    {
     Toast.makeText(MyApplication.context, str +"", Toast.LENGTH_LONG).show();
    }

}
