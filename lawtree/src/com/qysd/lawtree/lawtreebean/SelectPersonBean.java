package com.qysd.lawtree.lawtreebean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by QYSD_AD on 2018/3/16.
 */

public class SelectPersonBean implements Serializable {
    //    {
//            "status": [
//        {
//            "birthDay": "",
//                "mobileNum": "13620678721",
//                "reqStatus": 0,//0未接受  1已同意
//                "idCard": "1324",
//                "sex": 1,
//                "deptId": 0,
//                "sign": "",
//                "position": "总经理",
//                "userName": "张志杰",
//                "compName": "",
//                "userId": "7dbc673447ff4fbeb2b8232ce8d7e3d8"
//            "birthDayStr": null,
//            "headUrl": "/images/cef4626b3ffc4044affa105316e7f399.png",
//            "compId": null
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
        private String birthDay;
        private String mobileNum;
        private String reqStatus;
        private String sex;
        private String idCard;
        private String deptId;
        private String sign;
        private String position;
        private String userName;
        private String compName;
        private String userId;
        private String birthDayStr;
        private String headUrl;
        private String compId;

        public String getBirthDayStr() {
            return birthDayStr;
        }

        public void setBirthDayStr(String birthDayStr) {
            this.birthDayStr = birthDayStr;
        }

        public String getHeadUrl() {
            return headUrl;
        }

        public void setHeadUrl(String headUrl) {
            this.headUrl = headUrl;
        }

        public String getCompId() {
            return compId;
        }

        public void setCompId(String compId) {
            this.compId = compId;
        }

        public String getBirthDay() {
            return birthDay;
        }

        public void setBirthDay(String birthDay) {
            this.birthDay = birthDay;
        }

        public String getMobileNum() {
            return mobileNum;
        }

        public void setMobileNum(String mobileNum) {
            this.mobileNum = mobileNum;
        }

        public String getReqStatus() {
            return reqStatus;
        }

        public void setReqStatus(String reqStatus) {
            this.reqStatus = reqStatus;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getIdCard() {
            return idCard;
        }

        public void setIdCard(String idCard) {
            this.idCard = idCard;
        }

        public String getDeptId() {
            return deptId;
        }

        public void setDeptId(String deptId) {
            this.deptId = deptId;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getCompName() {
            return compName;
        }

        public void setCompName(String compName) {
            this.compName = compName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }


}
