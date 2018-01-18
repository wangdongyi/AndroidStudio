package com.gizwits.opensource.appkit.sharingdevice;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gizwits.gizwifisdk.api.GizDeviceSharing;
import com.gizwits.gizwifisdk.enumration.GizDeviceSharingWay;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizDeviceSharingListener;
import com.gizwits.opensource.appkit.CommonModule.GosBaseActivity;
import com.gizwits.opensource.appkit.R;

import java.util.Timer;
import java.util.TimerTask;

import wdy.business.listen.NoDoubleClickListener;
import wdy.business.util.StatusBarUtil;

public class twoSharedActivity extends GosBaseActivity {

    private String productname;
    private String did;
    private ImageView myimage;
    private TextView timeout;
    private TextView bottomtext;
    private int time = 15;
    private AppCompatImageView imageView_back;
    private TextView textView_title;
    private String share = "http://www.gizwits.com?product_key=e112876cb8514bd88c1365b45b40a99d&did=ZuZoBhguKx6AC4mBxFKx6T&passcode=123456";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gos_two_shared);

        setActionBar(true, true, R.string.scan_code_sharing);
        StatusBarUtil.setStatusBarDark(getWindow(), false);
        initData();
        initView();
    }

    private void initView() {
        TextView usersharedtext = (TextView) findViewById(R.id.usersharedtext);

        myimage = (ImageView) findViewById(R.id.myimageview);

        timeout = (TextView) findViewById(R.id.timeout);

        timeout2 = splits[0] + time + splits[1];

        timeout.setText(timeout2);

        bottomtext = (TextView) findViewById(R.id.bottomtext);

        usersharedtext.setText(getResources().getString(R.string.shared) + "   " + productname
                + getResources().getString(R.string.friends));
        imageView_back = (AppCompatImageView) findViewById(R.id.imageView_back);
        imageView_back.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                onBackPressed();
            }
        });
        textView_title = (TextView) findViewById(R.id.textView_title);
        textView_title.setText("设备分享");
    }

    private void initData() {

        Intent tent = getIntent();
        productname = tent.getStringExtra("productname");
        did = tent.getStringExtra("did");

        timeout2 = getResources().getString(R.string.zxingtimeout);
        splits = timeout2.split("15");
        GizDeviceSharing.sharingDevice(spf.getString("Token", ""), did, GizDeviceSharingWay.GizDeviceSharingByQRCode,
                null, null);

    }

    private void startTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {

                time = time - 1;
                hand.sendEmptyMessage(1);
            }
        }, 60000, 60000);
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (time > 0) {
//            timeout2 = splits[0] + time + splits[1];
//
//            timeout.setText(timeout2);
//        } else {
//            timeout.setText(getResources().getString(R.string.twofailed));
//        }
        GizDeviceSharing.setListener(new GizDeviceSharingListener() {

            @Override
            public void didSharingDevice(GizWifiErrorCode result, String deviceID, int sharingID,
                                         Bitmap QRCodeImage) {
                super.didSharingDevice(result, deviceID, sharingID, QRCodeImage);

                if (QRCodeImage != null) {
                    myimage.setImageBitmap(QRCodeImage);
                    bottomtext.setVisibility(View.VISIBLE);

                    // hand.sendEmptyMessageDelayed(1, 60000);
                    startTimer();
                } else {
                    int errorcode = result.ordinal();

                    if (8041 <= errorcode && errorcode <= 8050 || errorcode == 8308) {

                        timeout.setText(getResources().getString(R.string.twosharedtimeout));
                        bottomtext.setVisibility(View.GONE);

                    } else {
                        timeout.setText(getResources().getString(R.string.sharedfailed));
                        bottomtext.setVisibility(View.GONE);
                    }
                }

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    Handler hand = new Handler() {
        public void handleMessage(Message msg) {

            // time = time - 1;

            Log.e("ssssssss", "sssssss" + time);
            if (time > 0) {
                timeout2 = splits[0] + time + splits[1];

                timeout.setText(timeout2);
                // hand.sendEmptyMessageDelayed(1, 60000);
            } else {
                timeout.setText(getResources().getString(R.string.twofailed));
            }

        }

        ;
    };

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        time = 15;

        hand.removeMessages(1);
        if (timer != null) {
            timer.cancel();
        }

    }

    private String timeout2;
    private String[] splits;
    private Timer timer;
}
