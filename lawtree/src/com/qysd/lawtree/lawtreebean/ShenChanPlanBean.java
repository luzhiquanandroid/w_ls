package com.qysd.lawtree.lawtreebean;

import java.util.List;

public class ShenChanPlanBean {
    //    {
//        "code": "1",
//            "status": [
//        {
//            "actualfinnum": 0,
//                "performDateStr": "",
//                "finishstate": 0,
//                "orderCodeNick": "180528171011",
//                "plannum": 60,
//                "dOrderDateStr": "",
//                "materName": "图纸笔",
//                "compid": 2,
//                "materCodeNick": "SK-001",
//                "materid": 0,
//                "productionid": 4,
//                "productionPlanList": [],
//            "planid": 32,
//                "dicName": "箱",
//                "remarks": ""
//        },
//  ]
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

    public class Status {
        private String actualfinnum;
        private String performDateStr;
        private String finishstate;
        private String orderCodeNick;
        private String plannum;
        private String dOrderDateStr;

        private String materName;
        private String compid;
        private String materCodeNick;
        private String materid;
        private String productionid;
        //private String productionPlanList;
        private String planid;
        private String dicName;
        private String remarks;

        public String getActualfinnum() {
            return actualfinnum;
        }

        public void setActualfinnum(String actualfinnum) {
            this.actualfinnum = actualfinnum;
        }

        public String getPerformDateStr() {
            return performDateStr;
        }

        public void setPerformDateStr(String performDateStr) {
            this.performDateStr = performDateStr;
        }

        public String getFinishstate() {
            return finishstate;
        }

        public void setFinishstate(String finishstate) {
            this.finishstate = finishstate;
        }

        public String getOrderCodeNick() {
            return orderCodeNick;
        }

        public void setOrderCodeNick(String orderCodeNick) {
            this.orderCodeNick = orderCodeNick;
        }

        public String getPlannum() {
            return plannum;
        }

        public void setPlannum(String plannum) {
            this.plannum = plannum;
        }

        public String getdOrderDateStr() {
            return dOrderDateStr;
        }

        public void setdOrderDateStr(String dOrderDateStr) {
            this.dOrderDateStr = dOrderDateStr;
        }

        public String getMaterName() {
            return materName;
        }

        public void setMaterName(String materName) {
            this.materName = materName;
        }

        public String getCompid() {
            return compid;
        }

        public void setCompid(String compid) {
            this.compid = compid;
        }

        public String getMaterCodeNick() {
            return materCodeNick;
        }

        public void setMaterCodeNick(String materCodeNick) {
            this.materCodeNick = materCodeNick;
        }

        public String getMaterid() {
            return materid;
        }

        public void setMaterid(String materid) {
            this.materid = materid;
        }

        public String getProductionid() {
            return productionid;
        }

        public void setProductionid(String productionid) {
            this.productionid = productionid;
        }

        public String getPlanid() {
            return planid;
        }

        public void setPlanid(String planid) {
            this.planid = planid;
        }

        public String getDicName() {
            return dicName;
        }

        public void setDicName(String dicName) {
            this.dicName = dicName;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }
    }
}
