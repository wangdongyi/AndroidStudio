package com.gizwits.opensource.appkit.ControlModule;

import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.io.Serializable;

/**
 * 作者：王东一
 * 创建时间：2018/1/25.
 */

public class ItemViewBean implements Serializable {
    private TextView textView;
    private LinearLayout linearLayout;
    private Switch aSwitch;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public LinearLayout getLinearLayout() {
        return linearLayout;
    }

    public void setLinearLayout(LinearLayout linearLayout) {
        this.linearLayout = linearLayout;
    }

    public Switch getaSwitch() {
        return aSwitch;
    }

    public void setaSwitch(Switch aSwitch) {
        this.aSwitch = aSwitch;
    }
}
