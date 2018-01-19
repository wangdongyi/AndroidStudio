package com.gizwits.opensource.appkit.DeviceModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.gizwits.gizwifisdk.api.GizDeviceSharing;
import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceNetStatus;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizDeviceSharingListener;
import com.gizwits.opensource.appkit.CommonModule.MyGizWifiDevice;
import com.gizwits.opensource.appkit.R;
import com.gizwits.opensource.appkit.CommonModule.GosDeploy;
import com.gizwits.opensource.appkit.CommonModule.TipsDialog;
import com.gizwits.opensource.appkit.ConfigModule.GosAirlinkChooseDeviceWorkWiFiActivity;
import com.gizwits.opensource.appkit.ConfigModule.GosCheckDeviceWorkWiFiActivity;
import com.gizwits.opensource.appkit.ControlModule.GosDeviceControlActivity;
import com.gizwits.opensource.appkit.PushModule.GosPushManager;
import com.gizwits.opensource.appkit.SettingsModule.GosSettiingsActivity;
import com.gizwits.opensource.appkit.UserModule.GosUserLoginActivity;
import com.gizwits.opensource.appkit.sharingdevice.gosZxingDeviceSharingActivity;
import com.gizwits.opensource.appkit.utils.NetUtils;
import com.gizwits.opensource.appkit.view.SlideListView2;
import com.gizwits.opensource.appkit.view.VerticalSwipeRefreshLayout;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import wdy.business.event.MessageEvent;
import zxing.CaptureActivity;

@SuppressLint("HandlerLeak")
public class GosDeviceListActivity extends GosDeviceModuleBaseActivity implements OnClickListener, OnRefreshListener {

    /**
     * The ll NoDevice
     */
    private ScrollView llNoDevice;

    /**
     * The img NoDevice
     */
    private ImageView imgNoDevice;

    /**
     * The btn NoDevice
     */
    private Button btnNoDevice;

    /**
     * The ic BoundDevices
     */
    private View icBoundDevices;

    /**
     * The ic FoundDevices
     */
    private View icFoundDevices;

    /**
     * The ic OfflineDevices
     */
    private View icOfflineDevices;

    /**
     * The tv BoundDevicesListTitle
     */
    private TextView tvBoundDevicesListTitle;

    /**
     * The tv FoundDevicesListTitle
     */
    private TextView tvFoundDevicesListTitle;

    /**
     * The tv OfflineDevicesListTitle
     */
    private TextView tvOfflineDevicesListTitle;

    /**
     * The ll NoBoundDevices
     */
    private LinearLayout llNoBoundDevices;

    /**
     * The ll NoFoundDevices
     */
    private LinearLayout llNoFoundDevices;

    /**
     * The ll NoOfflineDevices
     */
    private LinearLayout llNoOfflineDevices;

    /**
     * The slv BoundDevices
     */
    private SlideListView2 slvBoundDevices;

    /**
     * The slv FoundDevices
     */
    private SlideListView2 slvFoundDevices;

    /**
     * The slv OfflineDevices
     */
    private SlideListView2 slvOfflineDevices;

    /**
     * The sv ListGroup
     */
    private ScrollView svListGroup;

    /**
     * 适配器
     */
    GosDeviceListAdapter myadapter;

    /**
     * 设备列表分类
     */
    List<MyGizWifiDevice> boundDevicesList, foundDevicesList, offlineDevicesList;
    List<MyGizWifiDevice> DevicesList = new ArrayList<>();
    /**
     * 设备热点名称列表
     */
    ArrayList<String> softNameList;

    /**
     * 与APP绑定的设备的ProductKey
     */
    private List<String> ProductKeyList;

    Intent intent;

    String softssid, uid, token;

    boolean isItemClicked = false;

    public static List<String> boundMessage;

    boolean isFrist = true;

    // boolean isLogout = false;
    //
    // public static boolean isAnonymousLoging = false;

    /**
     * 判断用户登录状态 0：未登录 1：实名用户登录 2：匿名用户登录 3：匿名用户登录中 4：匿名用户登录中断
     */
    public static int loginStatus;

    int threeSeconds = 3;

    /**
     * 获取设备列表
     */
    protected static final int GETLIST = 0;

