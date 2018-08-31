package com.qysd.lawtree.lawtreebusbean;

/**
 * Created by Administrator on 2018/6/19.
 * 我的企业添加员工和部门，添加成功刷新操作
 */

public class AddPersonOrDepartmentBusBean {
    private String type;

    public AddPersonOrDepartmentBusBean(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
