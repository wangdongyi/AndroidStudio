package com.gizwits.opensource.appkit.ControlModule;

import java.io.Serializable;

/**
 * 作者：王东一
 * 创建时间：2018/1/25.
 * 手动
 */

public class ManualBean implements Serializable {
    private Boolean KillSwitch;//一键杀菌

    public Boolean getKillSwitch() {
        return KillSwitch;
    }

    public void setKillSwitch(Boolean killSwitch) {
        KillSwitch = killSwitch;
    }
}
