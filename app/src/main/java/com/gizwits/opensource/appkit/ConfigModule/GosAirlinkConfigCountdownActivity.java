package com.gizwits.opensource.appkit.ConfigModule;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.czp.library.ArcProgress;
import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizWifiConfigureMode;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.enumration.GizWifiGAgentType;
import com.gizwits.opensource.appkit.R;
import com.gizwits.opensource.appkit.view.RoundProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import wdy.business.listen.NoDoubleClickListener;
import wdy.business.util.CodeUtil;
import wdy.business.util.StatusBarUtil;

@SuppressLint("HandlerLeak")
public class GosAirlinkConfigCountdownActivity extends GosConfigModuleBaseActivity {

    /**
     * The tv Time
     */
    private TextView tvTimer;

    /**
     * The rpb Config
     */
    private RoundProgressBar rpbConfig;

    /**
     * 倒计时
     */
    int secondleft = 60;

    /**
     * The timer
     */
    Timer timer;

    /**
     * 配置用参数
     */
    String workSSID, workSSIDPsw;

    /**
     * The String
     */
    String timerText;

    List<GizWifiGAgentType> modeList, modeDataList;
    private ArcProgress myProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gos_airlink_config_countdown);
        // 设置ActionBar
//        setActionBar(false, false, R.string.configcountDown_title);
        StatusBarUtil.setStatusBarDark(getWindow(), false);
        initView();
        initData();
        startAirlink();

    }

    private void initView() {
        WindowManager wm = this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        AppCompatImageView back = findViewById(R.id.imageView_back);
        back.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                finish();
            }
        });
        TextView title = findViewById(R.id.textView_title);
        title.setText(R.string.configcountDown_title);
        RelativeLayout cel_layout = (RelativeLayout) findViewById(R.id.params);
        LayoutParams params = cel_layout.getLayoutParams();
        params.height = width;
        params.width = width;
        cel_layout.setLayoutParams(params);
        tvTimer = (TextView) findViewById(R.id.tvTimer);

        rpbConfig = (RoundProgressBar) findViewById(R.id.rpbConfig);

        myProgress = (ArcProgress) findViewById(R.id.myProgress);
        myProgress.setOnCenterDraw(new ArcProgress.OnCenterDraw() {
            @Override
            public void draw(Canvas canvas, RectF rectF, float x, float y, float storkeWidth, int progress) {
                Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                textPaint.setStrokeWidth(35);
                textPaint.setTextSize(CodeUtil.dip2px(GosAirlinkConfigCountdownActivity.this,20));
                textPaint.setColor(getResources().getColor(R.color.top_color));
                String progressStr = String.valueOf(progress+"%");
                float textX = x-(textPaint.measureText(progressStr)/2);
                float textY = y-((textPaint.descent()+textPaint.ascent())/2);
                canvas.drawText(progressStr,textX,textY,textPaint);
            }
        });
    }

    private void initData() {
        workSSID = spf.getString("workSSID", "");
        workSSIDPsw = spf.getString("workSSIDPsw", "");
        modeDataList = new ArrayList<GizWifiGAgentType>();
        modeDataList.add(GizWifiGAgentType.GizGAgentESP);
        modeDataList.add(GizWifiGAgentType.GizGAgentMXCHIP);
        modeDataList.add(GizWifiGAgentType.GizGAgentHF);
        modeDataList.add(GizWifiGAgentType.GizGAgentRTK);
        modeDataList.add(GizWifiGAgentType.GizGAgentWM);
        modeDataList.add(GizWifiGAgentType.GizGAgentQCA);
        modeDataList.add(GizWifiGAgentType.GizGAgentTI);
        modeDataList.add(GizWifiGAgentType.GizGAgentFSK);
        modeDataList.add(GizWifiGAgentType.GizGAgentMXCHIP3);
        modeDataList.add(GizWifiGAgentType.GizGAgentBL);
        modeDataList.add(GizWifiGAgentType.GizGAgentAtmelEE);
        modeDataList.add(GizWifiGAgentType.GizGAgentOther);
        modeList = new ArrayList<GizWifiGAgentType>();

        modeList.add(modeDataList
                .get(GosAirlinkChooseDeviceWorkWiFiActivity.modeNum));

    }

    private void startAirlink() {
        GizWifiSDK.sharedInstance().setDeviceOnboarding(workSSID, workSSIDPsw,
                GizWifiConfigureMode.GizWifiAirLink, null, 60, modeList);
        handler.sendEmptyMessage(handler_key.START_TIMER.ordinal());

    }

    private enum handler_key {

        /**
         * 倒计时提示
         */
        TIMER_TEXT,

        /**
         * 倒计时开始
         */
        START_TIMER,

        /**
         * 配置成功
         */
        SUCCESSFUL,

        /**
         * 配置失败
         */
        FAILED,

    }

    /**
     * The handler.
     */
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handler_key key = handler_key.values()[msg.what];
            switch (key) {
                case TIMER_TEXT:

                    tvTimer.setText(timerText);
                    break;

                case START_TIMER:
                    isStartTimer();
                    break;

                case SUCCESSFUL:
                    Toast.makeText(GosAirlinkConfigCountdownActivity.this,
                            R.string.configuration_successful, toastTime).show();
                    finish();
                    break;

                case FAILED:
                    Toast.makeText(GosAirlinkConfigCountdownActivity.this,
                            msg.obj.toString(), toastTime).show();
                    Intent intent = new Intent(
                            GosAirlinkConfigCountdownActivity.this,
                            GosDeviceReadyActivity.class);
                    startActivity(intent);
                    finish();
                    break;

                default:
                    break;

            }
        }

    };

    // 屏蔽掉返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            quitAlert(this, timer);
            return true;
        }
        return false;
    }

    // 倒计时
    public void isStartTimer() {

        secondleft = 60;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                secondleft--;
                rpbConfig.setProgress((60 - secondleft) * (100 / 60.0));
                myProgress.setProgress((int) ((60 - secondleft) * (100 / 60.0)));
                if (secondleft == 58) {
                    timerText = (String) getText(R.string.searching_device);
                    handler.sendEmptyMessage(handler_key.TIMER_TEXT.ordinal());
                } else if (secondleft == 30) {
                    timerText = (String) getText(R.string.searched_device);
                    handler.sendEmptyMessage(handler_key.TIMER_TEXT.ordinal());
                } else if (secondleft == 28) {
                    timerText = (String) getText(R.string.trying_join_device);
                    handler.sendEmptyMessage(handler_key.TIMER_TEXT.ordinal());
                }
            }
        }, 1000, 1000);
    }

    /**
     * 设备配置回调
     *
     * @param result     错误码
     * @param mac        MAC
     * @param did        DID
     * @param productKey PK
     */
    protected void didSetDeviceOnboarding(GizWifiErrorCode result, String mac,
                                          String did, String productKey) {
        if (GizWifiErrorCode.GIZ_SDK_DEVICE_CONFIG_IS_RUNNING == result) {
            return;
        }

        if (timer != null) {
            timer.cancel();
        }
        Message message = new Message();
        if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
            message.what = handler_key.SUCCESSFUL.ordinal();
        } else {
            message.what = handler_key.FAILED.ordinal();
            message.obj = toastError(result);
        }
        Log.i("Apptest", result.toString());
        handler.sendMessage(message);
    }
}
