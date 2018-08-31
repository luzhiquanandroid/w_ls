package com.qysd.lawtree.lawtreebusbean;

public class RedCountEventBusBean {
    private String type;
    private int count;

    public RedCountEventBusBean(String type, int count) {
        this.type = type;
        this.count = count;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
