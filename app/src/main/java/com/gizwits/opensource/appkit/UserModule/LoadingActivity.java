package com.gizwits.opensource.appkit.UserModule;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.opensource.appkit.CommonModule.GosBaseActivity;
import com.gizwits.opensource.appkit.DeviceModule.GosDeviceListActivity;
import com.gizwits.opensource.appkit.DeviceModule.GosMainActivity;
import com.gizwits.opensource.appkit.PushModule.GosPushManager;
import com.gizwits.opensource.appkit.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import wdy.business.util.NetUtil;
import wdy.business.util.StatusBarUtil;

/**
 * 作者：王东一
 * 创建时间：2017/12/22.
 */

public class LoadingActivity extends GosUserModuleBaseActivity {
    private RelativeLayout main;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        initView();

    }
    private void initView() {
        main = (RelativeLayout) findViewById(R.id.main);
        StatusBarUtil.setStatusBarDark(getWindow(), true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                getTime();
            }
        }).start();
    }

    private void getTime() {
        autoLogin();
//        if (NetUtil.getNetWorkType(this) != 0) {
//            URL url = null;//取得资源对象
//            try {
//                url = new URL("http://www.baidu.com");
//                URLConnection uc = url.openConnection();//生成连接对象
//                uc.connect(); //发出连接
//                final long ld = uc.getDate(); //取得网站日期时间
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (ld < NetUtil.getTime("2018-02-01 00:00:00")) {
//                            autoLogin();
//                        } else {
//                            Toast.makeText(LoadingActivity.this, "试用期已过", Toast.LENGTH_LONG).show();
//                            finish();
//                        }
//                    }
//                });
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    if (System.currentTimeMillis() < NetUtil.getTime("2018-02-01 00:00:00")) {
//                        autoLogin();
//                    } else {
//                        Toast.makeText(LoadingActivity.this, "试用期已过", Toast.LENGTH_LONG).show();
//                        finish();
//                    }
//                }
//            });
//        }

    }

    /**
     * 与WXEntryActivity共用Handler
     */
    @SuppressLint("HandlerLeak")
    private Handler baseHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            GosUserLoginActivity.handler_key key = GosUserLoginActivity.handler_key.values()[msg.what];
            switch (key) {
                // 登录
                case LOGIN:
                    Intent intent = new Intent(LoadingActivity.this, GosUserLoginActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                // 自动登录
                case AUTO_LOGIN:
                    progressDialog.show();
                    GosDeviceListActivity.loginStatus = 0;
                    GizWifiSDK.sharedInstance().userLogin(spf.getString("UserName", ""), spf.getString("PassWord", ""));
                    break;

            }
        }


    };

    private void autoLogin() {
        if (TextUtils.isEmpty(spf.getString("UserName", "")) || TextUtils.isEmpty(spf.getString("PassWord", ""))) {
            baseHandler.sendEmptyMessageDelayed(GosUserLoginActivity.handler_key.LOGIN.ordinal(), 1000);
        } else {
            baseHandler.sendEmptyMessageDelayed(GosUserLoginActivity.handler_key.AUTO_LOGIN.ordinal(), 1000);
        }
    }

    /**
     * 用户登录回调
     */
    @Override
    protected void didUserLogin(GizWifiErrorCode result, String uid, String token) {
        progressDialog.cancel();
        Log.i("Apptest", GosDeviceListActivity.loginStatus + "\t" + "User");
        if (GosDeviceListActivity.loginStatus == 4 || GosDeviceListActivity.loginStatus == 3) {
            return;
        }
        Log.i("Apptest", GosDeviceListActivity.loginStatus + "\t" + "UserLogin");

        if (GizWifiErrorCode.GIZ_SDK_SUCCESS != result) {// 登录失败
            Toast.makeText(LoadingActivity.this, toastError(result), Toast.LENGTH_LONG).show();

        } else {// 登录成功
            GosDeviceListActivity.loginStatus = 1;
            // 绑定推送
            GosPushManager.pushBindService(token);
            Intent intent = new Intent(LoadingActivity.this, GosMainActivity.class);
            intent.putExtra("ThredLogin", true);
            startActivity(intent);
            finish();
        }
    }
}
