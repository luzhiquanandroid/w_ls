package com.qysd.lawtree.lawtreebean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by QYSD_AD on 2018/3/26.
 * 外部企业 其他联系人bean
 */

public class WaiBuQiyeOtherBean {
    //    {
//        "code": "1",
//            "status": [
//        {
//            "mobilenum": "13620678721",
//                "outcomuseid": 1,
//                "name": "老张",
//                "position": "总经理",
//                "fax": "1221",
//                "email": "2121",
//                "outcompid": 1
//        }
//    ]
//    }
    private String code;
    private List<Status> status;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Status> getStatus() {
        return status;
    }

    public void setStatus(List<Status> status) {
        this.status = status;
    }

    public class Status implements Serializable {
        private String mobilenum;
        private String outcomuseid;
        private String name;
        private String position;
        private String fax;
        private String email;
        private String outcompid;

        public String getMobilenum() {
            return mobilenum;
        }

        public void setMobilenum(String mobilenum) {
            this.mobilenum = mobilenum;
        }

        public String getOutcomuseid() {
            return outcomuseid;
        }

        public void setOutcomuseid(String outcomuseid) {
            this.outcomuseid = outcomuseid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getFax() {
            return fax;
        }

        public void setFax(String fax) {
            this.fax = fax;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getOutcompid() {
            return outcompid;
        }

        public void setOutcompid(String outcompid) {
            this.outcompid = outcompid;
        }
    }

}

