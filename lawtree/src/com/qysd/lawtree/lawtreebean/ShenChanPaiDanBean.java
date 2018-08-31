package com.qysd.lawtree.lawtreebean;

import java.util.List;

public class ShenChanPaiDanBean {
//        "code": "1",
//            "status": [
//        {
//            "sortValue": 1,
//                "materName": "钢钉",
//                "unitName": "个",
//                "orderCode": "180607101028",
//                "planId": 152,
//                "planNumber": 10
//        },

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
        private String sortValue;
        private String materName;
        private String unitName;

        private String orderCode;
        private String planId;
        private String planNumber;

        public String getSortValue() {
            return sortValue;
        }

        public void setSortValue(String sortValue) {
            this.sortValue = sortValue;
        }

        public String getMaterName() {
            return materName;
        }

        public void setMaterName(String materName) {
            this.materName = materName;
        }

        public String getUnitName() {
            return unitName;
        }

        public void setUnitName(String unitName) {
            this.unitName = unitName;
        }

        public String getOrderCode() {
            return orderCode;
        }

        public void setOrderCode(String orderCode) {
            this.orderCode = orderCode;
        }

        public String getPlanId() {
            return planId;
        }

        public void setPlanId(String planId) {
            this.planId = planId;
        }

        public String getPlanNumber() {
            return planNumber;
        }

        public void setPlanNumber(String planNumber) {
            this.planNumber = planNumber;
        }
    }

}
