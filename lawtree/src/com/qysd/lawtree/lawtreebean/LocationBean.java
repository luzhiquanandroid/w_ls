package com.qysd.lawtree.lawtreebean;

import java.util.List;

/**
 * Created by QYSD_AD on 2017/5/5.
 */

public class LocationBean {
    private String code;
    private String stuts;
    private List<RegionInfo> regionInfo;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStuts() {
        return stuts;
    }

    public void setStuts(String stuts) {
        this.stuts = stuts;
    }

    public List<RegionInfo> getRegionInfo() {
        return regionInfo;
    }

    public void setRegionInfo(List<RegionInfo> regionInfo) {
        this.regionInfo = regionInfo;
    }

    public class RegionInfo {
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
    }
}
