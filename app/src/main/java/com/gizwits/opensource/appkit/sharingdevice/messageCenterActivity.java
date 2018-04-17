package com.gizwits.opensource.appkit.sharingdevice;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gizwits.gizwifisdk.api.GizDeviceSharing;
import com.gizwits.gizwifisdk.api.GizMessage;
import com.gizwits.gizwifisdk.enumration.GizMessageStatus;
import com.gizwits.gizwifisdk.enumration.GizMessageType;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizDeviceSharingListener;
import com.gizwits.opensource.appkit.CommonModule.GosBaseActivity;
import com.gizwits.opensource.appkit.R;

import java.util.List;

import wdy.business.listen.NoDoubleClickListener;

public class messageCenterActivity extends GosBaseActivity {

    private View redpoint;
    private LinearLayout gizwitsmes;
    private LinearLayout deviceshared;
    private ImageView icon1;
    private ImageView icon2;
    private ImageView icon3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gos_message);

        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        String token = spf.getString("Token", "");
        GizDeviceSharing.queryMessageList(token, GizMessageType.GizMessageSharing);
        //GizDeviceSharing.queryMessageList(token, GizMessageType.GizMessageSystem);

        GizDeviceSharing.setListener(new GizDeviceSharingListener() {

            @Override
            public void didQueryMessageList(GizWifiErrorCode result, List<GizMessage> messageList) {
                super.didQueryMessageList(result, messageList);

                if (messageList.size() > 0) {
                    isShowRedPoint(messageList);
                } else {
                    redpoint.setVisibility(View.GONE);
                }

                if (result.ordinal() != 0) {
                    Toast.makeText(messageCenterActivity.this, toastError(result), 2).show();
                }

            }

        });
    }

    private void isShowRedPoint(List<GizMessage> messageList) {

        boolean isshow = false;
        redpoint.setVisibility(View.GONE);
        for (int i = 0; i < messageList.size(); i++) {

            GizMessage gizMessage = messageList.get(i);
            GizMessageStatus status = gizMessage.getStatus();
            if (status.ordinal() == 0) {
                isshow = true;

                redpoint.setVisibility(View.VISIBLE);
            }

        }
    }

    private void initView() {
        // 判断当前的view 是否需要显示这个红点
        redpoint = findViewById(R.id.redpoint);

        gizwitsmes = (LinearLayout) findViewById(R.id.gizwitsmes);

        deviceshared = (LinearLayout) findViewById(R.id.deviceshared);


        icon1 = (ImageView) findViewById(R.id.icon1);
        icon1.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                Intent intent= new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("https://sybsdq.1688.com");
                intent.setData(content_url);
                startActivity(intent);
            }
        });
        icon2 = (ImageView) findViewById(R.id.icon2);
        icon2.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                Intent intent= new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("http://www.sybsdq.com");
                intent.setData(content_url);
                startActivity(intent);
            }
        });
        icon3 = (ImageView) findViewById(R.id.icon3);
        icon3.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                Intent intent= new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("https://shop591402973.taobao.com");
                intent.setData(content_url);
                startActivity(intent);
            }
        });
    }

    // 跳转到设备分享的界面
    public void deviceshared(View v) {

        Intent tent = new Intent(this, deviceSharedMessageActivity.class);
        startActivity(tent);

        deviceshared.setEnabled(false);
        deviceshared.postDelayed(new Runnable() {
            @Override
            public void run() {
                deviceshared.setEnabled(true);
            }
        }, 1000);

    }

    // 跳转到机智云公告页面
    public void gizwitsmes(View v) {

        Intent intent = new Intent(this, MsgNoticeActivity.class);
        startActivity(intent);

        gizwitsmes.setEnabled(false);
        gizwitsmes.postDelayed(new Runnable() {
            @Override
            public void run() {
                gizwitsmes.setEnabled(true);
            }
        }, 1000);


    }

}
