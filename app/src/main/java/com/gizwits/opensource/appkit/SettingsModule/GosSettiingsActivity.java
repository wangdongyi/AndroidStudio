package com.gizwits.opensource.appkit.SettingsModule;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gizwits.opensource.appkit.CommonModule.GosBaseActivity;
import com.gizwits.opensource.appkit.DeviceModule.GosDeviceListActivity;
import com.gizwits.opensource.appkit.DeviceModule.GosMainActivity;
import com.gizwits.opensource.appkit.PushModule.GosPushManager;
import com.gizwits.opensource.appkit.R;
import com.gizwits.opensource.appkit.UserModule.GosChangeUserPasswordActivity;
import com.gizwits.opensource.appkit.UserModule.GosUserLoginActivity;
import com.gizwits.opensource.appkit.UserModule.GosUserManager;
import com.gizwits.opensource.appkit.sharingdevice.SharedDeviceListAcitivity;

import wdy.business.listen.NoDoubleClickListener;

public class GosSettiingsActivity extends GosBaseActivity implements OnClickListener {

    private static final int SETTINGS = 123;
    /**
     * The ll About
     */
    private LinearLayout llAbout;

    /**
     * The Intent
     */
    Intent intent;

    private LinearLayout usermanager;

    private LinearLayout lllogin;

    private TextView phoneusername;
    private LinearLayout deviceshared;
    private LinearLayout llpassword;
    private LinearLayout llfeetback;
    private Button btnRrgister;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gos_settings);
        // 设置ActionBar
        // setActionBar(true, true, R.string.personal_center);

        initView();
        initEvent();
    }

    private void initView() {
        llAbout = (LinearLayout) findViewById(R.id.llAbout);

        usermanager = (LinearLayout) findViewById(R.id.usermanager);

        lllogin = (LinearLayout) findViewById(R.id.lllogin);

        deviceshared = (LinearLayout) findViewById(R.id.deviceshared);

        phoneusername = (TextView) findViewById(R.id.phoneusername);

        if (!TextUtils.isEmpty(spf.getString("UserName", "")) && !TextUtils.isEmpty(spf.getString("PassWord", ""))) {
            usermanager.setVisibility(View.GONE);

            lllogin.setVisibility(View.GONE);
            phoneusername.setText(spf.getString("UserName", ""));
        } else if (TextUtils.isEmpty(spf.getString("UserName", "")) && TextUtils.isEmpty(spf.getString("PassWord", ""))
                && !TextUtils.isEmpty(spf.getString("thirdUid", ""))) {

            usermanager.setVisibility(View.GONE);
            String uid = spf.getString("thirdUid", "");

            lllogin.setVisibility(View.GONE);
            String myuid = uid.substring(0, 2) + "***" + uid.substring(uid.length() - 4, uid.length());
            phoneusername.setText(myuid);
        } else {
            usermanager.setVisibility(View.GONE);
            lllogin.setVisibility(View.VISIBLE);
        }
        llpassword = (LinearLayout) findViewById(R.id.llpassword);
        llpassword.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                Intent tent = new Intent(GosSettiingsActivity.this, GosChangeUserPasswordActivity.class);
                startActivity(tent);
            }
        });
        llfeetback = (LinearLayout) findViewById(R.id.llfeetback);
        llfeetback.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {

            }
        });
        btnRrgister = (Button) findViewById(R.id.btnRrgister);
        btnRrgister.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                showAlertDialog("是否退出登录", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        userlogout();
                    }
                });
            }
        });
    }

    private void initEvent() {
        llAbout.setOnClickListener(this);
        usermanager.setOnClickListener(this);
        lllogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llAbout:
                intent = new Intent(GosSettiingsActivity.this, GosAboutActivity.class);
                startActivity(intent);
                llAbout.setEnabled(false);
                llAbout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        llAbout.setEnabled(true);
                    }
                }, 1000);
                break;

            case R.id.usermanager:
                intent = new Intent(GosSettiingsActivity.this, GosUserManager.class);
                startActivityForResult(intent, SETTINGS);

                usermanager.setEnabled(false);
                usermanager.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        usermanager.setEnabled(true);
                    }
                }, 1000);

                break;

            case R.id.lllogin:
                setResult(SETTINGS);
                finish();
                break;

            default:
                break;
            case R.id.btnRrgister:
                break;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 234) {
            setResult(SETTINGS);
            finish();
        }
    }

    // 设备共享
    public void deviceshared(View v) {

        Intent deviceshared1 = new Intent(this, SharedDeviceListAcitivity.class);

        startActivity(deviceshared1);

        deviceshared.setEnabled(false);
        deviceshared.postDelayed(new Runnable() {
            @Override
            public void run() {
                deviceshared.setEnabled(true);
            }
        }, 1000);

    }

    public void userlogout() {
        logoutToClean();
        Intent intent = new Intent(GosSettiingsActivity.this, GosUserLoginActivity.class);
        startActivity(intent);
        finish();
        if (GosMainActivity.instance != null) {
            GosMainActivity.instance.finish();
        }
    }

    private void logoutToClean() {
        GosPushManager.pushUnBindService(spf.getString("Token", ""));
        spf.edit().putString("UserName", "").commit();
        isclean = true;
        spf.edit().putString("PassWord", "").commit();
        spf.edit().putString("Uid", "").commit();
        spf.edit().putString("Token", "").commit();

        spf.edit().putString("thirdUid", "").commit();

        if (GosDeviceListActivity.loginStatus == 1) {
            GosDeviceListActivity.loginStatus = 0;
        } else {
            GosDeviceListActivity.loginStatus = 4;
        }

    }
    //提示框
    public void showAlertDialog(String msg, DialogInterface.OnClickListener listener) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("标题")
                .setMessage(msg)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定",listener)
                .create();
        dialog.show();
    }
}
