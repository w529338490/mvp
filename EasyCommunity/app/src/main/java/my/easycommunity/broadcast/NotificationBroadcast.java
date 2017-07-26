package my.easycommunity.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.orhanobut.logger.Logger;
import com.umeng.message.MsgConstant;
import com.umeng.message.UTrack;
import com.umeng.message.common.UmLog;
import com.umeng.message.common.UmengMessageDeviceConfig;
import com.umeng.message.entity.UMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/7/27.
 */

public class NotificationBroadcast extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent) {

        Logger.e(intent.getAction() );
    }
}