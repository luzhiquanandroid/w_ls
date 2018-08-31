package com.qysd.lawtree.lawtreebean;

import java.util.List;

public class OrderBean {
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
        private String orderstatus;
        private String purchaseID;
        private String delFlag;
        private String userName;
        private String compName;
        private String performdateStr;
        private String relOutCompId;
        private String dorderdateStr;
        private String orderCodeNick;
        private String mobileNum;
        private String modelFlag;
        private String outCompId;
        private String compid;
        private String orderCode;
        private String ordermemo;
        private String ordertype;

        public String getOrderstatus() {
            return orderstatus;
        }

        public void setOrderstatus(String orderstatus) {
            this.orderstatus = orderstatus;
        }

        public String getPurchaseID() {
            return purchaseID;
        }

        public void setPurchaseID(String purchaseID) {
            this.purchaseID = purchaseID;
        }

        public String getDelFlag() {
            return delFlag;
        }

        public void setDelFlag(String delFlag) {
            this.delFlag = delFlag;
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

        public String getPerformdateStr() {
            return performdateStr;
        }

        public void setPerformdateStr(String performdateStr) {
            this.performdateStr = performdateStr;
        }

        public String getRelOutCompId() {
            return relOutCompId;
        }

        public void setRelOutCompId(String relOutCompId) {
            this.relOutCompId = relOutCompId;
        }

        public String getDorderdateStr() {
            return dorderdateStr;
        }

        public void setDorderdateStr(String dorderdateStr) {
            this.dorderdateStr = dorderdateStr;
        }

        public String getOrderCodeNick() {
            return orderCodeNick;
        }

        public void setOrderCodeNick(String orderCodeNick) {
            this.orderCodeNick = orderCodeNick;
        }

        public String getMobileNum() {
            return mobileNum;
        }

        public void setMobileNum(String mobileNum) {
            this.mobileNum = mobileNum;
        }

        public String getModelFlag() {
            return modelFlag;
        }

        public void setModelFlag(String modelFlag) {
            this.modelFlag = modelFlag;
        }

        public String getOutCompId() {
            return outCompId;
        }

        public void setOutCompId(String outCompId) {
            this.outCompId = outCompId;
        }

        public String getCompid() {
            return compid;
        }

        public void setCompid(String compid) {
            this.compid = compid;
        }

        public String getOrderCode() {
            return orderCode;
        }

        public void setOrderCode(String orderCode) {
            this.orderCode = orderCode;
        }

        public String getOrdermemo() {
            return ordermemo;
        }

        public void setOrdermemo(String ordermemo) {
            this.ordermemo = ordermemo;
        }

        public String getOrdertype() {
            return ordertype;
        }

        public void setOrdertype(String ordertype) {
            this.ordertype = ordertype;
        }
    }
}
