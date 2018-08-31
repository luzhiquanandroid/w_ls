package com.qysd.lawtree.lawtreebean;

import java.util.List;

public class ShenChanPlanDetailBean {
    //    {

    //        "planid": null,
//            "materid": null,
//            "productionid": null,
//            "plannum": 2,
//            "actualfinnum": null,
//            "finishstate": null,
//            "compid": null,
//            "orderCodeNick": "180604131024",
//            "dOrderDateStr": "2018-06-04",
//            "performDateStr": "2018-06-12",
//            "remarks": "",
//            "materCodeNick": "SJ",
//            "materName": "华为a9",
//            "dicName": "件",
//            "productionPlanList": [
//        {
//            "procedureName": "组装",
//                "state": 2,
//                "workerName": "赵六",
//                "procedureId": 11
//        }
//  ]
//    }
    private String code;
    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public class Status {
        private String planid;
        private String materid;
        private String productionid;
        private String plannum;
        private String actualfinnum;
        private String finishstate;
        private String orderCodeNick;
        private String compid;

        private String dOrderDateStr;
        private String performDateStr;
        private String remarks;
        private String materCodeNick;
        private String materName;
        private String dicName;

        private List<ProductionPlanList> productionPlanList;

        public String getPlanid() {
            return planid;
        }

        public void setPlanid(String planid) {
            this.planid = planid;
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

        public String getPlannum() {
            return plannum;
        }

        public void setPlannum(String plannum) {
            this.plannum = plannum;
        }

        public String getActualfinnum() {
            return actualfinnum;
        }

        public void setActualfinnum(String actualfinnum) {
            this.actualfinnum = actualfinnum;
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

        public String getCompid() {
            return compid;
        }

        public void setCompid(String compid) {
            this.compid = compid;
        }

        public String getdOrderDateStr() {
            return dOrderDateStr;
        }

        public void setdOrderDateStr(String dOrderDateStr) {
            this.dOrderDateStr = dOrderDateStr;
        }

        public String getPerformDateStr() {
            return performDateStr;
        }

        public void setPerformDateStr(String performDateStr) {
            this.performDateStr = performDateStr;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public String getMaterCodeNick() {
            return materCodeNick;
        }

        public void setMaterCodeNick(String materCodeNick) {
            this.materCodeNick = materCodeNick;
        }

        public String getMaterName() {
            return materName;
        }

        public void setMaterName(String materName) {
            this.materName = materName;
        }

        public String getDicName() {
            return dicName;
        }

        public void setDicName(String dicName) {
            this.dicName = dicName;
        }

        public List<ProductionPlanList> getProductionPlanList() {
            return productionPlanList;
        }

        public void setProductionPlanList(List<ProductionPlanList> productionPlanList) {
            this.productionPlanList = productionPlanList;
        }

        public class ProductionPlanList {
            private String procedureName;
            private String workerName;
            private String procedureId;
            private String state;//1.是未完成 2.是已完成

            public String getProcedureName() {
                return procedureName;
            }

            public void setProcedureName(String procedureName) {
                this.procedureName = procedureName;
            }

            public String getWorkerName() {
                return workerName;
            }

            public void setWorkerName(String workerName) {
                this.workerName = workerName;
            }

            public String getProcedureId() {
                return procedureId;
            }

            public void setProcedureId(String procedureId) {
                this.procedureId = procedureId;
            }

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }
        }
    }
}
