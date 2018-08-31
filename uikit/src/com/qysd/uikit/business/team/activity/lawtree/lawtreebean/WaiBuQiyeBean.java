package com.qysd.uikit.business.team.activity.lawtree.lawtreebean;

import java.util.List;

/**
 * Created by QYSD_AD on 2018/3/26.
 */

public class WaiBuQiyeBean {
    private String code;
    private int Pages;
    private int Total;
    private int PageNum;

    private List<LawyerList> lawyerList;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getPages() {
        return Pages;
    }

    public void setPages(int pages) {
        Pages = pages;
    }

    public int getTotal() {
        return Total;
    }

    public void setTotal(int total) {
        Total = total;
    }

    public int getPageNum() {
        return PageNum;
    }

    public void setPageNum(int pageNum) {
        PageNum = pageNum;
    }

    public List<LawyerList> getLawyerList() {
        return lawyerList;
    }

    public void setLawyerList(List<LawyerList> lawyerList) {
        this.lawyerList = lawyerList;
    }

    public class LawyerList {
        private int unReadCount;
        private String lawyerId;
        private String lawyerMob;
        private String lawyerName;
        private String workAge;
        private String practiseOrg;
        private String address;
        private String headUrl;
        private String stuts;
        private List<Fields> fields;

        public int getUnReadCount() {
            return unReadCount;
        }

        public void setUnReadCount(int unReadCount) {
            this.unReadCount = unReadCount;
        }

        public String getStuts() {
            return stuts;
        }

        public void setStuts(String stuts) {
            this.stuts = stuts;
        }

        public String getLawyerId() {
            return lawyerId;
        }

        public void setLawyerId(String lawyerId) {
            this.lawyerId = lawyerId;
        }

        public String getLawyerMob() {
            return lawyerMob;
        }

        public void setLawyerMob(String lawyerMob) {
            this.lawyerMob = lawyerMob;
        }

        public String getLawyerName() {
            return lawyerName;
        }

        public void setLawyerName(String lawyerName) {
            this.lawyerName = lawyerName;
        }

        public String getWorkAge() {
            return workAge;
        }

        public void setWorkAge(String workAge) {
            this.workAge = workAge;
        }

        public String getPractiseOrg() {
            return practiseOrg;
        }

        public void setPractiseOrg(String practiseOrg) {
            this.practiseOrg = practiseOrg;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getHeadUrl() {
            return headUrl;
        }

        public void setHeadUrl(String headUrl) {
            this.headUrl = headUrl;
        }

        public List<Fields> getFields() {
            return fields;
        }

        public void setFields(List<Fields> fields) {
            this.fields = fields;
        }

        public class Fields {
            private String fieldName;

            public String getFieldName() {
                return fieldName;
            }

            public void setFieldName(String fieldName) {
                this.fieldName = fieldName;
            }
        }
    }
}

