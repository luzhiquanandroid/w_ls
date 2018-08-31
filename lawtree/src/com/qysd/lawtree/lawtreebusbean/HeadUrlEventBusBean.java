package com.qysd.lawtree.lawtreebusbean;

public class HeadUrlEventBusBean {
    private String name;
    private String headUrl;

    public HeadUrlEventBusBean(String name,String headUrl) {
        this.name = name;
        this.headUrl = headUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }
}
