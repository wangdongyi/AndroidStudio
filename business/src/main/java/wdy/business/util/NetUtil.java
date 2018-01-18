package wdy.business.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.WIFI_SERVICE;

/**
 * 作者：王东一
 * 创建时间：2017/12/27.
 */

public class NetUtil {
    /**
     * 没有网络
     */
    public static final int NETWORKTYPE_INVALID = 0;
    /**
     * 手机网络
     */
    public static final int NETWORKTYPE_PHONE = 1;
    /**
     * wifi网络
     */
    public static final int NETWORKTYPE_WIFI = 2;

    // 得到本机Mac地址
    @SuppressLint("HardwareIds")
    public static String getLocalMac(Context mContext) {
        String mac = "";
        // 获取wifi管理器
        WifiManager wifiMng = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfor = wifiMng.getConnectionInfo();
        mac = "本机的mac地址是：" + wifiInfor.getMacAddress();
        return mac;
    }

    //获取当前网路类型
    public static int getNetWorkType(Context context) {
        int mNetWorkType = 0;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();
            if (type.equalsIgnoreCase("WIFI")) {
                mNetWorkType = NETWORKTYPE_WIFI;
            } else if (type.equalsIgnoreCase("MOBILE")) {
                mNetWorkType = NETWORKTYPE_PHONE;
            }
        } else {
            mNetWorkType = NETWORKTYPE_INVALID;
        }
        return mNetWorkType;
    }

    public static String getConnectWifiName(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager != null ? wifiManager.getConnectionInfo() : null;
        String name = "";
        assert wifiInfo != null;
        if (!CodeUtil.isEmpty(wifiInfo.getSSID())) {
            name = wifiInfo.getSSID();
            if (name.indexOf("\"") == 0) name = name.substring(1, name.length());   //去掉第一个 "
            if (name.lastIndexOf("\"") == (name.length() - 1))
                name = name.substring(0, name.length() - 1);  //去掉最后一个 "
        }
        if (CodeUtil.isEmpty(name)) {
            name = "";
        }
        return name;
    }
    //字符串转时间戳
    @SuppressLint("SimpleDateFormat")
    public static long getTime(String timeString) {
        long l = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date d;
        try {
            d = sdf.parse(timeString);
            l = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return l;
    }
}
