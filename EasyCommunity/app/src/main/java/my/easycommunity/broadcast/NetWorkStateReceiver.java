package my.easycommunity.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.orhanobut.logger.Logger;
import my.easycommunity.utill.NetWorkUtil;
import my.easycommunity.utill.ToastUtil;

/**
 * Created by Administrator on 2017/7/21.
 *  全局网络状态监听
 */
public class NetWorkStateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.e("================="+this);

        if ( context!=null && !NetWorkUtil.networkCanUse(context)) {
                ToastUtil.show("亲，你已进入无网世界！");

        }
    }
}
