package com.qysd.lawtree.lawtreebusbean;

import java.util.List;

public class AddPersonEventBusBean {
    private String type;
    private List<String> name;
    private List<String> id;

    public AddPersonEventBusBean(String type, List<String> name, List<String> id) {
        this.type = type;
        this.name = name;
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }

    public List<String> getId() {
        return id;
    }

    public void setId(List<String> id) {
        this.id = id;
    }
}
