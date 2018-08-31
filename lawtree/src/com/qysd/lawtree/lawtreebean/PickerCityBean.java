package com.qysd.lawtree.lawtreebean;


import com.bigkoo.pickerview.model.IPickerViewData;

/**
 * Created by Sai on 15/11/22.
 */
public class PickerCityBean implements IPickerViewData {

    private String id;
    private String name;
    private String pid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    //这个用来显示在PickerView上面的字符串,PickerView会通过IPickerViewData获取getPickerViewText方法显示出来。
    @Override
    public String getPickerViewText() {
        return name;
    }
}
