package com.gizwits.opensource.appkit.DeviceModule;

import java.util.List;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceNetStatus;
import com.gizwits.opensource.appkit.CommonModule.MyGizWifiDevice;
import com.gizwits.opensource.appkit.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import wdy.business.listen.NoDoubleClickListener;
import wdy.business.util.CodeUtil;

@SuppressLint("InflateParams")
public class GosDeviceListAdapter extends BaseAdapter {

    Handler handler = new Handler();
    protected static final int UNBOUND = 99;

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    Context context;
    List<MyGizWifiDevice> deviceList;
    private Holder.OnclickRename onclickRename;

    public Holder.OnclickRename getOnclickRename() {
        return onclickRename;
    }

    public void setOnclickRename(Holder.OnclickRename onclickRename) {
        this.onclickRename = onclickRename;
    }

    public GosDeviceListAdapter(Context context, List<MyGizWifiDevice> deviceList) {
        super();
        this.context = context;
        this.deviceList = deviceList;
    }

    @Override
    public int getCount() {
        return deviceList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Holder holder;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_gos_device_list, null);
            holder = new Holder(view);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        final MyGizWifiDevice device = deviceList.get(position);
        String LAN, noLAN, unbind;
        LAN = (String) context.getText(R.string.lan);
        noLAN = (String) context.getText(R.string.no_lan);
        unbind = (String) context.getText(R.string.unbind);
        String deviceAlias = device.getGizWifiDevice().getAlias();
        String devicePN = device.getGizWifiDevice().getProductName();
        if (device.getGizWifiDevice().getNetStatus() == GizWifiDeviceNetStatus.GizDeviceOnline || device.getGizWifiDevice().getNetStatus() == GizWifiDeviceNetStatus.GizDeviceControlled) {
            if (device.getGizWifiDevice().isBind()) {// 已绑定设备
                holder.getTvDeviceMac().setText(device.getGizWifiDevice().getMacAddress());
                if (device.getGizWifiDevice().isLAN()) {
                    holder.getTvDeviceStatus().setText(LAN);
                } else {
                    holder.getTvDeviceStatus().setText(noLAN);
                }
                if (TextUtils.isEmpty(deviceAlias)) {
                    holder.getTvDeviceName().setText(devicePN);
                } else {
                    holder.getTvDeviceName().setText(deviceAlias);
                }
                holder.getImgIcon().setBackgroundColor(ContextCompat.getColor(context, R.color.top_color));
                if (device.isOpen()) {
                    holder.getTvDeviceType().setText("状态：开机");
                } else {
                    holder.getTvDeviceType().setText("状态：关机");
                }
                switch (device.getWaring()) {
                    case 0:
                        holder.getWaring().setVisibility(View.GONE);
                        break;
                    default:
                        holder.getWaring().setVisibility(View.VISIBLE);
                        Glide.with(context).load(R.drawable.waring).into(holder.getWaring());
                        break;
                }
            } else {// 未绑定设备
                holder.getTvDeviceMac().setText(device.getGizWifiDevice().getMacAddress());
                holder.getTvDeviceStatus().setText(unbind);
                if (TextUtils.isEmpty(deviceAlias)) {
                    holder.getTvDeviceName().setText(devicePN);
                } else {
                    holder.getTvDeviceName().setText(deviceAlias);
                }
                holder.getImgIcon().setBackgroundColor(ContextCompat.getColor(context, R.color.top_color));
            }

        } else {// 设备不在线
            holder.getTvDeviceMac().setText(device.getGizWifiDevice().getMacAddress());
            holder.getTvDeviceStatus().setText("离线");
            holder.getLlLeft().setBackgroundResource(R.drawable.btn_getcode_shape_gray);
            if (TextUtils.isEmpty(deviceAlias)) {
                holder.getTvDeviceName().setText(devicePN);
            } else {
                holder.getTvDeviceName().setText(deviceAlias);
            }
            holder.getImgIcon().setBackgroundColor(ContextCompat.getColor(context, R.color.gray));
            if (device.isOpen()) {
                holder.getTvDeviceType().setText("状态：开机");
            } else {
                holder.getTvDeviceType().setText("状态：关机");
            }
            switch (device.getWaring()) {
                case 0:
                    holder.getWaring().setVisibility(View.GONE);
                    break;
                default:
                    holder.getWaring().setVisibility(View.VISIBLE);
                    Glide.with(context).load(R.drawable.waring).into(holder.getWaring());
                    break;
            }
        }
        holder.getDelete1().setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                if (getOnclickRename() != null) {
                    getOnclickRename().click(device.getGizWifiDevice());
                }
            }
        });
        holder.getDelete2().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.what = UNBOUND;
                message.obj = device.getGizWifiDevice().getDid().toString();
                handler.sendMessage(message);
            }
        });
        return view;
    }
}

class Holder {
    private View view;

    Holder(View view) {
        this.view = view;
    }

    private TextView tvDeviceMac;
    private TextView tvDeviceStatus;
    private TextView tvDeviceName;
    private ImageView icon;
    private ImageView waringImage;

    public TextView getTvDeviceType() {
        if (null == tvDeviceType) {
            tvDeviceType = (TextView) view.findViewById(R.id.tvDeviceType);
        }
        return tvDeviceType;
    }

    private TextView tvDeviceType;
    private RelativeLayout delete1;
    private RelativeLayout delete2;

    private ImageView imgRight;

    private LinearLayout llLeft;

    public LinearLayout getLlLeft() {
        if (null == llLeft) {
            llLeft = (LinearLayout) view.findViewById(R.id.llLeft);
        }
        return llLeft;
    }

    public ImageView getImgIcon() {
        if (null == icon) {
            icon = (ImageView) view.findViewById(R.id.icon);
        }
        return icon;
    }

    public ImageView getWaring() {
        if (null == waringImage) {
            waringImage = (ImageView) view.findViewById(R.id.waringImage);
        }
        return waringImage;
    }

    public ImageView getImgRight() {
        if (null == imgRight) {
            imgRight = (ImageView) view.findViewById(R.id.imgRight);
        }
        return imgRight;
    }

    public RelativeLayout getDelete1() {
        if (null == delete1) {
            delete1 = (RelativeLayout) view.findViewById(R.id.delete1);
        }
        return delete1;
    }

    public RelativeLayout getDelete2() {
        if (null == delete2) {
            delete2 = (RelativeLayout) view.findViewById(R.id.delete2);
        }
        return delete2;
    }

    public TextView getTvDeviceMac() {
        if (null == tvDeviceMac) {
            tvDeviceMac = (TextView) view.findViewById(R.id.tvDeviceMac);
        }
        return tvDeviceMac;
    }

    public TextView getTvDeviceStatus() {
        if (null == tvDeviceStatus) {
            tvDeviceStatus = (TextView) view.findViewById(R.id.tvDeviceStatus);
        }
        return tvDeviceStatus;
    }

    public TextView getTvDeviceName() {
        if (null == tvDeviceName) {
            tvDeviceName = (TextView) view.findViewById(R.id.tvDeviceName);
        }
        return tvDeviceName;
    }

    public interface OnclickRename {
        void click(GizWifiDevice device);
    }
}
