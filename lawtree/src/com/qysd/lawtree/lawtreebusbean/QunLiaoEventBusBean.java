package com.qysd.lawtree.lawtreebusbean;

import com.qysd.lawtree.lawtreebean.MyQiyeQunLiaoBean;
import com.qysd.lawtree.lawtreebean.SelectPersonBean;

import java.util.List;

public class QunLiaoEventBusBean {
    private String type;
    private List<MyQiyeQunLiaoBean.Status> selectList;
    private String userId;

    public QunLiaoEventBusBean(String type, String userId) {
        this.type = type;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public QunLiaoEventBusBean(String type, List<MyQiyeQunLiaoBean.Status> selectList) {
        this.type = type;
        this.selectList = selectList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<MyQiyeQunLiaoBean.Status> getSelectList() {
        return selectList;
    }

    public void setSelectList(List<MyQiyeQunLiaoBean.Status> selectList) {
        this.selectList = selectList;
    }
}
