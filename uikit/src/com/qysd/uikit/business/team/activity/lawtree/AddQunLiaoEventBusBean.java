package com.qysd.uikit.business.team.activity.lawtree;

import com.qysd.uikit.business.team.activity.lawtree.lawtreebean.SelectPersonBean;

import java.util.List;

public class AddQunLiaoEventBusBean {
    private String type;
    private String account;
    private int status;
    private List<SelectPersonBean.Status> selectList;

    public AddQunLiaoEventBusBean(String type, String account) {
        this.type = type;
        this.account = account;
    }

    public AddQunLiaoEventBusBean(String type, String account, int status) {
        this.type = type;
        this.account = account;
        this.status = status;
    }

    public AddQunLiaoEventBusBean(String type, List<SelectPersonBean.Status> selectList) {
        this.type = type;
        this.selectList = selectList;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<SelectPersonBean.Status> getSelectList() {
        return selectList;
    }

    public void setSelectList(List<SelectPersonBean.Status> selectList) {
        this.selectList = selectList;
    }
}
