package com.qysd.uikit.business.team.activity.lawtree.lawtreebean;

import java.util.List;

public class MyQiyeBean {
//     "code": "1",
//    "status": {
//        "departInfoAndUserInfo": [
//        {
//            "mobileNum": "string",
//                "position": "string",
//                "sign": "string",
//                "userId": "string",
//                "userName": "string"
//        }
//  ],
//        "departmentInfoExt": [
//        {
//            "compid": 0,
//                "createby": "string",
//                "createtime": "2018-03-31T05:18:12.029Z",
//                "departpath": "string",
//                "deptid": 0,
//                "deptname": "string",
//                "deptnum": "string",
//                "level": 0,
//                "memo": "string",
//                "modifyby": "string",
//                "modifytime": "2018-03-31T05:18:12.029Z",
//                "parentid": 0,
//                "proddeptflag": 0,
//                "userNum": 0
//        }
//  ]
//    }
    private String code;
    private List<DepartInfoAndUserInfo> departInfoAndUserInfo;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public class DepartInfoAndUserInfo {
       private String  mobileNum;
        private String  position;
        private String  sign;
        private String  userId;
        private String  userName;

        public String getMobileNum() {
            return mobileNum;
        }

        public void setMobileNum(String mobileNum) {
            this.mobileNum = mobileNum;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }
    private List<DepartmentInfoExt> departmentInfoExt;
    public class DepartmentInfoExt{
        private String compid;
        private String createby;
        private String createtime;
        private String departpath;
        private String deptid;
        private String deptname;
        private String deptnum;

        private String level;
        private String memo;
        private String modifyby;
        private String modifytime;
        private String parentid;
        private String proddeptflag;
        private String userNum;

        public String getCompid() {
            return compid;
        }

        public void setCompid(String compid) {
            this.compid = compid;
        }

        public String getCreateby() {
            return createby;
        }

        public void setCreateby(String createby) {
            this.createby = createby;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public String getDepartpath() {
            return departpath;
        }

        public void setDepartpath(String departpath) {
            this.departpath = departpath;
        }

        public String getDeptid() {
            return deptid;
        }

        public void setDeptid(String deptid) {
            this.deptid = deptid;
        }

        public String getDeptname() {
            return deptname;
        }

        public void setDeptname(String deptname) {
            this.deptname = deptname;
        }

        public String getDeptnum() {
            return deptnum;
        }

        public void setDeptnum(String deptnum) {
            this.deptnum = deptnum;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getMemo() {
            return memo;
        }

        public void setMemo(String memo) {
            this.memo = memo;
        }

        public String getModifyby() {
            return modifyby;
        }

        public void setModifyby(String modifyby) {
            this.modifyby = modifyby;
        }

        public String getModifytime() {
            return modifytime;
        }

        public void setModifytime(String modifytime) {
            this.modifytime = modifytime;
        }

        public String getParentid() {
            return parentid;
        }

        public void setParentid(String parentid) {
            this.parentid = parentid;
        }

        public String getProddeptflag() {
            return proddeptflag;
        }

        public void setProddeptflag(String proddeptflag) {
            this.proddeptflag = proddeptflag;
        }

        public String getUserNum() {
            return userNum;
        }

        public void setUserNum(String userNum) {
            this.userNum = userNum;
        }
    }

    public List<DepartInfoAndUserInfo> getDepartInfoAndUserInfo() {
        return departInfoAndUserInfo;
    }

    public void setDepartInfoAndUserInfo(List<DepartInfoAndUserInfo> departInfoAndUserInfo) {
        this.departInfoAndUserInfo = departInfoAndUserInfo;
    }

    public List<DepartmentInfoExt> getDepartmentInfoExt() {
        return departmentInfoExt;
    }

    public void setDepartmentInfoExt(List<DepartmentInfoExt> departmentInfoExt) {
        this.departmentInfoExt = departmentInfoExt;
    }
}