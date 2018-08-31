package com.qysd.lawtree.lawtreebean;

import java.util.List;

public class GxBean {
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
        //procedureid 工序
        //procedurename 工序名称
        private String procedureid;
        private String procedurename;

        public String getProcedureid() {
            return procedureid;
        }

        public void setProcedureid(String procedureid) {
            this.procedureid = procedureid;
        }

        public String getProcedurename() {
            return procedurename;
        }

        public void setProcedurename(String procedurename) {
            this.procedurename = procedurename;
        }
    }
}
