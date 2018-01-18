package com.gizwits.opensource.appkit.CommonModule;

import com.gizwits.gizwifisdk.api.GizWifiDevice;

/**
 * 作者：王东一
 * 创建时间：2018/1/15.
 */

public class MyGizWifiDevice {
    private int Waring;
    private boolean Open;
    private GizWifiDevice gizWifiDevice;

    public GizWifiDevice getGizWifiDevice() {
        return gizWifiDevice;
    }

    public void setGizWifiDevice(GizWifiDevice gizWifiDevice) {
        this.gizWifiDevice = gizWifiDevice;
    }

    public int getWaring() {
        return Waring;
    }

    public void setWaring(int waring) {
        Waring = waring;
    }

    public boolean isOpen() {
        return Open;
    }

    public void setOpen(boolean open) {
        Open = open;
    }
}
