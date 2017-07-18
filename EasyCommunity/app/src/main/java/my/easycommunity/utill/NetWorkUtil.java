package my.easycommunity.utill;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by Administrator on 2017/7/18.
 */
public class NetWorkUtil {
    public static boolean networkCanUse(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = connectivityManager.getActiveNetworkInfo();
        return netinfo != null && netinfo.isConnected();
    }

    public static boolean checkWifiConnection(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connMgr.getNetworkInfo(1);
        return wifi.isAvailable();
    }

    public static boolean checkNetworkConnection(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connMgr.getNetworkInfo(1);
        NetworkInfo mobile = connMgr.getNetworkInfo(0);
        return wifi.isAvailable() || mobile.isAvailable();
    }

    public static String getLocalIpAddress() {
        try {
            Enumeration ex = NetworkInterface.getNetworkInterfaces();

            while(ex.hasMoreElements()) {
                NetworkInterface intf = (NetworkInterface)ex.nextElement();
                Enumeration enumIpAddr = intf.getInetAddresses();

                while(enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress)enumIpAddr.nextElement();
                    if(!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException var4) {

        }

        return null;
    }

    public static boolean isWifiNetwrokType(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return info != null && info.isAvailable() && info.getTypeName().equalsIgnoreCase("wifi");
    }

    public static String checkNetType(Context context) {
        ConnectivityManager connectMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectMgr.getActiveNetworkInfo();
        String type = "no_network";
        if(info == null) {
            return type;
        } else {
            if(info.getType() == 1) {
                type = "wifi";
            } else {
                switch(info.getSubtype()) {
                    case 0:
                        type = "UNKNOWN";
                        break;
                    case 1:
                        type = "GPRS";
                        break;
                    case 2:
                        type = "EDGE";
                        break;
                    case 3:
                        type = "UMTS";
                        break;
                    case 4:
                        type = "CDMA";
                        break;
                    case 5:
                        type = "EVDO_0";
                        break;
                    case 6:
                        type = "EVDO_A";
                        break;
                    case 7:
                        type = "1xRTT";
                        break;
                    case 8:
                        type = "HSDPA";
                        break;
                    case 9:
                        type = "HSUPA";
                        break;
                    case 10:
                        type = "HSPA";
                        break;
                    case 11:
                        type = "IDEN";
                        break;
                    case 12:
                        type = "EVDO_B";
                        break;
                    case 13:
                        type = "LTE";
                        break;
                    case 14:
                        type = "EHRPD";
                        break;
                    case 15:
                        type = "HSPAP";
                }
            }

            return type;
        }
    }
}
