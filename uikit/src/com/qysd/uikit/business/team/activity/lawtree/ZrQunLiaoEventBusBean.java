package com.qysd.uikit.business.team.activity.lawtree;

import org.greenrobot.eventbus.EventBus;

public class ZrQunLiaoEventBusBean {
    private String type;
    private String account;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public ZrQunLiaoEventBusBean(String type, String account) {
        this.type = type;
        this.account = account;
    }
}
