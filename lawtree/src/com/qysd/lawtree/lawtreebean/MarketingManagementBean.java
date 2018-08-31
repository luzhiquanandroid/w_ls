package com.qysd.lawtree.lawtreebean;

import java.util.List;

public class MarketingManagementBean {
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

    public class Status {
//                  "source": 2,
//                "compName": "浙江玉环机械一厂",
//                "performdateStr": "2018/05/31",
//                "ordercode": "CG180528171489",
//                "compid": 2,
//                "createdby": "",
//                "delflag": 0,
//                "ordercodenick": "180528171011",
//                "outcompid": 1,
//                "createtime": {
//                "date": 28,
//                    "hours": 17,
//                    "seconds": 31,
//                    "month": 4,
//                    "timezoneOffset": -480,
//                    "year": 118,
//                    "minutes": 59,
//                    "time": 1527501571000,
//                    "day": 1
//        },
        private String ordercode;
        private String modifyby;
        private String orderstatus;
        private String busiUserId;
        private String userName;
        private String dorderdateStr;
        private String mobileNum;
        private String salesid;
        private String ordermemo;
        private String ordercodenick;
        private String performdateStr;
        private String compName;

        public String getOrdercode() {
            return ordercode;
        }

        public void setOrdercode(String ordercode) {
            this.ordercode = ordercode;
        }

        public String getModifyby() {
            return modifyby;
        }

        public void setModifyby(String modifyby) {
            this.modifyby = modifyby;
        }

        public String getOrderstatus() {
            return orderstatus;
        }

        public void setOrderstatus(String orderstatus) {
            this.orderstatus = orderstatus;
        }

        public String getBusiUserId() {
            return busiUserId;
        }

        public void setBusiUserId(String busiUserId) {
            this.busiUserId = busiUserId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getDorderdateStr() {
            return dorderdateStr;
        }

        public void setDorderdateStr(String dorderdateStr) {
            this.dorderdateStr = dorderdateStr;
        }

        public String getMobileNum() {
            return mobileNum;
        }

        public void setMobileNum(String mobileNum) {
            this.mobileNum = mobileNum;
        }

        public String getSalesid() {
            return salesid;
        }

        public void setSalesid(String salesid) {
            this.salesid = salesid;
        }

        public String getOrdermemo() {
            return ordermemo;
        }

        public void setOrdermemo(String ordermemo) {
            this.ordermemo = ordermemo;
        }

        public String getOrdercodenick() {
            return ordercodenick;
        }

        public void setOrdercodenick(String ordercodenick) {
            this.ordercodenick = ordercodenick;
        }

        public String getPerformdateStr() {
            return performdateStr;
        }

        public void setPerformdateStr(String performdateStr) {
            this.performdateStr = performdateStr;
        }

        public String getCompName() {
            return compName;
        }

        public void setCompName(String compName) {
            this.compName = compName;
        }

    }
}
