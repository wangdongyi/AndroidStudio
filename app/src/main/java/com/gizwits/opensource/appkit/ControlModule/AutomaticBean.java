package com.gizwits.opensource.appkit.ControlModule;

import java.io.Serializable;

/**
 * 作者：王东一
 * 创建时间：2018/1/25.
 */

public class AutomaticBean implements Serializable {
    private int StarHour;
    private int StarMinute;
    private int EndHour;
    private int EndMinute;
    private int Week;

    public int getStarHour() {
        return StarHour;
    }

    public void setStarHour(int starHour) {
        StarHour = starHour;
    }

    public int getStarMinute() {
        return StarMinute;
    }

    public void setStarMinute(int starMinute) {
        StarMinute = starMinute;
    }

    public int getEndHour() {
        return EndHour;
    }

    public void setEndHour(int endHour) {
        EndHour = endHour;
    }

    public int getEndMinute() {
        return EndMinute;
    }

    public void setEndMinute(int endMinute) {
        EndMinute = endMinute;
    }

    public int getWeek() {
        return Week;
    }

    public void setWeek(int week) {
        Week = week;
    }
}
