package com.qysd.lawtree.lawtreebusbean;

public class FragmentEventBusBean {
    private String type;
    private String fragmentType;

    public FragmentEventBusBean(String type, String fragmentType) {
        this.type = type;
        this.fragmentType = fragmentType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFragmentType() {
        return fragmentType;
    }

    public void setFragmentType(String fragmentType) {
        this.fragmentType = fragmentType;
    }
}
