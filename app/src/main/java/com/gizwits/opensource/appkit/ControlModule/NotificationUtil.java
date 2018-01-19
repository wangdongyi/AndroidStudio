package com.gizwits.opensource.appkit.ControlModule;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.gizwits.opensource.appkit.R;

/**
 * 作者：王东一
 * 创建时间：2018/1/19.
 */

public class NotificationUtil {
    private static NotificationUtil mInstance;
    private NotificationManager mNotificationManager;

    public static NotificationUtil getInstance(Context context) {
        if (mInstance == null) {
            synchronized (NotificationUtil.class) {
                if (mInstance == null) {
                    mInstance = new NotificationUtil(context);
                }
            }
        }
        return mInstance;
    }

    private NotificationUtil(Context context) {
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void sendNotification(Context context, int type) {
        if (type == 0) {
            return;
        }
//        0表示无故障
//        1表示高温报警
//        2表示短路报警
//        3表示断路报警
//        4表示参数设置错误报警
        String content = "";
        switch (type) {
            case 1:
                content = "高温报警";
                break;
            case 2:
                content = "短路报警";
                break;
            case 3:
                content = "断路报警";
                break;
            case 4:
                content = "参数设置错误报警";
                break;
        }
        //震动也有两种设置方法,与设置铃声一样,在此不再赘述
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("警告")
                .setContentText(content)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        mNotificationManager.notify(3, builder.build());
    }
}
