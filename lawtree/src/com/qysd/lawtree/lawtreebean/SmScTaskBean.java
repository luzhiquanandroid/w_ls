package com.qysd.lawtree.lawtreebean;

/**
 * Created by Administrator on 2018/6/22.
 */

public class SmScTaskBean {
    //    {
//        "code": "1",
//            "status": {
//        "orderCodeNick": "180607101028",
//                "materName": "大号螺丝",
//                "planNum": 20,
//                "dOrderDate": 1525140524000,
//                "performDate": 1529424000000,
//                "orderMemo": "水陆草木之花，可爱者甚蕃。晋陶渊明独爱菊。自李唐来，世人甚爱牡丹。予独爱莲之出淤泥而不染，濯清涟而不妖，中通外直，不蔓不枝，香远益清，亭亭净植，可远观而不可亵玩焉。\n予谓菊，花之隐逸者也；牡丹，花之富贵者也；莲，花之君子者也。噫！菊之爱，陶后鲜有闻。莲之爱，同予者何人？牡丹之爱，宜乎众矣！",
//                "procedureName": "设计图纸",
//                "dicName": "件",
//                "taskid": 42,
//                "tasknum": 100,
//                "state": 1,
//                "dOrderDateStr": "2018/05/01",
//                "performDateStr": "2018/06/20"
//    }
//    }
    private String code;
    private Status status;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public class Status {
        private String orderCodeNick;
        private String materName;
        private String planNum;
        private String dOrderDate;
        private String performDate;
        private String orderMemo;
        private String procedureName;
        private String dicName;
        private String taskid;
        private String tasknum;
        private String state;
        private String dOrderDateStr;
        private String performDateStr;

        public String getOrderCodeNick() {
            return orderCodeNick;
        }

        public void setOrderCodeNick(String orderCodeNick) {
            this.orderCodeNick = orderCodeNick;
        }

        public String getMaterName() {
            return materName;
        }

        public void setMaterName(String materName) {
            this.materName = materName;
        }

        public String getPlanNum() {
            return planNum;
        }

        public void setPlanNum(String planNum) {
            this.planNum = planNum;
        }

        public String getdOrderDate() {
            return dOrderDate;
        }

        public void setdOrderDate(String dOrderDate) {
            this.dOrderDate = dOrderDate;
        }

        public String getPerformDate() {
            return performDate;
        }

        public void setPerformDate(String performDate) {
            this.performDate = performDate;
        }

        public String getOrderMemo() {
            return orderMemo;
        }

        public void setOrderMemo(String orderMemo) {
            this.orderMemo = orderMemo;
        }

        public String getProcedureName() {
            return procedureName;
        }

        public void setProcedureName(String procedureName) {
            this.procedureName = procedureName;
        }

        public String getDicName() {
            return dicName;
        }

        public void setDicName(String dicName) {
            this.dicName = dicName;
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

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
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
    }
}
