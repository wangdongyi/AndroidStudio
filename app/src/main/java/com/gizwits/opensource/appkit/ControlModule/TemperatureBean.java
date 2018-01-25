package com.gizwits.opensource.appkit.ControlModule;

import java.io.Serializable;

/**
 * 作者：王东一
 * 创建时间：2018/1/25.
 */

public class TemperatureBean implements Serializable {
    private Boolean selected;
    private int num;

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