    /**
     * 刷新设备列表
     */
    protected static final int UPDATALIST = 1;

    /**
     * 订阅成功前往控制页面
     */
    protected static final int TOCONTROL = 2;

    /**
     * 通知
     */
    protected static final int TOAST = 3;

    /**
     * 设备绑定
     */
    protected static final int BOUND = 9;

    /**
     * 设备解绑
     */
    protected static final int UNBOUND = 99;

    /**
     * 新设备提醒
     */
    protected static final int SHOWDIALOG = 999;

    private static final int PULL_TO_REFRESH = 888;

    private VerticalSwipeRefreshLayout mSwipeLayout;

    private VerticalSwipeRefreshLayout mSwipeLayout1;

    private static final int REQUEST_CODE_SETTING = 100;

    Handler handler = new Handler() {
        private AlertDialog myDialog;
        private TextView dialog_name;

        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GETLIST:

                    if (!uid.isEmpty() && !token.isEmpty()) {
                        GizWifiSDK.sharedInstance().getBoundDevices(uid, token, ProductKeyList);
                    }

                    if (loginStatus == 0 && GosDeploy.setAnonymousLogin()) {
                        loginStatus = 3;
                        GizWifiSDK.sharedInstance().userLoginAnonymous();
                    }

                    break;

                case UPDATALIST:
                    if (progressDialog.isShowing()) {
                        progressDialog.cancel();
                    }
                    UpdateUI();
                    break;

                case BOUND:

                    break;

                case UNBOUND:
                    progressDialog.show();
                    GizWifiSDK.sharedInstance().unbindDevice(uid, token, msg.obj.toString());
                    break;

                case TOCONTROL:
                    intent = new Intent(GosDeviceListActivity.this, GosDeviceControlActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("GizWifiDevice", (GizWifiDevice) msg.obj);
                    intent.putExtras(bundle);
                    // startActivity(intent);
                    startActivityForResult(intent, 1);
                    break;

                case TOAST:

                    Toast.makeText(GosDeviceListActivity.this, msg.obj.toString(), toastTime).show();
                    break;

                case PULL_TO_REFRESH:
                    handler.sendEmptyMessage(GETLIST);
                    mSwipeLayout.setRefreshing(false);
                    mSwipeLayout1.setRefreshing(false);

                    break;

                case SHOWDIALOG:

                    if (!softNameList.toString()
                            .contains(GosMessageHandler.getSingleInstance().getNewDeviceList().toString())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(GosDeviceListActivity.this);
                        View view = View.inflate(GosDeviceListActivity.this, R.layout.alert_gos_new_device, null);
                        Button diss = (Button) view.findViewById(R.id.diss);
                        Button ok = (Button) view.findViewById(R.id.ok);
                        dialog_name = (TextView) view.findViewById(R.id.dialog_name);
                        String foundOneDevice, foundManyDevices;
                        foundOneDevice = (String) getText(R.string.not_text);
                        foundManyDevices = (String) getText(R.string.found_many_devices);
                        if (GosMessageHandler.getSingleInstance().getNewDeviceList().size() < 1) {
                            return;
                        }
                        if (GosMessageHandler.getSingleInstance().getNewDeviceList().size() == 1) {
                            String ssid = GosMessageHandler.getSingleInstance().getNewDeviceList().get(0);
                            if (!TextUtils.isEmpty(ssid)
                                    && ssid.equalsIgnoreCase(NetUtils.getCurentWifiSSID(GosDeviceListActivity.this))) {
                                return;
                            }
                            if (softNameList.toString().contains(ssid)) {
                                return;
                            }
                            softNameList.add(ssid);
                            dialog_name.setText(ssid + foundOneDevice);
                            softssid = ssid;
                        } else {
                            for (String s : GosMessageHandler.getSingleInstance().getNewDeviceList()) {
                                if (!softNameList.toString().contains(s)) {
                                    softNameList.add(s);
                                }
                            }
                            dialog_name.setText(foundManyDevices);
                        }
                        myDialog = builder.create();
                        Window window = myDialog.getWindow();
                        myDialog.setView(view);
                        myDialog.show();
                        window.setGravity(Gravity.BOTTOM);
                        ok.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (GosMessageHandler.getSingleInstance().getNewDeviceList().size() == 1) {
                                    Intent intent = new Intent(GosDeviceListActivity.this,
                                            GosCheckDeviceWorkWiFiActivity.class);
                                    intent.putExtra("softssid", softssid);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(GosDeviceListActivity.this,
                                            GosCheckDeviceWorkWiFiActivity.class);
                                    startActivity(intent);
                                }
                                myDialog.cancel();
                            }
                        });
                        diss.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myDialog.cancel();
                            }
                        });
                    }
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gos_device_list);
        EventBus.getDefault().register(this);
        handler.sendEmptyMessage(GETLIST);
        softNameList = new ArrayList<String>();
        initData();
        initView();
        initEvent();
    }


	/*
     * @Override public void onWindowFocusChanged(boolean hasFocus) {
	 * super.onWindowFocusChanged(hasFocus); if (hasFocus && isFrist) {
	 * progressDialog.show();
	 * 
	 * isFrist = false; } }
	 */

    @Override
    public void onResume() {
        super.onResume();
        GizDeviceSharing.setListener(new GizDeviceSharingListener() {

            @Override
            public void didCheckDeviceSharingInfoByQRCode(GizWifiErrorCode result, String userName, String productName,
                                                          String deviceAlias, String expiredAt) {
                super.didCheckDeviceSharingInfoByQRCode(result, userName, productName, deviceAlias, expiredAt);
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                int errorcode = result.ordinal();
                if (8041 <= errorcode && errorcode <= 8050 || errorcode == 8308) {
                    Toast.makeText(GosDeviceListActivity.this, getResources().getString(R.string.sorry), toastTime).show();
                } else if (errorcode != 0) {
                    Toast.makeText(GosDeviceListActivity.this, getResources().getString(R.string.verysorry), toastTime).show();
                } else {
                    Intent tent = new Intent(GosDeviceListActivity.this, gosZxingDeviceSharingActivity.class);
                    tent.putExtra("userName", userName);
                    tent.putExtra("productName", productName);
                    tent.putExtra("deviceAlias", deviceAlias);
                    tent.putExtra("expiredAt", expiredAt);
                    tent.putExtra("code", boundMessage.get(2));
                    startActivity(tent);

                }
            }

        });
        GosDeviceModuleBaseActivity.deviceslist = GizWifiSDK.sharedInstance().getDeviceList();
        UpdateUI();
        if (boundMessage.size() != 0) {
            progressDialog.show();
            if (boundMessage.size() == 2) {
                GizWifiSDK.sharedInstance().bindDevice(uid, token, boundMessage.get(0), boundMessage.get(1), null);
            } else if (boundMessage.size() == 1) {
                GizWifiSDK.sharedInstance().bindDeviceByQRCode(uid, token, boundMessage.get(0));
            } else if (boundMessage.size() == 3) {

                GizDeviceSharing.checkDeviceSharingInfoByQRCode(spf.getString("Token", ""), boundMessage.get(2));
            } else {
                Log.i("Apptest", "ListSize:" + boundMessage.size());
            }
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        boundMessage.clear();
    }

    private void initView() {
        svListGroup = (ScrollView) findViewById(R.id.svListGroup);
        llNoDevice = (ScrollView) findViewById(R.id.llNoDevice);
        imgNoDevice = (ImageView) findViewById(R.id.imgNoDevice);
        btnNoDevice = (Button) findViewById(R.id.btnNoDevice);

        icBoundDevices = findViewById(R.id.icBoundDevices);
        icFoundDevices = findViewById(R.id.icFoundDevices);
        icOfflineDevices = findViewById(R.id.icOfflineDevices);

        slvBoundDevices = (SlideListView2) icBoundDevices.findViewById(R.id.slideListView1);
        slvFoundDevices = (SlideListView2) icFoundDevices.findViewById(R.id.slideListView1);
        slvOfflineDevices = (SlideListView2) icOfflineDevices.findViewById(R.id.slideListView1);

        llNoBoundDevices = (LinearLayout) icBoundDevices.findViewById(R.id.llHaveNotDevice);
        llNoFoundDevices = (LinearLayout) icFoundDevices.findViewById(R.id.llHaveNotDevice);
        llNoOfflineDevices = (LinearLayout) icOfflineDevices.findViewById(R.id.llHaveNotDevice);

        tvBoundDevicesListTitle = (TextView) icBoundDevices.findViewById(R.id.tvListViewTitle);
        tvFoundDevicesListTitle = (TextView) icFoundDevices.findViewById(R.id.tvListViewTitle);
        tvOfflineDevicesListTitle = (TextView) icOfflineDevices.findViewById(R.id.tvListViewTitle);

        String boundDevicesListTitle = (String) getText(R.string.bound_divices);
        tvBoundDevicesListTitle.setText(boundDevicesListTitle);
        String foundDevicesListTitle = (String) getText(R.string.found_devices);
        tvFoundDevicesListTitle.setText(foundDevicesListTitle);
        String offlineDevicesListTitle = (String) getText(R.string.offline_devices);
        tvOfflineDevicesListTitle.setText(offlineDevicesListTitle);

        // 下拉刷新

        mSwipeLayout = (VerticalSwipeRefreshLayout) findViewById(R.id.id_swipe_ly);

        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        mSwipeLayout1 = (VerticalSwipeRefreshLayout) findViewById(R.id.id_swipe_ly1);
        mSwipeLayout1.setOnRefreshListener(this);
        mSwipeLayout1.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

    }

    private void initEvent() {

        imgNoDevice.setOnClickListener(this);
        btnNoDevice.setOnClickListener(this);

        slvFoundDevices.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                progressDialog.show();
                slvFoundDevices.setEnabled(false);
                slvFoundDevices.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        slvFoundDevices.setEnabled(true);
                    }
                }, 3000);
                GizWifiDevice device = foundDevicesList.get(position).getGizWifiDevice();
                device.setListener(getGizWifiDeviceListener());

                String productKey = device.getProductKey();
                if (productKey.equals("ac102d79bbb346389e1255eafca0cfd2")) {
                    device.setSubscribe("b83feefa750740f6862380043c0f78fb", true);
                } else {
                    device.setSubscribe(true);
                }

            }
        });

        slvBoundDevices.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                GizWifiDevice device = boundDevicesList.get(position).getGizWifiDevice();
                if (device.getNetStatus() == GizWifiDeviceNetStatus.GizDeviceOffline) {
                    Toast.makeText(GosDeviceListActivity.this, "该设备不在线", Toast.LENGTH_LONG).show();
                } else {
                    progressDialog.show();
                    slvBoundDevices.setEnabled(false);
                    slvBoundDevices.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            slvBoundDevices.setEnabled(true);
                        }
                    }, 3000);
                    device.setListener(getGizWifiDeviceListener());
                    String productKey = device.getProductKey();
                    if (productKey.equals("ac102d79bbb346389e1255eafca0cfd2")) {
                        device.setSubscribe("b83feefa750740f6862380043c0f78fb", true);
                    } else {
                        device.setSubscribe(true);
                    }
                }
            }
        });

        slvBoundDevices.initSlideMode(SlideListView2.MOD_RIGHT);
        slvOfflineDevices.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                progressDialog.show();
                slvOfflineDevices.setEnabled(false);
                slvOfflineDevices.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        slvOfflineDevices.setEnabled(true);
                    }
                }, 3000);
                GizWifiDevice device = offlineDevicesList.get(position).getGizWifiDevice();
                device.setListener(getGizWifiDeviceListener());
                String productKey = device.getProductKey();
                if (productKey.equals("ac102d79bbb346389e1255eafca0cfd2")) {
                    device.setSubscribe("b83feefa750740f6862380043c0f78fb", true);
                } else {
                    device.setSubscribe(true);
                }
            }
        });
        slvOfflineDevices.initSlideMode(SlideListView2.MOD_RIGHT);
    }

    private void initData() {
        boundMessage = new ArrayList<String>();
        ProductKeyList = GosDeploy.setProductKeyList();
        uid = spf.getString("Uid", "");
        token = spf.getString("Token", "");

        if (uid.isEmpty() && token.isEmpty()) {
            loginStatus = 0;
        }
    }

    protected void didDiscovered(GizWifiErrorCode result, java.util.List<GizWifiDevice> deviceList) {
        GosDeviceModuleBaseActivity.deviceslist.clear();
        for (GizWifiDevice gizWifiDevice : deviceList) {
            GosDeviceModuleBaseActivity.deviceslist.add(gizWifiDevice);
        }
        handler.sendEmptyMessage(UPDATALIST);

    }

    protected void didUserLogin(GizWifiErrorCode result, java.lang.String uid, java.lang.String token) {

        if (GizWifiErrorCode.GIZ_SDK_SUCCESS == result) {
            loginStatus = 2;
            this.uid = uid;
            this.token = token;
            spf.edit().putString("Uid", this.uid).commit();
            spf.edit().putString("Token", this.token).commit();
            handler.sendEmptyMessage(GETLIST);
            //  绑定推送
            GosPushManager.pushBindService(token);
        } else {
            loginStatus = 0;
            if (GosDeploy.setAnonymousLogin()) {
                tryUserLoginAnonymous();
            }

        }
    }

    protected void didUnbindDevice(GizWifiErrorCode result, java.lang.String did) {
        progressDialog.cancel();
        if (GizWifiErrorCode.GIZ_SDK_SUCCESS != result) {
            // String unBoundFailed = (String) getText(R.string.unbound_failed);
            Toast.makeText(this, toastError(result), toastTime).show();
        }
    }

    @Override
    protected void didSetSubscribe(GizWifiErrorCode result, GizWifiDevice device, boolean isSubscribed) {
        //  控制页面跳转
        progressDialog.cancel();
        Message msg = new Message();
        if (GizWifiErrorCode.GIZ_SDK_SUCCESS == result) {
            msg.what = TOCONTROL;
            msg.obj = device;
        } else {
            if (device.isBind()) {
                msg.what = TOAST;
                // String setSubscribeFail = (String)
                // getText(R.string.setsubscribe_failed);
                msg.obj = toastError(result);// setSubscribeFail + "\n" + arg0;
            }
        }
        handler.sendMessage(msg);
    }

    /**
     * 推送绑定回调
     *
     * @param result
     */
    @Override
    protected void didChannelIDBind(GizWifiErrorCode result) {
        Log.i("Apptest", result.toString());
        if (GizWifiErrorCode.GIZ_SDK_SUCCESS != result) {
            Toast.makeText(this, toastError(result), toastTime).show();
        }
    }

    /**
     * 设备绑定回调(旧)
     *
     * @param error
     * @param errorMessage
     * @param did
     */
    protected void didBindDevice(int error, String errorMessage, String did) {
        progressDialog.cancel();
        if (error != 0) {
            String toast = getResources().getString(R.string.bound_failed) + "\n" + errorMessage;
            Toast.makeText(this, toast, toastTime).show();
        } else {
            Toast.makeText(this, R.string.bound_successful, toastTime).show();
        }
    }

    /**
     * 设备绑定回调
     *
     * @param result
     * @param did
     */
    protected void didBindDevice(GizWifiErrorCode result, java.lang.String did) {
        progressDialog.cancel();
        if (result != GizWifiErrorCode.GIZ_SDK_SUCCESS) {
            Toast.makeText(this, toastError(result), toastTime).show();
        } else {
            Toast.makeText(this, R.string.add_successful, toastTime).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!TextUtils.isEmpty(spf.getString("UserName", "")) && !TextUtils.isEmpty(spf.getString("PassWord", ""))) {
            getMenuInflater().inflate(R.menu.devicelist_logout, menu);
        } else {
            if (getIntent().getBooleanExtra("ThredLogin", false)) {
                getMenuInflater().inflate(R.menu.devicelist_logout, menu);
            } else {
                getMenuInflater().inflate(R.menu.devicelist_login, menu);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                if (checkNetwork(GosDeviceListActivity.this)) {
                    progressDialog.show();
                    handler.sendEmptyMessage(GETLIST);
                }
                break;
            case R.id.action_QR_code:
                intent = new Intent(GosDeviceListActivity.this, CaptureActivity.class);
                startActivity(intent);
                break;
            case R.id.action_addDevice:
                if (!checkNetwork(GosDeviceListActivity.this)) {
                    Toast.makeText(GosDeviceListActivity.this, R.string.network_error, toastTime).show();
                } else {
                    intent = new Intent(GosDeviceListActivity.this, GosAirlinkChooseDeviceWorkWiFiActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.action_site:
                intent = new Intent(GosDeviceListActivity.this, GosSettiingsActivity.class);
                startActivityForResult(intent, 600);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void UpdateUI() {
        if (GosDeviceModuleBaseActivity.deviceslist.isEmpty()) {
            svListGroup.setVisibility(View.GONE);
            llNoDevice.setVisibility(View.VISIBLE);
            mSwipeLayout1.setVisibility(View.VISIBLE);
            return;
        } else {
            llNoDevice.setVisibility(View.GONE);
            mSwipeLayout1.setVisibility(View.GONE);
            svListGroup.setVisibility(View.VISIBLE);
        }

        boundDevicesList = new ArrayList<MyGizWifiDevice>();
        foundDevicesList = new ArrayList<MyGizWifiDevice>();
        offlineDevicesList = new ArrayList<MyGizWifiDevice>();

        for (GizWifiDevice gizWifiDevice : GosDeviceModuleBaseActivity.deviceslist) {
            if (GizWifiDeviceNetStatus.GizDeviceOnline == gizWifiDevice.getNetStatus()
                    || GizWifiDeviceNetStatus.GizDeviceControlled == gizWifiDevice.getNetStatus()) {
                MyGizWifiDevice myGizWifiDevice = new MyGizWifiDevice();
                if (gizWifiDevice.isBind()) {
                    for (int i = 0; i < DevicesList.size(); i++) {
                        if (DevicesList.get(i).getGizWifiDevice().getDid().equals(gizWifiDevice.getDid())) {
                            myGizWifiDevice.setWaring(DevicesList.get(i).getWaring());
                            myGizWifiDevice.setOpen(DevicesList.get(i).isOpen());
                        }
                    }
                    myGizWifiDevice.setGizWifiDevice(gizWifiDevice);
                    boundDevicesList.add(myGizWifiDevice);
                } else {
                    myGizWifiDevice.setGizWifiDevice(gizWifiDevice);
                    foundDevicesList.add(myGizWifiDevice);
                }
            } else {
                MyGizWifiDevice myGizWifiDevice = new MyGizWifiDevice();
                for (int i = 0; i < DevicesList.size(); i++) {
                    if (DevicesList.get(i).getGizWifiDevice().getDid().equals(gizWifiDevice.getDid())) {
                        myGizWifiDevice.setWaring(DevicesList.get(i).getWaring());
                        myGizWifiDevice.setOpen(DevicesList.get(i).isOpen());
                    }
                }
                myGizWifiDevice.setGizWifiDevice(gizWifiDevice);
                boundDevicesList.add(myGizWifiDevice);
//				offlineDevicesList.add(gizWifiDevice);
            }
        }

        if (boundDevicesList.isEmpty()) {
            slvBoundDevices.setVisibility(View.GONE);
            llNoBoundDevices.setVisibility(View.VISIBLE);
        } else {
            myadapter = new GosDeviceListAdapter(this, boundDevicesList);
            myadapter.setHandler(handler);
            slvBoundDevices.setAdapter(myadapter);
            llNoBoundDevices.setVisibility(View.GONE);
            slvBoundDevices.setVisibility(View.VISIBLE);
            myadapter.setOnclickRename(new Holder.OnclickRename() {
                @Override
                public void click(GizWifiDevice device) {
                    setDeviceInfo(device);
                }
            });
        }

        if (foundDevicesList.isEmpty()) {
            slvFoundDevices.setVisibility(View.GONE);
            llNoFoundDevices.setVisibility(View.VISIBLE);
        } else {
            myadapter = new GosDeviceListAdapter(this, foundDevicesList);
            slvFoundDevices.setAdapter(myadapter);
            llNoFoundDevices.setVisibility(View.GONE);
            slvFoundDevices.setVisibility(View.VISIBLE);
        }

        if (offlineDevicesList.isEmpty()) {
            slvOfflineDevices.setVisibility(View.GONE);
            llNoOfflineDevices.setVisibility(View.VISIBLE);
        } else {
            myadapter = new GosDeviceListAdapter(this, offlineDevicesList);
            myadapter.setHandler(handler);
            slvOfflineDevices.setAdapter(myadapter);
            llNoOfflineDevices.setVisibility(View.GONE);
            slvOfflineDevices.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNoDevice:
                if (!checkNetwork(GosDeviceListActivity.this)) {
                    Toast.makeText(GosDeviceListActivity.this, R.string.network_error, toastTime).show();
                    return;
                }
                intent = new Intent(GosDeviceListActivity.this, GosAirlinkChooseDeviceWorkWiFiActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    private void tryUserLoginAnonymous() {
        threeSeconds = 3;
        final Timer tsTimer = new Timer();
        tsTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                threeSeconds--;
                if (threeSeconds <= 0) {
                    tsTimer.cancel();
                    handler.sendEmptyMessage(GETLIST);
                } else {
                    if (loginStatus == 4) {
                        tsTimer.cancel();
                    }
                }
            }
        }, 1000, 1000);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 666) {
            finish();
        } else if (resultCode == 98765) {
            TipsDialog dialog = new TipsDialog(GosDeviceListActivity.this,
                    getResources().getString(R.string.devicedisconnected));
            dialog.show();
        }
    }

    public Handler getMyHandler() {
        return handler;
    }

    @Override
    public void onRefresh() {
        handler.sendEmptyMessageDelayed(PULL_TO_REFRESH, toastTime);
    }

    /**
     * Description:设置设备别名与备注
     */
    private void setDeviceInfo(final GizWifiDevice mDevice) {

        final Dialog mDialog = new AlertDialog.Builder(GosDeviceListActivity.this).setView(new EditText(GosDeviceListActivity.this)).create();
        mDialog.show();

        Window window = mDialog.getWindow();
        window.setContentView(R.layout.alert_gos_set_device_info);

        final EditText etAlias;
        final EditText etRemark;
        etAlias = (EditText) window.findViewById(R.id.etAlias);
        etRemark = (EditText) window.findViewById(R.id.etRemark);

        LinearLayout llNo, llSure;
        llNo = (LinearLayout) window.findViewById(R.id.llNo);
        llSure = (LinearLayout) window.findViewById(R.id.llSure);

        if (!TextUtils.isEmpty(mDevice.getAlias())) {
            setEditText(etAlias, mDevice.getAlias());
        }
        if (!TextUtils.isEmpty(mDevice.getRemark())) {
            setEditText(etRemark, mDevice.getRemark());
        }

        llNo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        llSure.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etRemark.getText().toString()) && TextUtils.isEmpty(etAlias.getText().toString())) {
                    Toast.makeText(GosDeviceListActivity.this, "请输入设备别名或备注！", Toast.LENGTH_SHORT).show();
                    return;
                }
                mDevice.setCustomInfo(etRemark.getText().toString(), etAlias.getText().toString());
                mDialog.dismiss();
                String loadingText = (String) getText(R.string.loadingtext);
                progressDialog.setMessage(loadingText);
                progressDialog.show();
            }
        });

        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                hideKeyBoard();
            }
        });
    }

    private void setEditText(EditText et, Object value) {
        et.setText(value.toString());
        et.setSelection(value.toString().length());
        et.clearFocus();
    }

    protected void hideKeyBoard() {
        // 隐藏键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(MessageEvent messageEvent) {
        switch (messageEvent.getMessage()) {
            case "刷新":
                boolean hasId = false;
                if (DevicesList.size() > 0)
                    for (int i = 0; i < DevicesList.size(); i++) {
                        MyGizWifiDevice gizWifiDevice = (MyGizWifiDevice) messageEvent.getObject();
                        if (gizWifiDevice.getGizWifiDevice().getDid().equals(DevicesList.get(i).getGizWifiDevice().getDid())) {
                            hasId = true;
                            DevicesList.get(i).setOpen(gizWifiDevice.isOpen());
                            DevicesList.get(i).setWaring(gizWifiDevice.getWaring());
                            DevicesList.get(i).setGizWifiDevice(gizWifiDevice.getGizWifiDevice());
                        }
                    }
                if (!hasId) {
                    MyGizWifiDevice gizWifiDevice = (MyGizWifiDevice) messageEvent.getObject();
                    DevicesList.add(gizWifiDevice);
                }
                UpdateUI();
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
