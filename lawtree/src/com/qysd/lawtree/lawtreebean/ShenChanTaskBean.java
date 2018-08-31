package com.qysd.lawtree.lawtreebean;

import java.io.Serializable;
import java.util.List;

public class ShenChanTaskBean implements Serializable {
//    {
//        "code": "1",
//            "status": [
//        {
//            "workerid": "e42ee32f609b45b4a5bb03c89d9ad7c0",
//                "performDateStr": "2018-06-20",
//                "finishnum": 0,
//                "orderCodeNick": "180607101028",
//                "planNum": 10,
//                "dOrderDateStr": "2018-05-01",
//                "materName": "智能电子",
//                "compid": 1,
//                "materCodeNick": "CP-002",
//                "procedureName": "切纸",
//                "planid": 153,
//                "state": 1,
//                "workerName": "去洗澡了",
//                "dicName": "箱",
//                "remarks": "",
//                "taskid": 29,
//                "tasknum": 23
    //                "orderMemo":订单备注
//        }
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

    public class Status implements Serializable {
        private String workerid;
        private String performDateStr;
        private String finishnum;
        private String orderCodeNick;
        private String planNum;
        private String dOrderDateStr;
        private String materName;
        private String compid;
        private String materCodeNick;
        private String procedureName;
        private String planid;
        private String state;
        private String workerName;
        private String dicName;
        private String remarks;
        private String taskid;
        private String tasknum;
        private String orderMemo;

        public String getOrderMemo() {
            return orderMemo;
        }

        public void setOrderMemo(String orderMemo) {
            this.orderMemo = orderMemo;
        }

        public String getWorkerid() {
            return workerid;
        }

        public void setWorkerid(String workerid) {
            this.workerid = workerid;
        }

        public String getPerformDateStr() {
            return performDateStr;
        }

        public void setPerformDateStr(String performDateStr) {
            this.performDateStr = performDateStr;
        }

        public String getFinishnum() {
            return finishnum;
        }

        public void setFinishnum(String finishnum) {
            this.finishnum = finishnum;
        }

        public String getOrderCodeNick() {
            return orderCodeNick;
        }

        public void setOrderCodeNick(String orderCodeNick) {
            this.orderCodeNick = orderCodeNick;
        }

        public String getPlanNum() {
            return planNum;
        }

        public void setPlanNum(String planNum) {
            this.planNum = planNum;
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

        public String getProcedureName() {
            return procedureName;
        }

        public void setProcedureName(String procedureName) {
            this.procedureName = procedureName;
        }

        public String getPlanid() {
            return planid;
        }

        public void setPlanid(String planid) {
            this.planid = planid;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getWorkerName() {
            return workerName;
        }

        public void setWorkerName(String workerName) {
            this.workerName = workerName;
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

        public String getTaskid() {
            return taskid;
        }

        public void setTaskid(String taskid) {
            this.taskid = taskid;
        }

        public String getTasknum() {
            return tasknum;
        }

        public void setTasknum(String tasknum) {
            this.tasknum = tasknum;
        }
    }
}
