package com.qysd.uikit.business.team.activity.lawtree.lawtreebean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by QYSD_AD on 2018/3/16.
 */

public class SelectPersonBean implements Serializable {
    //    {
//        "code": "1",
//            "status": [
//        {
//            "birthDay": {
//            "date": 17,
//                    "hours": 18,
//                    "seconds": 2,
//                    "month": 3,
//                    "timezoneOffset": -480,
//                    "year": 118,
//                    "minutes": 8,
//                    "time": 1523959682197,
//                    "day": 2
//        },
//            "idCard": "",
//                "sex": 1,
//                "deptId": 27,
//                "headUrl": "/images/cef4626b3ffc4044affa105316e7f399.png",
//                "sign": "哈哈哈哈还哈",
//                "userName": "刘明明",
//                "compName": "",
//                "userId": "dfad7dc2bc0145dc90c2bde32f219c56",
//                "mobileNum": "13141103344",
//                "reqStatus": 3,
//                "compId": "",
//                "birthDayStr": "",
//                "position": "高级iOS开发工程师"
//        },
//    ]
    private String code;
    private List<SelectPersonBean.Status> status;

    public List<SelectPersonBean.Status> getStatus() {
        return status;
    }

    public void setStatus(List<SelectPersonBean.Status> status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static class Status implements Serializable {
        private String compId;
        private String compName;
        private String deptId;

        private String headUrl;
        private String idCard;
        private String mobileNum;
        private String position;
        private String reqStatus;
        private String sex;
        private String sign;
        private String userId;
        private String userName;
       // private SelectPersonBean.Status.BirthDay birthDay;

        public Status(String userName, String userId) {
            this.userName = userName;
            this.userId = userId;
        }

        public String getCompId() {
            return compId;
        }

        public void setCompId(String compId) {
            this.compId = compId;
        }

        public String getCompName() {
            return compName;
        }

        public void setCompName(String compName) {
            this.compName = compName;
        }

        public String getDeptId() {
            return deptId;
        }

        public void setDeptId(String deptId) {
            this.deptId = deptId;
        }

        public String getHeadUrl() {
            return headUrl;
        }

        public void setHeadUrl(String headUrl) {
            this.headUrl = headUrl;
        }

        public String getIdCard() {
            return idCard;
        }

        public void setIdCard(String idCard) {
            this.idCard = idCard;
        }

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

//        public SelectPersonBean.Status.BirthDay getBirthDay() {
//            return birthDay;
//        }
//
//        public void setBirthDay(SelectPersonBean.Status.BirthDay birthDay) {
//            this.birthDay = birthDay;
//        }

        public class BirthDay implements Serializable {
            private String date;
            private String day;
            private String hours;
            private String minutes;
            private String month;
            private String seconds;
            private String time;
            private String timezoneOffset;
            private String year;

            public String getDay() {
                return day;
            }

            public void setDay(String day) {
                this.day = day;
            }

            public String getHours() {
                return hours;
            }

            public void setHours(String hours) {
                this.hours = hours;
            }

            public String getMinutes() {
                return minutes;
            }

            public void setMinutes(String minutes) {
                this.minutes = minutes;
            }

            public String getMonth() {
                return month;
            }

            public void setMonth(String month) {
                this.month = month;
            }

            public String getSeconds() {
                return seconds;
            }

            public void setSeconds(String seconds) {
                this.seconds = seconds;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getTimezoneOffset() {
                return timezoneOffset;
            }

            public void setTimezoneOffset(String timezoneOffset) {
                this.timezoneOffset = timezoneOffset;
            }

            public String getYear() {
                return year;
            }

            public void setYear(String year) {
                this.year = year;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }
        }
    }
//    //    {
////            "status": [
////        {
////            "birthDay": "",
////                "mobileNum": "13620678721",
////                "reqStatus": 0,//0未接受  1已同意
////                "idCard": "1324",
////                "sex": 1,
////                "deptId": 0,
////                "sign": "",
////                "position": "总经理",
////                "userName": "张志杰",
////                "compName": "",
////                "userId": "7dbc673447ff4fbeb2b8232ce8d7e3d8"
////        }
////  ]
////    }
//    private String code;
//    private List<Status> status;
//
//    public String getCode() {
//        return code;
//    }
//
//    public void setCode(String code) {
//        this.code = code;
//    }
//
//    public List<Status> getStatus() {
//        return status;
//    }
//
//    public void setStatus(List<Status> status) {
//        this.status = status;
//    }
//
//    public static class Status implements Serializable {
//        public Status(String userName, String userId) {
//            this.userName = userName;
//            this.userId = userId;
//        }
//
//        private String birthDay;
//        private String mobileNum;
//        private String reqStatus;
//        private String sex;
//        private String idCard;
//        private String deptId;
//        private String sign;
//        private String position;
//        private String userName;
//        private String compName;
//        private String userId;
//
//        public String getBirthDay() {
//            return birthDay;
//        }
//
//        public void setBirthDay(String birthDay) {
//            this.birthDay = birthDay;
//        }
//
//        public String getMobileNum() {
//            return mobileNum;
//        }
//
//        public void setMobileNum(String mobileNum) {
//            this.mobileNum = mobileNum;
//        }
//
//        public String getReqStatus() {
//            return reqStatus;
//        }
//
//        public void setReqStatus(String reqStatus) {
//            this.reqStatus = reqStatus;
//        }
//
//        public String getSex() {
//            return sex;
//        }
//
//        public void setSex(String sex) {
//            this.sex = sex;
//        }
//
//        public String getIdCard() {
//            return idCard;
//        }
//
//        public void setIdCard(String idCard) {
//            this.idCard = idCard;
//        }
//
//        public String getDeptId() {
//            return deptId;
//        }
//
//        public void setDeptId(String deptId) {
//            this.deptId = deptId;
//        }
//
//        public String getSign() {
//            return sign;
//        }
//
//        public void setSign(String sign) {
//            this.sign = sign;
//        }
//
//        public String getPosition() {
//            return position;
//        }
//
//        public void setPosition(String position) {
//            this.position = position;
//        }
//
//        public String getUserName() {
//            return userName;
//        }
//
//        public void setUserName(String userName) {
//            this.userName = userName;
//        }
//
//        public String getCompName() {
//            return compName;
//        }
//
//        public void setCompName(String compName) {
//            this.compName = compName;
//        }
//
//        public String getUserId() {
//            return userId;
//        }
//
//        public void setUserId(String userId) {
//            this.userId = userId;
//        }
//    }


}
