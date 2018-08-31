package com.qysd.lawtree.lawtreebean;

import java.util.List;

public class OrderDetailBean {
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
        private List<OrderMaterielList> orderMaterielList;

        public List<OrderMaterielList> getOrderMaterielList() {
            return orderMaterielList;
        }

        public void setOrderMaterielList(List<OrderMaterielList> orderMaterielList) {
            this.orderMaterielList = orderMaterielList;
        }

        public class OrderMaterielList {
//            "matername": "内存条",
//                    "materielType": "定制",
//                    "norms": "16G",
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
