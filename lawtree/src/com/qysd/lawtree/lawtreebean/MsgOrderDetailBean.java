package com.qysd.lawtree.lawtreebean;

import java.util.List;

public class MsgOrderDetailBean {
    private String code;
    private List<MsgList> status;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<MsgList> getStatus() {
        return status;
    }

    public void setStatus(List<MsgList> status) {
        this.status = status;
    }

    public class MsgList {
//        "orderstatus": 3,
//                "purchaseID": 19,
//                "delFlag": 0,
//                "userName": "小王",
//                "compName": "浙江数控集团",
//                "performdateStr": "2018/06/20",
//                "relOutCompId": 0,
//                "dorderdateStr": "2018/06/07",
//                "orderCodeNick": "180607101028",
//                "mobileNum": "13988888844",
//                "modelFlag": 1,
//                "outCompId": 2,
//                "compid": 2,
//                "orderCode": "CG180607102300",
//                "ordermemo": "",
//                "ordertype": "定制订单"
        private List<OrderMaterielList> orderMaterielList;
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
        private String salesid;

        public List<OrderMaterielList> getOrderMaterielList() {
            return orderMaterielList;
        }

        public void setOrderMaterielList(List<OrderMaterielList> orderMaterielList) {
            this.orderMaterielList = orderMaterielList;
        }

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

        public String getSalesid() {
            return salesid;
        }

        public void setSalesid(String salesid) {
            this.salesid = salesid;
        }

        public class OrderMaterielList {
//            "matername": "大号螺丝",
//                    "materielType": "定制",
//                    "norms": "DHLS-1001",
//                    "num": 20
            private String matername;
            private String materielType;
            private String norms;
            private String num;

            public String getMatername() {
                return matername;
            }

            public void setMatername(String matername) {
                this.matername = matername;
            }

            public String getMaterielType() {
                return materielType;
            }

            public void setMaterielType(String materielType) {
                this.materielType = materielType;
            }

            public String getNorms() {
                return norms;
            }

            public void setNorms(String norms) {
                this.norms = norms;
            }

            public String getNum() {
                return num;
            }

            public void setNum(String num) {
                this.num = num;
            }
        }
    }
}
