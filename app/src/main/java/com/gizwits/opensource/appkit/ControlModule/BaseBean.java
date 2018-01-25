package com.gizwits.opensource.appkit.ControlModule;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 作者：王东一
 * 创建时间：2018/1/25.
 */

public class BaseBean implements Serializable {
    private Boolean AllSwitch;//总开关
    private int Type;//模式
    private ManualBean manualBean;//手动
    private AutomaticBean automaticBean;//自动
    private TimingBean timingBean;//定时
    private ArrayList<TemperatureBean> list;//温控器

    public Boolean getAllSwitch() {
        return AllSwitch;
    }

    public void setAllSwitch(Boolean allSwitch) {
        AllSwitch = allSwitch;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public ManualBean getManualBean() {
        return manualBean;
    }

    public void setManualBean(ManualBean manualBean) {
        this.manualBean = manualBean;
    }

    public AutomaticBean getAutomaticBean() {
        return automaticBean;
    }

    public void setAutomaticBean(AutomaticBean automaticBean) {
        this.automaticBean = automaticBean;
    }

    public TimingBean getTimingBean() {
        return timingBean;
    }

    public void setTimingBean(TimingBean timingBean) {
        this.timingBean = timingBean;
    }

    public ArrayList<TemperatureBean> getList() {
        return list;
    }

    public void setList(ArrayList<TemperatureBean> list) {
        this.list = list;
    }
}
