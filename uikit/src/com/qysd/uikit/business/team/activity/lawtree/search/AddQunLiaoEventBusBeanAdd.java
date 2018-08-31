package com.qysd.uikit.business.team.activity.lawtree.search;

import com.qysd.uikit.business.team.activity.lawtree.lawtreebean.SelectPersonBean;

import java.util.List;

public class AddQunLiaoEventBusBeanAdd {
    private String type;
    private String account;
    private List<SelectPersonBean.Status> selectList;

    public AddQunLiaoEventBusBeanAdd(String type, String account) {
        this.type = type;
        this.account = account;
    }

    public AddQunLiaoEventBusBeanAdd(String type, List<SelectPersonBean.Status> selectList) {
        this.type = type;
        this.selectList = selectList;
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
