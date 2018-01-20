package com.gizwits.opensource.appkit.ControlModule;

import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.gizwits.opensource.appkit.R;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 作者：王东一
 * 创建时间：2018/1/19.
 */

public class NotificationUtil {
    private static NotificationUtil mInstance;
    private NotificationManager mNotificationManager;

    static NotificationUtil getInstance(Context context) {
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

    void sendNotification(Context context, int type) {
        if (type == 0) {
            return;
        }
        if (!isNotificationEnabled(context)) {
            Toast.makeText(context, "通知权限 未开启", Toast.LENGTH_LONG).show();
            requestPermission(context);
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

    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

    @SuppressLint("NewApi")
    public static boolean isNotificationEnabled(Context context) {
        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;
        Class appOpsClass = null;
      /* Context.APP_OPS_MANAGER */
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE, String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }
    protected void requestPermission(Context context) {
        // 6.0以上系统才可以判断权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE) {
            // 进入设置系统应用权限界面
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            context.startActivity(intent);
            return;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {// 运行系统在5.x环境使用
            // 进入设置系统应用权限界面
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            context.startActivity(intent);
            return;
        }
        return;
    }
}
