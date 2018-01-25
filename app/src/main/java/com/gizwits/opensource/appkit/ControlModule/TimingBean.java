package com.gizwits.opensource.appkit.ControlModule;

import java.io.Serializable;

/**
 * 作者：王东一
 * 创建时间：2018/1/25.
 */

public class TimingBean implements Serializable {
    private int hour;
    private int minute;
    private int hourRight;
    private int minuteRight;

    public int getHourRight() {
        return hourRight;
    }

    public void setHourRight(int hourRight) {
        this.hourRight = hourRight;
    }

    public int getMinuteRight() {
        return minuteRight;
    }

    public void setMinuteRight(int minuteRight) {
        this.minuteRight = minuteRight;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}
