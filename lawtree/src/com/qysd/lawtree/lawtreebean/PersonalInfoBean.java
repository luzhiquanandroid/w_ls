package com.qysd.lawtree.lawtreebean;

/**
 * Created by QYSD_GT on 2017/4/21.
 */

public class PersonalInfoBean {
    private int type;
    private String str;

    public PersonalInfoBean(int type, String str) {
        this.type = type;
        this.str = str;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }
}
