package com.qysd.lawtree.lawtreebean;

import java.util.List;

/**
 * Created by QYSD_AD on 2017/11/16.
 */

public class RepertoryListBean {
    private String code;

    private List<LeaveList> status;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<LeaveList> getStatus() {
        return status;
    }

    public void setStatus(List<LeaveList> status) {
        this.status = status;
    }

    public static class LeaveList{
//        "number": 900,
//        "materName": "图纸笔",
//        "uinitName": "箱",
//        "norms": "700"
        private String number;
        private String materName;
        private String uinitName;
        private String norms;

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getMaterName() {
            return materName;
        }

        public void setMaterName(String materName) {
            this.materName = materName;
        }

        public String getUinitName() {
            return uinitName;
        }

        public void setUinitName(String uinitName) {
            this.uinitName = uinitName;
        }

        public String getNorms() {
            return norms;
        }

        public void setNorms(String norms) {
            this.norms = norms;
        }
    }
}
