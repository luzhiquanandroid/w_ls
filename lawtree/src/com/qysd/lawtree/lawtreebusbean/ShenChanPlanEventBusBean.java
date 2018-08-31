package com.qysd.lawtree.lawtreebusbean;

public class ShenChanPlanEventBusBean {
    private int current;//当前属于哪个fragment
    private String startTime, endTime;
    private String productName;
    private String orderNum;

    public ShenChanPlanEventBusBean(int current, String startTime, String endTime, String productName, String orderNum) {
        this.current = current;
        this.startTime = startTime;
        this.endTime = endTime;
        this.productName = productName;
        this.orderNum = orderNum;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }
}
