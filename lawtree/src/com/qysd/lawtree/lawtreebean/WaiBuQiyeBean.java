package com.qysd.lawtree.lawtreebean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by QYSD_AD on 2018/3/26.
 */

public class WaiBuQiyeBean {
    //    {
//        "code": "1",
//            "status": [
//        {
//            "ifHelp": 1,  1是外协
//    "depUserName": "刘明明", 分管部门人姓名
//    "depName": "Java部门", 分管部门
//                "mobileNum": "17710740964", 业务联系人手机号
//                "outCompId": 3, 外部企业id
//                "property": 1, 1客户 2 供应商  3客户和供应商
//                "userName": "宋龙龙", 业务联系人姓名
//                "compName": "哦JOJO", 企业名称
//                "signId": "100003", 外部企业编码
//                "logoUrl": "http://192.168.1.155/images/13775cfb927d404ea23e0faebfa33dbb.png" 企业logo
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
        private String ifHelp;
        private String mobileNum;
        private String outCompId;
        private String property;
        private String userName;
        private String compName;
        private String signId;
        private String logoUrl;
        private String depUserName;
        private String depName;


        public String getDepUserName() {
            return depUserName;
        }

        public void setDepUserName(String depUserName) {
            this.depUserName = depUserName;
        }

        public String getDepName() {
            return depName;
        }

        public void setDepName(String depName) {
            this.depName = depName;
        }

        public String getIfHelp() {
            return ifHelp;
        }

        public void setIfHelp(String ifHelp) {
            this.ifHelp = ifHelp;
        }

        public String getMobileNum() {
            return mobileNum;
        }

        public void setMobileNum(String mobileNum) {
            this.mobileNum = mobileNum;
        }

        public String getOutCompId() {
            return outCompId;
        }

        public void setOutCompId(String outCompId) {
            this.outCompId = outCompId;
        }

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getCompName() {
            return compName;
        }

        public void setCompName(String compName) {
            this.compName = compName;
        }

        public String getSignId() {
            return signId;
        }

        public void setSignId(String signId) {
            this.signId = signId;
        }

        public String getLogoUrl() {
            return logoUrl;
        }

        public void setLogoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
        }
    }

}

